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
public @interface JapexGlobal
{
   // input
   String chartType()        default Constants.DEFAULT_CHART_TYPE;
   String plotDrivers()      default Constants.DEFAULT_PLOT_DRIVERS;
   String plotGroupSize()    default Constants.DEFAULT_PLOT_GROUP_SIZE;
   String reportsDirectory() default "japex-reports";
   String resultAxis()       default Constants.DEFAULT_RESULT_AXIS;
   String resultAxisX();     // N/A
   String resultUnit();      // N/A
   String resultUnitX();     // N/A

   // internal use for Japex config generation
   String __INTERNAL_PARAM_NAME_chartType()        default Constants.CHART_TYPE;
   String __INTERNAL_PARAM_NAME_plotDrivers()      default Constants.PLOT_DRIVERS;
   String __INTERNAL_PARAM_NAME_plotGroupSize()    default Constants.PLOT_GROUP_SIZE;
   String __INTERNAL_PARAM_NAME_reportsDirectory() default Constants.REPORTS_DIRECTORY;
   String __INTERNAL_PARAM_NAME_resultAxis()       default Constants.RESULT_AXIS;
   String __INTERNAL_PARAM_NAME_resultAxisX()      default Constants.RESULT_AXIS_X;
   String __INTERNAL_PARAM_NAME_resultUnit()       default Constants.RESULT_UNIT;
   String __INTERNAL_PARAM_NAME_resultUnitX()      default Constants.RESULT_UNIT_X;
}
