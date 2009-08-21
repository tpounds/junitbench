package org.junitbench;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junitbench.result.ResultType;

/**
 * @author Trevor Pounds
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GenerateResults
{
   ResultType[] value() default ResultType.STDOUT;
}
