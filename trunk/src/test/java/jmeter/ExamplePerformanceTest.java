package org.junitbench.jmeter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import org.junitbench.ThreadGroup;
import org.junitbench.Sampler;
import org.junitbench.runner.ThreadGroupRunner;

@RunWith(ThreadGroupRunner.class)
@ThreadGroup(loops=10, threads=10, rampUpPeriod=100)
public class ExamplePerformanceTest
{
   @BeforeClass public static void startBeforeClass()
      { System.out.println("@BeforeClass"); }

   @AfterClass public static void stopAfterClass()
      { System.out.println("@AfterClass"); }

   @Before public void stopBefore()
      { System.out.println("@Before"); }

   @After public void stopAfter()
      { System.out.println("@After"); }

   @Sampler public void sampler1()
      { System.out.println("sampler1"); }

   @Sampler public void sampler2()
      { System.out.println("sampler2"); }

   @Sampler public void sampler3()
      { System.out.println("sampler3"); }
}
