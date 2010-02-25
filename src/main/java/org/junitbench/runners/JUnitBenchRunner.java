package org.junitbench.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import org.junitbench.Loops;
import org.junitbench.Threads;
import org.junitbench.internal.reflect.ClassHelper;
import org.junitbench.internal.result.Result;
import org.junitbench.internal.result.ResultWriter;
import org.junitbench.internal.runners.EmptyJUnit4xRunner;

/**
 * @author Trevor Pounds
 */
public class JUnitBenchRunner extends EmptyJUnit4xRunner
{
   private Class<?> clazz = null;
   private ResultWriter results = null;

   public JUnitBenchRunner(final Class<?> clazz)
      { this(clazz, null); }

   protected JUnitBenchRunner(final Class<?> clazz, final Class<?> parent)
   {
      this.clazz = clazz;
      this.results = (parent != null) ? new ResultWriter(clazz, parent) : new ResultWriter(clazz);
   }

   @Override public void run(final RunNotifier notifier)
   {
      Description desc = Description.createTestDescription(this.clazz, "Thread Group Runner");

      try
      {
         doBeforeClasses(this.clazz);

//         doWarmupClasses(this.clazz);

         // TODO: support non-static member classes?
         Object testObject = this.clazz.newInstance();

         int loops = 1;
         if(this.clazz.isAnnotationPresent(Loops.class))
            { loops = this.clazz.getAnnotation(Loops.class).value(); }
         for(int i = 0; i < loops; i++)
         {
            try
            {
               doBefores(this.clazz, testObject);
               runLoop(this.clazz, testObject, notifier, i);
            }
            catch(Throwable t)
               { throw t; }
            finally
               { doAfters(this.clazz, testObject); }
         }
         results.output();
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

   protected void runLoop(final Class<?> testClass, final Object testObject, final RunNotifier notifier, final int iteration)
   {
      Description desc = Description.createTestDescription(testClass, "Thread Group Loop");

      try
      {
         notifier.fireTestStarted(desc);

         long rampUpPeriod = 0;
         int threads = 1;
         if(testClass.isAnnotationPresent(Threads.class))
         {
            Threads ann = testClass.getAnnotation(Threads.class);
            rampUpPeriod = ann.rampUpPeriod();
            threads = ann.value();
         }
         ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
         for(int i = 0; i < threads; i++)
         {
            final String threadID = "thread-" + i;
            pool.execute(new Runnable()
            {
               public void run()
               {
                  for(Method m : ClassHelper.getMethods(testClass, Test.class))
                  {
                     Result result = new Result();
                     result.startTimeStamp = System.currentTimeMillis();
                     long startTime = System.nanoTime();
                     try
                        { m.invoke(testObject, null); }
                     catch(Throwable t)
                        { result.error = true; }
                     finally
                     {
                        result.elapsedTime = System.nanoTime() - startTime;
                        result.endTimeStamp = System.currentTimeMillis();
                        result.methodID = testClass.getName() + "." + m.getName();
                        result.threadID = threadID;
                        result.iteration = iteration;
                        result.error = false;
                        results.add(result);
                     }
                  }
               }
            });
            Thread.sleep(rampUpPeriod / threads);
         }
         pool.shutdown();
         if(!pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS))
         {
            pool.shutdownNow();
            throw new TimeoutException("Loop timed out after " + Long.MAX_VALUE + " ms with " + pool.getActiveCount() + " incomplete tests!");
         }

         notifier.fireTestFinished(desc);
      }
      catch(Throwable t)
         { notifier.fireTestFailure(new Failure(desc, t)); }
   }
}
