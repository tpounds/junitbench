package org.junitbench.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Trevor Pounds
 */
public class ClassHelper
{
   public static Method[] getDeclaredMethods(Class<?> clazz, Class<? extends Annotation> annotation)
   {
      List<Method> methods = new ArrayList();
      for(Method m : clazz.getDeclaredMethods())
      {
         if(m.getAnnotation(annotation) != null)
            { methods.add(m); }
      }
      return methods.toArray(new Method[0]);
   }
}
