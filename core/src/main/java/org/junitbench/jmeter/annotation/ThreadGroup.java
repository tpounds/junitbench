package org.junitbench.jmeter.annotation;

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
public @interface ThreadGroup
{
   int  loops()        default 1;
   long rampUpPeriod() default 0;
   int  threads()      default 1;
   long timeout()      default Long.MAX_VALUE;
}
