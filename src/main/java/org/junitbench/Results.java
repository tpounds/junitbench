package org.junitbench;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Trevor Pounds
 */
@Target({})
public @interface Results
{
   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface CSV
      { String value() default "."; }

   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface JMeter
      { String value() default "."; }

   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface NONE
      {}

   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface STDERR
      {}

   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface STDOUT
      {}
}
