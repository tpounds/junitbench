package org.junitbench.runners;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

import org.junitbench.internal.runners.EmptyJUnit4xRunner;

/**
 * @author Trevor Pounds
 */
public class JUnitBenchSuiteRunner extends EmptyJUnit4xRunner
{
   private Class<?> clazz = null;

   public JUnitBenchSuiteRunner(final Class<?> clazz)
      { this.clazz = clazz; }

   @Override public void run(final RunNotifier notifier)
   {
      // TODO: check for invalid configurations?
      Description desc = Description.createTestDescription(this.clazz, "Thread Group Suite Runner");

      try
      {
         doBeforeClasses(this.clazz);

//         doWarmupClasses(this.clazz);

         Class<?>[] classes = this.clazz.getAnnotation(Suite.SuiteClasses.class).value();
         ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(classes.length);
         for(final Class<?> memberClass : classes)
         {
            final RunNotifier memberNotifier = notifier;
            pool.execute(new Runnable()
            {
               public void run()
               {
                  // TODO: synchronized notifier?
                  Runner tgRunner = new JUnitBenchRunner(memberClass, clazz);
                  tgRunner.run(memberNotifier);
               }
            });
         }
         pool.shutdown();
         if(!pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS))
         {
            pool.shutdownNow();
            throw new TimeoutException(JUnitBenchSuiteRunner.class.getName() + " timed out after " + Long.MAX_VALUE + "ms with " + pool.getActiveCount() + " incomplete thread group(s)!");
         }
      }
      catch(Throwable t)
         { notifier.fireTestFailure(new Failure(desc, t)); }
      finally
      {
         try
            { doAfterClasses(this.clazz); }
         catch(Throwable t)
            { notifier.fireTestFailure(new Failure(desc, t)); }
      }
      // results.write();
   }
}
