package org.junitbench.examples;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import org.junitbench.Results;
import org.junitbench.Loops;
import org.junitbench.Threads;
import org.junitbench.runners.JUnitBenchSuiteRunner;

@RunWith(JUnitBenchSuiteRunner.class) // JUnitBench Suite Runner (i.e. multiple thread group classes)
@Suite.SuiteClasses({MultipleThreadGroupExampleTest.ThreadGroup1.class, MultipleThreadGroupExampleTest.ThreadGroup2.class}) // thread clases to run
public class MultipleThreadGroupExampleTest
{
   /**
    * @BeforeClass is run only once before all suite classes begin
    */
   @BeforeClass public static void doBeforeClass()
   {
      // do init stuff (i.e. setup HTTP server, etc).
   }

   /**
    * @AfterClass is run only once after all suite classes complete
    */
   @AfterClass public static void doAfterClass()
   {
      // do clean-up
   }

   /**
    * @Test methods are executed serially within the threads as follows.
    *
    * i.e.
    *
    * [ThreadGroup1 1]  [ThreadGroup2 1] ... [ThreadGroup1 n] [ThreadGroup2 n]
    *        |                 |                    |                |
    *       op1               op3                  op1              op3
    *        |                 |                    |                |
    *       op2               op4                  op2              op4
    */

   @Results(output=Results.Output.CSV, // output perf results (i.e. @Test methods) to CSV file
            file="./target/surefire-reports/MultipleThreadGroupExample-TG1.csv") // output to CSV with exact file name
   @Loops(10)   // run with 10 loops
   @Threads(10) // run each loop with 10 threads
   public static class ThreadGroup1
   {
      @Test public void op1()
      {
         // do op1 stuff
      }

      @Test public void op2()
      {
         // do op1 stuff
      }
   }

   @Results(output=Results.Output.CSV, // output perf results (i.e. @Test methods) to CSV file
            file="./target/surefire-reports/MultipleThreadGroupExample-TG2.csv") // output to CSV with exact file name
   @Loops(10)   // run with 10 loops
   @Threads(10) // run each loop with 10 threads
   public static class ThreadGroup2
   {
      @Test public void op3()
      {
         // do op1 stuff
      }

      @Test public void op4()
      {
         // do op1 stuff
      }
   }
}
