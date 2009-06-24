package org.junitbench.jmeter.runner;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.jmeter.samplers.SampleResult;

import org.junitbench.jmeter.annotation.Sampler;
import org.junitbench.jmeter.annotation.ThreadGroup;
import org.junitbench.jmeter.results.SampleResultWriter;
import org.junitbench.reflect.ClassHelper;

/**
 * @author Trevor Pounds
 */
public class ThreadGroupInterceptor implements MethodInterceptor
{
   public interface INTERFACE { public void __INTERNAL_RUNNER__(); }
   public static Method METHOD = null;

   private SampleResultWriter writer = null;

   public ThreadGroupInterceptor(SampleResultWriter writer)
      { this.writer = writer; }

   public static Object create(Class<?> clazz, SampleResultWriter writer)
      { return Enhancer.create(clazz, new Class[] { INTERFACE.class }, new ThreadGroupInterceptor(writer)); }

   public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
   {
      if(method.equals(METHOD))
      {
         // XXX: use super class because cglib does not emitt annotations
         //      to generated subclasses even when used with @Inherited
         final Class<?> testClass  = obj.getClass().getSuperclass();
         final Object   testObject = obj;

         ThreadGroup threadGroup = testClass.getAnnotation(ThreadGroup.class);
         ExecutorService service = Executors.newFixedThreadPool(threadGroup.threads());
         for(int i = 0; i < threadGroup.threads(); i++)
         {
            final String threadName = "thread-" + i;
            service.execute(new Runnable()
            {
               public void run()
               {
                  try
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
                           writer.addResult(result);
                        }
                     }
                  }
                  catch(Throwable t)
                     { t.printStackTrace(); }
               }
            });
            Thread.sleep(threadGroup.rampUpPeriod());
         }
         service.shutdown();
         if(!service.awaitTermination(threadGroup.timeout(), TimeUnit.MILLISECONDS))
         {
            // TODO handle timeout
         }
         return null;
      }
      return proxy.invokeSuper(obj, args);
   }

   static
   {
      try
         { METHOD = INTERFACE.class.getDeclaredMethod("__INTERNAL_RUNNER__", null); }
      catch(NoSuchMethodException e)
         { e.printStackTrace(); }
   }
}
