package org.junitbench.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import org.junitbench.GenerateResults;
import org.junitbench.Sampler;
import org.junitbench.ThreadGroup;
import org.junitbench.reflect.ClassHelper;
import org.junitbench.result.Result;
import org.junitbench.result.ResultType;
import org.junitbench.result.ResultWriter;
import org.junitbench.runner.AbstractRunner;

/**
 * @author Trevor Pounds
 */
public class ThreadGroupRunner extends AbstractRunner
{
   private Class<?> clazz = null;
   private ResultWriter results = null;

   public ThreadGroupRunner(Class<?> clazz)
   {
      this.clazz = clazz;

      GenerateResults genResults = this.clazz.getAnnotation(GenerateResults.class);
      if(genResults == null)
         { results = new ResultWriter(this.clazz.getName(), ResultType.STDOUT); }
      else
         { results = new ResultWriter(this.clazz.getName(), genResults.value()); }
   }

   public void run(RunNotifier notifier)
   {
      Description desc = Description.createTestDescription(this.clazz, "Thread Group Runner");

      try
      {
         doBeforeClasses(this.clazz);

//         doWarmupClasses(this.clazz);

         // TODO: support non-static member classes?
         Object testObject = this.clazz.newInstance();

         ThreadGroup threadGroup = this.clazz.getAnnotation(ThreadGroup.class);
         for(int i = 0; i < threadGroup.loops(); i++)
         {
            try
            {
               doBefores(this.clazz, testObject);
               runLoop(this.clazz, testObject, notifier);
            }
            catch(Throwable t)
               { throw t; }
            finally
               { doAfters(this.clazz, testObject); }
         }
      }
      catch(Throwable t)
         { notifier.fireTestFailure(new Failure(desc, t)); }
      finally
      {
         try
            { doAfterClasses(this.clazz); }
         catch(Throwable t)
            { notifier.fireTestFailure(new Failure(desc, t)); }
      }
   }

   protected void runLoop(final Class<?> testClass, final Object testObject, RunNotifier notifier)
   {
      Description desc = Description.createTestDescription(testClass, "Thread Group Loop");

      try
      {
         notifier.fireTestStarted(desc);

         ThreadGroup threadGroup = testClass.getAnnotation(ThreadGroup.class);
         ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadGroup.threads());
         for(int i = 0; i < threadGroup.threads(); i++)
         {
            final int threadNumber = i;
            pool.execute(new Runnable()
            {
               public void run()
               {
                  for(Method m : ClassHelper.getMethods(testClass, Sampler.class))
                  {
                     Result result = new Result();
                     long startTime = System.nanoTime();
                     try
                        { m.invoke(testObject, null); }
                     catch(Throwable t)
                        { result.error = true; }
                     finally
                     {
                        long totalTime = System.nanoTime() - startTime;
                        result.ID = testClass.getName() + "." + m.getName();
                        result.threadNumber = threadNumber;
                        result.error = false;
                        result.time = totalTime;
                        results.add(result);
                     }
                  }
               }
            });
            Thread.sleep(threadGroup.rampUpPeriod() / threadGroup.threads());
         }
         pool.shutdown();
         if(!pool.awaitTermination(threadGroup.timeout(), TimeUnit.MILLISECONDS))
         {
            pool.shutdownNow();
            throw new TimeoutException("Loop timed out after " + threadGroup.timeout() + "ms with " + pool.getActiveCount() + " incomplete sampler(s)!");
         }
         results.output();
      }
      catch(Throwable t)
         { notifier.fireTestFailure(new Failure(desc, t)); }
      finally
         { notifier.fireTestFinished(desc); }
   }
}
