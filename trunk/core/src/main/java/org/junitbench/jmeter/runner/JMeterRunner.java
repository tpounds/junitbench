package org.junitbench.jmeter.runner;

import java.util.List;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import org.junitbench.jmeter.annotation.Sampler;
import org.junitbench.jmeter.annotation.ThreadGroup;
/**
 * @author Trevor Pounds
 */
public class JMeterRunner extends BlockJUnit4ClassRunner
{
   private long samples = 1;
   private long threads = 1;

   public JMeterRunner(Class<?> clazz) throws InitializationError
   {
      super(clazz);

      ThreadGroup group = clazz.getAnnotation(ThreadGroup.class);
      this.samples = group.samples();
      this.threads = group.threads();
   }

   protected List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> clazz)
      { return super.getTestClass().getAnnotatedMethods(clazz); }

   // XXX: use internal impl class?
   @Override protected List<FrameworkMethod> computeTestMethods()
   {
      List<FrameworkMethod> methods = new ArrayList();
      for(FrameworkMethod m : getAnnotatedMethods(Sampler.class))
         { methods.add(m); }
      return methods;
   }

   protected void doBeforeClass() throws Throwable
   {
      for(FrameworkMethod m : getAnnotatedMethods(BeforeClass.class))
         { m.invokeExplosively(null); }
   }

   protected void doAfterClass() throws Throwable
   {
      for(FrameworkMethod m : getAnnotatedMethods(AfterClass.class))
         { m.invokeExplosively(null); }
   }

   protected void doBefore(Object obj) throws Throwable
   {
      for(FrameworkMethod m : getAnnotatedMethods(Before.class))
         { m.invokeExplosively(obj); }
   }

   protected void doAfter(Object obj) throws Throwable
   {
      for(FrameworkMethod m : getAnnotatedMethods(After.class))
         { m.invokeExplosively(obj); }
   }

   @Override public void run(RunNotifier notifier)
   {
try
{
      doBeforeClass();
      List<FrameworkMethod> methods = computeTestMethods();
      for(int i = 0; i < samples; i++)
      {
         Description sampleDesc = Description.createTestDescription(super.getTestClass().getJavaClass(), "Sample " + i);
         notifier.fireTestStarted(sampleDesc);

         Object obj = super.createTest();
         doBefore(obj);
         for(FrameworkMethod m : methods)
         {
            try
            {
               long start = System.nanoTime();
               m.invokeExplosively(obj);
               long elapsedTime = System.nanoTime() - start;
System.out.println("Sample" + i + ", " + m.getName() + ", " + elapsedTime);
//               SampleResult result = new SampleResult.createTestSample(start, end);
//               result.setSampleLabel();
//               result.setSuccessful();
//               result.setTimeStamp();
//               result.setThreadName();
            }
            catch(Throwable t)
               { t.printStackTrace(); } // TODO
         }
         doAfter(obj);

         // TODO: aggregate results

         notifier.fireTestFinished(sampleDesc);
      }
}
catch(Throwable t)
   { t.printStackTrace(); }
finally
{
   try
      { doAfterClass(); }
   catch(Throwable t)
      { t.printStackTrace(); }
}
   }

   @Override public int testCount()
      { return 1; }
}
