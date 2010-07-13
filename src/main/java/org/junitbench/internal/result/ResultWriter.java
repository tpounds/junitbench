package org.junitbench.internal.result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junitbench.Results;

public final class ResultWriter
{
   private final Collection<Result>           results = new ConcurrentLinkedQueue();
   private final Collection<ResultWriterImpl> writers = new ConcurrentLinkedQueue();
   // TODO: metadata

   public ResultWriter(final Class<?>... classes)
   {
      for(Class<?> clazz : classes)
      {
         if(clazz.isAnnotationPresent(Results.class))
         {
            Results results = clazz.getAnnotation(Results.class);
            for(Results.Output output : results.output())
            {
               switch(output)
               {
                  case CSV:    writers.add(new CSVWriter(clazz.getName(), results.file())); break;
                  case JAPEX:  /*TODO*/ break;
                  case JMETER: writers.add(new JMeterWriter(clazz.getName(), results.file())); break;
                  case NONE:   /* do nothing */ break;
                  case STDERR: writers.add(new ConsoleWriter(System.err)); break;
                  case STDOUT: writers.add(new ConsoleWriter(System.out)); break;
               }
            }
         }
      }
   }

   public void add(final Result... results)
   {
      for(Result r : results) 
         { this.results.add(r); }
   }

   public void output() throws Throwable
   {
      for(ResultWriterImpl writer : writers)
         { writer.output(); }
   }

   private interface ResultWriterImpl
      { public void output() throws Throwable; }

   private class CSVWriter implements ResultWriterImpl
   {
      private final static String HEADER = "Sampler ID,Thread ID,Iteration,Elapsed Time (ns),Elapsed Time (ms),Start Time (ms),End Time (ms),Error Occured?,Error Message\n";

      private File csv = null;

      public CSVWriter(final String className, final String file)
      {
         csv = new File(file);

         if(csv.getName().endsWith(".csv"))
         {
            File parent = csv.getParentFile();
            if(parent != null)
               { parent.mkdirs(); }
         }
         else
         {
            csv.mkdirs();
            csv = new File(csv, className + ".csv");
         }
      }

      public void output() throws Throwable
      {
         StringBuilder sb = new StringBuilder(HEADER);
         for(Result r : ResultWriter.this.results)
         {
            sb.append(r.methodID + ",");
            sb.append(r.threadID + ",");
            sb.append(r.iteration + ",");
            sb.append(r.elapsedTime + ",");
            sb.append((r.endTimeStamp - r.startTimeStamp) + ",");
            sb.append(r.startTimeStamp + ",");
            sb.append(r.endTimeStamp + ",");
            sb.append(r.error + ",");
            sb.append(r.errorMessage);
            sb.append("\n");
         }

         new FileOutputStream(csv).write(sb.toString().getBytes());
      }
   }

   private class JMeterWriter implements ResultWriterImpl
   {
      private final static String START_ROOT_ELEMENT = "<testResults version=\"1.2\">";
      private final static String END_ROOT_ELEMENT   = "</testResults>";

      private File jtl = null;

      public JMeterWriter(final String className, final String file)
      {
         jtl = new File(file);

         if(jtl.getName().endsWith(".jtl"))
         {
            File parent = jtl.getParentFile();
            if(parent != null)
               { parent.mkdirs(); }
         }
         else
         {
            jtl.mkdirs();
            jtl = new File(jtl, className + ".jtl");
         }
      }

      public void output() throws Throwable
      {
         StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
         sb.append(START_ROOT_ELEMENT);
         for(Result r : ResultWriter.this.results)
         {
            sb.append("<sampleResult");
            sb.append(" lb=\"" + r.methodID + "\"");
            sb.append(" tn=\"" + r.threadID + "-" + r.iteration + "\"");
            sb.append(" t=\"" + (r.endTimeStamp - r.startTimeStamp) + "\""); // JMeter attribute for milli duration
            sb.append(" t_nano=\"" + r.elapsedTime + "\"");                  // custom attribute for nano duration
            sb.append(" ts=\"" + r.endTimeStamp + "\"");
            sb.append(" s=\"" + r.error + "\"");
            // TODO: what attribute do we write for r.errorMessage?
            // TODO: what do we do with custom metadata?
            sb.append("/>");
         }
         sb.append(END_ROOT_ELEMENT);

         new FileOutputStream(jtl).write(sb.toString().getBytes());
      }
   }

   private class ConsoleWriter implements ResultWriterImpl
   {
      private PrintStream out = null;

      public ConsoleWriter(final PrintStream out)
         { this.out = out; }

      public void output()
      {
         for(Result r : ResultWriter.this.results)
         {
            // TODO: aggregate summary?
            out.println("ID: " + r.methodID + ", threadID: " + r.threadID + ", iteration: " + r.iteration + ", elapsed time (ns): " + r.elapsedTime + ", error: " + r.error + ", error message: " + r.errorMessage);
         }
      }
   }
}
