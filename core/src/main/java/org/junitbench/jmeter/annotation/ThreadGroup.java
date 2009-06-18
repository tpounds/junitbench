package org.junitbench.jmeter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Trevor Pounds
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ThreadGroup
{
//   int rampUpPeriod() default 1;
//   int rampDownPeriod() default 1;
   long samples() default 1;
   long threads() default 1;
}
