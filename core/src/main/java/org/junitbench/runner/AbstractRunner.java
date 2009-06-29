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
   protected ResultWriter writer = null;

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
         doBeforeClasses(clazz);
         for(Method m : computeTestMethods())
         {
            Description sampleDesc = Description.createTestDescription(clazz, "TODO");
            notifier.fireTestStarted(sampleDesc);

            Object obj = createTestObject();
            doBefores(clazz, obj);
            m.invoke(obj, null);
            doAfters(clazz, obj);

            notifier.fireTestFinished(sampleDesc);
         }
      }
      catch(Throwable t)
         { t.printStackTrace(); }
      finally
      {
         try
            { doAfterClasses(clazz); }
         catch(Throwable t)
            { t.printStackTrace(); }

         writer.write();
      }
   }

   public void doAfterClasses(Class<?> clazz) throws Throwable
   {
      for(Method m : ClassHelper.getDeclaredMethods(clazz, AfterClass.class))
         { m.invoke(null, null); }
   }

   public void doBeforeClasses(Class<?> clazz) throws Throwable
   {
      for(Method m : ClassHelper.getDeclaredMethods(clazz, BeforeClass.class))
         { m.invoke(null, null); }
   }

   public void doAfters(Class<?> clazz, Object obj) throws Throwable
   {
      for(Method m : ClassHelper.getDeclaredMethods(clazz, After.class))
         { m.invoke(obj, null); }
   }

   public void doBefores(Class<?> clazz, Object obj) throws Throwable
   {
      for(Method m : ClassHelper.getDeclaredMethods(clazz, Before.class))
         { m.invoke(obj, null); }
   }

   public Object createTestObject() throws Throwable
      { return clazz.newInstance(); }

   public abstract Method[] computeTestMethods() throws Throwable;
}
