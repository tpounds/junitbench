package org.junitbench.runner;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.Runner;

import org.junitbench.reflect.ClassHelper;

/**
 * @author Trevor Pounds
 */
public abstract class AbstractRunner extends Runner
{
   public Description getDescription()
   {
      // XXX: what do I put here?
      return null;
   }

   public void doAfterClasses(Class<?> clazz) throws Throwable
   {
      for(Method m : ClassHelper.getMethods(clazz, AfterClass.class))
         { m.invoke(null, null); }
   }

   public void doBeforeClasses(Class<?> clazz) throws Throwable
   {
      for(Method m : ClassHelper.getMethods(clazz, BeforeClass.class))
         { m.invoke(null, null); }
   }

   public void doAfters(Class<?> clazz, Object obj) throws Throwable
   {
      for(Method m : ClassHelper.getMethods(clazz, After.class))
         { m.invoke(obj, null); }
   }

   public void doBefores(Class<?> clazz, Object obj) throws Throwable
   {
      for(Method m : ClassHelper.getMethods(clazz, Before.class))
         { m.invoke(obj, null); }
   }
}
