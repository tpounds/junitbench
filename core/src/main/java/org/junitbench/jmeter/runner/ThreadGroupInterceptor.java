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
import org.junitbench.reflect.ClassHelper;

/**
 * @author Trevor Pounds
 */
public class ThreadGroupInterceptor implements MethodInterceptor
{
   public static Method METHOD = null;

   protected interface ThreadGroupInterface { public void __INTERNAL_RUNNER__(); }

   public static Object create(Class<?> clazz)
      { return Enhancer.create(clazz, new Class[] { ThreadGroupInterface.class }, new ThreadGroupInterceptor()); }

   public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
   {
      if(method.equals(METHOD))
      {
         // XXX: use super class because cglib does not emitt annotations
         //      to generated subclasses even when used with @Inherited
         final Class<?> testClass  = obj.getClass().getSuperclass();
         final Object   testObject = obj;

         Thread threadGroup = testClass.getAnnotation(ThreadGroup.class);
         ExecutorService service = Executors.newFixedThreadPool(threadGroup.threads());
         for(int i = 0; i < threadGroup.threads(); i++)
         {
            service.execute(new Runnable()
            {
               public void run()
               {
                  try
                  {
                     // TODO: timing code
                     for(Method m : ClassHelper.getDeclaredMethods(testClass, Sampler.class))
                        { m.invoke(testObject, null); }
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
         { METHOD = ThreadGroupInterface.class.getDeclaredMethod("__INTERNAL_RUNNER__", null); }
      catch(NoSuchMethodException e)
         { e.printStackTrace(); }
   }
}
