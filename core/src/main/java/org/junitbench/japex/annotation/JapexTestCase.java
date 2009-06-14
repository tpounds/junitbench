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
public @interface JapexTestCase
{
   // input
   String runIterations()    default Constants.DEFAULT_RUN_ITERATIONS;
   String runTime();
   String warmupIterations() default Constants.DEFAULT_WARMUP_ITERATIONS;
   String warmupTime();
   String inputFile();

   // internal use for Japex config generation
   String __INTERNAL_PARAM_NAME_runIterations()    default Constants.RUN_ITERATIONS;
   String __INTERNAL_PARAM_NAME_runTime()          default Constants.RUN_TIME;
   String __INTERNAL_PARAM_NAME_warmupIterations() default Constants.WARMUP_ITERATIONS;
   String __INTERNAL_PARAM_NAME_warmupTime()       default Constants.WARMUP_TIME;
   String __INTERNAL_PARAM_NAME_inputFile()        default Constants.INPUT_FILE;
}
