package org.junitbench.internal.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Trevor Pounds
 */
public class ClassHelper
{
   public static Class<?>[] getClasses(Class<?> clazz, Class<? extends Annotation> annotation)
   {
      List<Class<?>> classes = new ArrayList();
      for(Class<?> c : clazz.getClasses())
      {
         if(c.isAnnotationPresent(annotation))
            { classes.add(c); }
      }
      return classes.toArray(new Class<?>[0]);
   }

   public static Class<?>[] getDeclaredClasses(Class<?> clazz, Class<? extends Annotation> annotation)
   {
      List<Class<?>> classes = new ArrayList();
      for(Class<?> c : clazz.getDeclaredClasses())
      {
         if(c.isAnnotationPresent(annotation))
            { classes.add(c); }
      }
      return classes.toArray(new Class<?>[0]);
   }

   public static Method[] getMethods(Class<?> clazz, Class<? extends Annotation> annotation)
   {
      List<Method> methods = new ArrayList();
      for(Method m : clazz.getMethods())
      {
         if(m.isAnnotationPresent(annotation))
            { methods.add(m); }
      }
      return methods.toArray(new Method[0]);
   }

   public static Method[] getDeclaredMethods(Class<?> clazz, Class<? extends Annotation> annotation)
   {
      List<Method> methods = new ArrayList();
      for(Method m : clazz.getDeclaredMethods())
      {
         if(m.isAnnotationPresent(annotation))
            { methods.add(m); }
      }
      return methods.toArray(new Method[0]);
   }
}
