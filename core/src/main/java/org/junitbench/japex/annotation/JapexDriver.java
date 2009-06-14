package org.junitbench.japex.annotation;

import com.sun.japex.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 *
 * @author Trevor Pounds
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JapexDriver
{
   // input
   String driverClass();
   String numberOfThreads()  default Constants.DEFAULT_NUMBER_OF_THREADS;
   String runsPerDriver()    default Constants.DEFAULT_RUNS_PER_DRIVER;
   String warmupsPerDriver();// TODO: default Constants.WARMUPS_PER_DRIVER;

   // internal use for Japex config generation
   String __INTERNAL_PARAM_NAME_driverClass()      default Constants.DRIVER_CLASS;
   String __INTERNAL_PARAM_NAME_numberOfThreads()  default Constants.NUMBER_OF_THREADS;
   String __INTERNAL_PARAM_NAME_runsPerDriver()    default Constants.RUNS_PER_DRIVER;
   String __INTERNAL_PARAM_NAME_warmupsPerDriver() default Constants.WARMUPS_PER_DRIVER;
}
