package org.junitbench.reflect;

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
   public static Class<?>[] getDeclaredClasses(Class<?> clazz, Class<? extends Annotation> annotation)
   {
      List<Class<?>> classes = new ArrayList();
      for(Class<?> c : clazz.getDeclaredClasses())
      {
         if(hasAnnotation(c, annotation))
            { classes.add(c); }
      }
      return classes.toArray(new Class<?>[0]);
   }

   public static Method[] getDeclaredMethods(Class<?> clazz, Class<? extends Annotation> annotation)
   {
      List<Method> methods = new ArrayList();
      for(Method m : clazz.getDeclaredMethods())
      {
         if(hasAnnotation(m, annotation))
            { methods.add(m); }
      }
      return methods.toArray(new Method[0]);
   }

   public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotation)
   {
      if(element.getAnnotation(annotation) != null)
         { return true; }
      return false;
   }
}
