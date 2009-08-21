package org.junitbench.jmeter;

import org.junit.After;
import org.junit.AfterClass;
//import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import org.junitbench.ThreadGroup;
import org.junitbench.Sampler;
import org.junitbench.runner.ThreadGroupSuiteRunner;

@RunWith(ThreadGroupSuiteRunner.class)
@Suite.SuiteClasses({NestedThreadGroupTest.ThreadGroup1.class, NestedThreadGroupTest.ThreadGroup2.class, NestedThreadGroupTest.ThreadGroup3.class})
public class NestedThreadGroupTest
{
   @BeforeClass public static void startBeforeClass()
      { System.out.println("@BeforeClass"); }

   @ThreadGroup(loops=1, threads=10)
   public static class ThreadGroup1
   {
      @Sampler public void sampler1()
         { System.out.println("ThreadGroup 1/Sampler 1"); }

      @Sampler public void sampler2()
         { System.out.println("ThreadGroup 1/Sampler 2"); }

      @Sampler public void sampler3()
         { System.out.println("ThreadGroup 1/Sampler 3"); }
   }

   @ThreadGroup(loops=1, threads=10)
   public static class ThreadGroup2
   {
      @Sampler public void sampler1()
         { System.out.println("ThreadGroup 2/Sampler 1"); }

      @Sampler public void sampler2()
         { System.out.println("ThreadGroup 2/Sampler 2"); }

      @Sampler public void sampler3()
         { System.out.println("ThreadGroup 2/Sampler 3"); }
   }

   @ThreadGroup(loops=10, threads=1)
   public static class ThreadGroup3
   {
      @Sampler public void sampler1()
         { System.out.println("ThreadGroup 3/Sampler 1"); }

      @Sampler public void sampler2()
         { System.out.println("ThreadGroup 3/Sampler 2"); }

      @Sampler public void sampler3()
         { System.out.println("ThreadGroup 3/Sampler 3"); }

      @After public void sleep() throws Throwable
      {
         System.out.println("XXXX: sleep");
         Thread.sleep(2000);
      }
   }

   @AfterClass public static void stopAfterClass()
      { System.out.println("@AfterClass"); }
}
