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
public @interface Shmoo
{
   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.METHOD, ElementType.TYPE})
   public @interface Loops
   {
      int  max()  default 1;
      int  min()  default 1;
      int  step() default 1;
   }

   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.METHOD, ElementType.TYPE})
   public @interface Threads
   {
      int  max()          default 1;
      int  min()          default 1;
      long rampUpPeriod() default 0;
      int  step()         default 1;
   }
}
