package org.junitbench.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import org.junitbench.reflect.ClassHelper;

/**
 * @author Trevor Pounds
 */
public abstract class AbstractRunner extends Runner
{
   protected Class clazz = null;
   private ResultWriter writer = null;

   public AbstractRunner(Class<?> clazz, ResultWriter writer)
   {
      this.clazz = clazz;
      this.writer = writer;
   }

   public Description getDescription()
   {
      return null;
   }

   public void run(RunNotifier notifier)
   {
      try
      {
         doBeforeClasses();
         for(Method m : computeTestMethods())
         {
            Description sampleDesc = Description.createTestDescription(clazz, "TODO");
            notifier.fireTestStarted(sampleDesc);

            Object obj = createTestObject();
            doBefores(obj);
            m.invoke(obj, null);
            doAfters(obj);

            notifier.fireTestFinished(sampleDesc);
         }
      }
      catch(Throwable t)
         { t.printStackTrace(); }
      finally
      {
         try
            { doAfterClasses(); }
         catch(Throwable t)
            { t.printStackTrace(); }

         // TODO: write results
      }
   }

   public void doAfterClasses() throws Throwable
   {
      for(Method m : ClassHelper.getDeclaredMethods(clazz, AfterClass.class))
         { m.invoke(null, null); }
   }

   public void doBeforeClasses() throws Throwable
   {
      for(Method m : ClassHelper.getDeclaredMethods(clazz, BeforeClass.class))
         { m.invoke(null, null); }
   }

   public void doAfters(Object obj) throws Throwable
   {
      for(Method m : ClassHelper.getDeclaredMethods(clazz, After.class))
         { m.invoke(obj, null); }
   }

   public void doBefores(Object obj) throws Throwable
   {
      for(Method m : ClassHelper.getDeclaredMethods(clazz, Before.class))
         { m.invoke(obj, null); }
   }

   public Object createTestObject() throws Throwable
      { return clazz.newInstance(); }

   public abstract Method[] computeTestMethods() throws Throwable;
}
