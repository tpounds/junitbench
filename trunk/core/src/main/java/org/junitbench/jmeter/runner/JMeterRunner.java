package org.junitbench.jmeter.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.ArrayList;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.jmeter.samplers.SampleResult;

import org.junitbench.jmeter.annotation.Sampler;
import org.junitbench.jmeter.annotation.ThreadGroup;
import org.junitbench.runner.AbstractRunner;

/**
 * @author Trevor Pounds
 */
public class JMeterRunner extends AbstractRunner
{
   private int loops = 1;

   public JMeterRunner(Class<?> clazz)
   {
      // TODO: fix result writer
      super(clazz, new org.junitbench.jmeter.results.JTLSampleResultWriter());

      // TODO: handle missing annotation
      ThreadGroup group = clazz.getAnnotation(ThreadGroup.class);
      if(group != null)
         { this.loops = group.loops(); }
   }

   @Override public Method[] computeTestMethods() throws Throwable
   {
      Method m = ThreadGroupInterceptor.METHOD;
      List<Method> methods = new ArrayList();
      for(int i = 0; i < this.loops; i++)
         { methods.add(m); }
      return methods.toArray(new Method[0]);
   }

   @Override public Object createTestObject() throws Throwable
      { return ThreadGroupInterceptor.create(clazz); }
}
