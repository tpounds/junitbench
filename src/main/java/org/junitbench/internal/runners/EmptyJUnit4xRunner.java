package org.junitbench.internal.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import org.junitbench.internal.reflect.ClassHelper;

/**
 * @author Trevor Pounds
 */
public class EmptyJUnit4xRunner extends Runner
{
   @Override public Description getDescription()
      { return Description.EMPTY; }

   @Override public void run(final RunNotifier notifier)
      { /* do nothing */ }

   @Override public int testCount()
      { return 0; }

   public void doAfterClasses(final Class<?> clazz) throws Throwable
      { doAnnotatedMethods(clazz, null, AfterClass.class); }

   public void doBeforeClasses(final Class<?> clazz) throws Throwable
      { doAnnotatedMethods(clazz, null, BeforeClass.class); }

   public void doAfters(final Class<?> clazz, final Object target) throws Throwable
      { doAnnotatedMethods(clazz, target, After.class); }

   public void doBefores(final Class<?> clazz, final Object target) throws Throwable
      { doAnnotatedMethods(clazz, target, Before.class); }

   public void doAnnotatedMethods(final Class<?> clazz, final Object target, final Class<? extends Annotation> annotation) throws Throwable
   {
      try
      {
         for(Method m : ClassHelper.getMethods(clazz, annotation))
            { m.invoke(target, null); }
      }
      catch(InvocationTargetException ex)
         { throw ex.getCause(); }
   }
}
