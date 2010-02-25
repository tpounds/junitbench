package org.junitbench;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specifying user generated metadata that
 * can be used to supplement the auto-generated results
 * which may be useful for later post-processing.
 *
 * @author Trevor Pounds
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestMetadata
{
   Detail[] value();

   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({})
   public @interface Detail
   {
      String name();
      String value();
   }
}
