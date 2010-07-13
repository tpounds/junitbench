package org.junitbench.examples;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import org.junitbench.Results;
import org.junitbench.Loops;
import org.junitbench.Threads;
import org.junitbench.runners.JUnitBenchRunner;

@RunWith(Suite.class)
@Suite.SuiteClasses({ConcurrentHashMapPerformanceTest.ConcurrentHashMapTest.class, ConcurrentHashMapPerformanceTest.SynchronizedHashMapTest.class})
public class ConcurrentHashMapPerformanceTest
{
   @Results(output=Results.Output.CSV, file="./target/surefire-reports")
   public static class HashMapTestFixture
   {
      protected static Map<Integer, Object> targetMap = null;

      private final static int[] inputs = new int[2000];

      @BeforeClass public static void populate()
      {
         Random rand = new Random();
         for(int i = 0; i < inputs.length; i++)
            { inputs[i] = rand.nextInt(); }
      }

      @Test public void insertions()
      {
         for(int i = 0; i < 100; i++)
            { targetMap.put(inputs[i], null); }
      }

      @Test public void deletions()
      {
         for(int i = 0; i < 100; i++)
            { targetMap.remove(inputs[i]); }
      }

      @Test public void mixedInsertionsAndDeletions()
      {
         for(int i = 0; i < 100; i++)
         {
            targetMap.put(inputs[i], null);
            targetMap.remove(inputs[inputs.length - 1 - i]);
         }
      }
   }

   @RunWith(JUnitBenchRunner.class)
   @Loops(50)
   @Threads(100)
   public static class ConcurrentHashMapTest extends HashMapTestFixture
   {
      static { targetMap = new ConcurrentHashMap(); }
   }

   @RunWith(JUnitBenchRunner.class)
   @Loops(50)
   @Threads(100)
   public static class SynchronizedHashMapTest extends HashMapTestFixture
   {
      static { targetMap = Collections.synchronizedMap(new HashMap()); }
   }
}
