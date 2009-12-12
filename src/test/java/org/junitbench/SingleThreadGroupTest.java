package org.junitbench;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.junitbench.ThreadGroup;
import org.junitbench.Sampler;
import org.junitbench.runner.ThreadGroupRunner;

/**
 * @author Trevor Pounds
 */
@RunWith(ThreadGroupRunner.class)
@ThreadGroup(loops=10, threads=10)
public class SingleThreadGroupTest
{
   private final static AtomicInteger afterCalls  = new AtomicInteger(0);
   private final static AtomicInteger beforeCalls = new AtomicInteger(0);

   private final static AtomicInteger sampler1Calls = new AtomicInteger(0);
   private final static AtomicInteger sampler2Calls = new AtomicInteger(0);
   private final static AtomicInteger sampler3Calls = new AtomicInteger(0);

   @AfterClass public static void doAfterClass()
   {
      assertEquals(10, beforeCalls.get());
      assertEquals(10, beforeCalls.get());

      assertEquals(100, sampler1Calls.get());
      assertEquals(100, sampler2Calls.get());
      assertEquals(100, sampler3Calls.get());
   }

   @Before public void doBefore()
      { beforeCalls.incrementAndGet(); }

   @After public void doAfter()
      { afterCalls.incrementAndGet(); }

   @Sampler public void sampler1()
      { sampler1Calls.incrementAndGet(); }

   @Sampler public void sampler2()
      { sampler2Calls.incrementAndGet(); }

   @Sampler public void sampler3()
      { sampler3Calls.incrementAndGet(); }
}
