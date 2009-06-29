package org.junitbench.jmeter.runner;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.jmeter.samplers.SampleResult;

//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import org.junitbench.jmeter.annotation.Sampler;
import org.junitbench.jmeter.annotation.ThreadGroup;
import org.junitbench.jmeter.results.JTLSampleResultWriter;
import org.junitbench.jmeter.results.SampleResultWriter;
import org.junitbench.reflect.ClassHelper;
import org.junitbench.runner.AbstractRunner;

/**
 * @author Trevor Pounds
 */
public class JMeterRunner extends AbstractRunner
{
   private Class<?> clazz = null;

   public JMeterRunner(Class<?> clazz)
   {
      // FIXME: currently hardcoded to support maven and only JTL writer
      super(clazz, new org.junitbench.jmeter.results.JTLSampleResultWriter(new File("target/surefire-reports/" + clazz.getName() + ".jtl")));

      this.clazz = clazz;
   }

   @Override public Method[] computeTestMethods() throws Throwable
   {
/*
      Method m = ThreadGroupInterceptor.METHOD;
      List<Method> methods = new ArrayList();
      for(int i = 0; i < this.loops; i++)
         { methods.add(m); }
*/
      return null;
   }

   @Override public Object createTestObject() throws Throwable
      { return ThreadGroupInterceptor.create(clazz, (SampleResultWriter) writer); }

   public void run(RunNotifier notifier)
   {
      // TODO: check for invalid configurations?

      // TODO: suite of ThreadGroups
      // single ThreadGroup
      if(ClassHelper.hasAnnotation(this.clazz, ThreadGroup.class))
      {
         try
            { runThreadGroup(this.clazz, notifier); }
         catch(Throwable t)
            { notifier.fireTestFailure(new Failure(null, t)); }
      }
      // multiple nested ThreadGroups
      else
      {
         try
         {
            doBeforeClasses(this.clazz);

            Class<?>[] classes = ClassHelper.getDeclaredClasses(this.clazz, ThreadGroup.class);
            ExecutorService service = Executors.newFixedThreadPool(classes.length);
            for(final Class<?> memberClass : classes)
            {
               final RunNotifier memberNotifier = notifier;
               service.execute(new Runnable()
               {
                  public void run()
                     { runThreadGroup(memberClass, memberNotifier); }
               });
            }
            service.shutdown();
            if(!service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS))
            {
               List<Runnable> incompleteTasks = service.shutdownNow();
               throw new TimeoutException("Thread Group timed out after " + Long.MAX_VALUE + "ms with " + incompleteTasks.size() + " incomplete sampler(s)!");
            }
         }
         catch(Throwable t)
            { t.printStackTrace(); notifier.fireTestFailure(new Failure(null, t)); }
         finally
         {
            try
               { doAfterClasses(this.clazz); }
            catch(Throwable t)
               { notifier.fireTestFailure(new Failure(null, t)); }
         }
      }

      ((SampleResultWriter) writer).write();
   }

   protected void runThreadGroup(Class<?> testClass, RunNotifier notifier)
   {
      try
      {
         doBeforeClasses(testClass);

         // TODO: support non-static member classes?
         Object testObject = testClass.newInstance();

         ThreadGroup threadGroup = testClass.getAnnotation(ThreadGroup.class);
         for(int i = 0; i < threadGroup.loops(); i++)
         {
            try
            {
               doBefores(testClass, testObject);
               runLoop(testClass, testObject, notifier);
            }
            catch(Throwable t)
               {/*XXX: ignore?*/}
            finally
               { doAfters(testClass, testObject); }
         }
      }
      catch(Throwable t)
         { t.printStackTrace(); notifier.fireTestFailure(new Failure(null, t)); }
      finally
      {
         try
            { doAfterClasses(testClass); }
         catch(Throwable t)
            { notifier.fireTestFailure(new Failure(null, t)); }
      }
   }

   protected void runLoop(final Class<?> testClass, final Object testObject, RunNotifier notifier) throws Throwable
   {
      Description desc = Description.createTestDescription(testClass, "Thread Group Loop");

      try
      {
         notifier.fireTestStarted(desc);

         ThreadGroup threadGroup = testClass.getAnnotation(ThreadGroup.class);
         ExecutorService service = Executors.newFixedThreadPool(threadGroup.threads());
         for(int i = 0; i < threadGroup.threads(); i++)
         {
            final String threadName = "thread-" + i;
            service.execute(new Runnable()
            {
               public void run()
               {
                  for(Method m : ClassHelper.getDeclaredMethods(testClass, Sampler.class))
                  {
                     SampleResult result = new SampleResult();
                     try
                     {
                        result.setSampleLabel(testClass.getName() + "." + m.getName());
                        result.setThreadName(threadName);
                        result.sampleStart();
                        m.invoke(testObject, null);
                        result.setSuccessful(true);
                     }
                     catch(Throwable t)
                        { result.setSuccessful(false); }
                     finally
                     {
                        result.sampleEnd();
                        ((SampleResultWriter) writer).addResult(result);
                     }
                  }
               }
            });
            Thread.sleep(threadGroup.rampUpPeriod());
         }
         service.shutdown();
         if(!service.awaitTermination(threadGroup.timeout(), TimeUnit.MILLISECONDS))
         {
            List<Runnable> incompleteTasks = service.shutdownNow();
            throw new TimeoutException("Loop timed out after " + threadGroup.timeout() + "ms with " + incompleteTasks.size() + " incomplete sampler(s)!");
         }
      }
      catch(Throwable t)
         { notifier.fireTestFailure(new Failure(desc, t)); }
      finally
         { notifier.fireTestFinished(desc); }
   }
}
