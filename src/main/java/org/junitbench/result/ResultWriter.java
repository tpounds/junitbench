package org.junitbench.result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junitbench.ResultsTo;

public class ResultWriter
{
   Class<?>[]   classes = null;
   List<Result> results = new LinkedList();

   public ResultWriter(Class<?>... classes)
      { this.classes = classes; }

   public void add(Result... results)
   {
      for(Result r : results) 
         { this.results.add(r); }
   }

   public void output() throws Throwable
   {
      for(Class<?> clazz : classes)
      {
         if(clazz.isAnnotationPresent(ResultsTo.CSV.class))
            { new CSV(clazz).output(); }
         if(clazz.isAnnotationPresent(ResultsTo.JMeter.class))
            { new JMeter(clazz).output(); }
         if(clazz.isAnnotationPresent(ResultsTo.STDERR.class))
            { new STD(System.err).output(); }
         if(clazz.isAnnotationPresent(ResultsTo.STDOUT.class))
            { new STD(System.out).output(); }
      }
   }

   protected interface ResultWriterImpl
      { public void output() throws Throwable; }

   protected class CSV implements ResultWriterImpl
   {
      private final static String HEADER = "Sampler ID,Thread ID,Iteration,Elapsed Time (ns),Elapsed Time (ms),Start Time (ms),End Time (ms),Error Occured?,Error Message\n";

      private File csv = null;

      public CSV(Class<?> clazz)
      {
         csv = new File(clazz.getAnnotation(ResultsTo.CSV.class).value());
         
         if(csv.getName().endsWith(".csv"))
         {
            File parent = csv.getParentFile();
            if(parent != null)
               { parent.mkdirs(); }
         }
         else
         {
            csv.mkdirs();
            csv = new File(csv, clazz.getName() + ".csv");
         }
      }

      public void output() throws Throwable
      {
         StringBuilder sb = new StringBuilder(HEADER);
         for(Result r : ResultWriter.this.results)
         {
            sb.append(r.samplerID + ",");
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

   protected class JMeter implements ResultWriterImpl
   {
      private final static String START_ROOT_ELEMENT = "<testResults version=\"1.2\">";
      private final static String END_ROOT_ELEMENT   = "</testResults>";

      private File jtl = null;

      public JMeter(Class<?> clazz)
      {
         jtl = new File(clazz.getAnnotation(ResultsTo.JMeter.class).value());
         
         if(jtl.getName().endsWith(".jtl"))
         {
            File parent = jtl.getParentFile();
            if(parent != null)
               { parent.mkdirs(); }
         }
         else
         {
            jtl.mkdirs();
            jtl = new File(jtl, clazz.getName() + ".jtl");
         }
      }

      public void output() throws Throwable
      {
         StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
         sb.append(START_ROOT_ELEMENT);
         for(Result r : ResultWriter.this.results)
         {
            sb.append("<sampleResult");
            sb.append(" lb=\"" + r.samplerID + "\"");
            sb.append(" tn=\"" + r.threadID + "-" + r.iteration + "\"");
            sb.append(" t=\"" + (r.endTimeStamp - r.startTimeStamp) + "\""); // JMeter only supports millisecond durations
            sb.append(" t_nano=\"" + r.elapsedTime + "\"");                  // write nano durations to custom attribute
            sb.append(" ts=\"" + r.endTimeStamp + "\"");
            sb.append(" s=\"" + r.error + "\"");
            // TODO: what attribute should hold error message?
            sb.append("/>");
         }
         sb.append(END_ROOT_ELEMENT);

         new FileOutputStream(jtl).write(sb.toString().getBytes());
      }
   }

   protected class STD implements ResultWriterImpl
   {
      private PrintStream out = null;

      public STD(PrintStream out)
         { this.out = out; }

      public void output()
      {
         for(Result r : ResultWriter.this.results)
         {
            // TODO: aggregate summary
            out.println("ID: " + r.samplerID + ", threadID: " + r.threadID + ", iteration: " + r.iteration + ", elapsed time (ns): " + r.elapsedTime + ", error: " + r.error);
         }
      }
   }
}
