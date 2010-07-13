package org.junitbench.examples;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junitbench.Results;
import org.junitbench.Loops;
import org.junitbench.Threads;
import org.junitbench.runners.JUnitBenchRunner;

@RunWith(JUnitBenchRunner.class) // JUnitBench runner
@Results(output=Results.Output.CSV, // output perf results (i.e. @Test methods) to CSV file
         file="./target/surefire-reports") // write CSV file output to relative directory "target/surefire-reports"
@Loops(10)   // run with 10 loops
@Threads(10) // run each loop with 10 threads
public class SingleThreadGroupExampleTest
{
   /**
    * @BeforeClass is run only once before all testing begins
    */
   @BeforeClass public static void doBeforeClass()
   {
      // do init stuff (i.e. setup HTTP server, etc).
   }

   /**
    * @AfterClass is run only once before all testing begins
    */
   @AfterClass public static void doAfterClass()
   {
      // do clean-up
   }

   /**
    * @Before is run once before each loop
    */
   @Before public void doBefore()
   {
      // do stuff before loop
   }

   /**
    * @After is run once after each loop
    */
   @After public void doAfter()
   {
   }

   /**
    * @Test methods are executed serially within a thread.
    *
    * i.e.
    *
    * [Thread 1]  [Thread 2] ... [Thread n]
    *     |           |              |
    *    op1         op1            op1
    *     |           |              |
    *    op2         op2            op2
    *     |           |              |
    *    op3         op3            op3
    */

   @Test public void op1()
   {
      // do op1 stuff
   }

   @Test public void op2()
   {
      // do op2 stuff
   }  

   @Test public void op3()
   {
      // do op3 stuff
   }
}
