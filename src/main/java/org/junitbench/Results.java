package org.junitbench;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Trevor Pounds
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Results
{
   /**
    * Fields for specifying the output mechanism.
    */
   Output[] output() default { Output.NONE };
   String   file()   default ".";

   public enum Output { CSV, JAPEX, JMETER, NONE, STDERR, STDOUT }

   /**
    * Field for specifying user generated metadata that
    * can be used to supplement the auto-generated results;
    * particularly useful for post-processing requirements.
    */
   Metadata[] metadata() default {};

   @Retention(RetentionPolicy.RUNTIME)
   @Target({})
   public @interface Metadata
   {
      String name();
      String value();
   }
}
