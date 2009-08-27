package org.junitbench.result;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

public class ResultWriter
{
   String directory = null;
   String instanceName = null;
   ResultType[] types = null;
   List<Result> results = new LinkedList();

   public ResultWriter(String directory, String instanceName, ResultType... types)
   {
      this.directory = directory;
      this.instanceName = instanceName;
      this.types = types;
   }

   public void add(Result... results)
   {
      for(Result r : results) 
         { this.results.add(r); }
   }

   public void output() throws Throwable
   {
      for(ResultType type : types)
      {
         switch(type)
         {
            case CSV:    new CSVResultWriter().doOutput(); break;
            case JMETER: new JMeterResultWriter().doOutput(); break;
            case NONE:   /*ignore*/ break;
            case STDOUT: new STDOUTResultWriter().doOutput(); break;
         }
      }
   }

   protected class CSVResultWriter
   {
      private final static String HEADER = "sampler,thread,iteration,time,error\n";

      private void doOutput() throws Throwable
      {
         StringBuilder sb = new StringBuilder(HEADER);
         for(Result r : ResultWriter.this.results)
         {
            sb.append(r.samplerID + ",");
            sb.append(r.threadID + ",");
            sb.append(r.iteration + ",");
            sb.append(r.time + ",");
//            sb.append(r.timeStamp + ",");
            sb.append(r.error);
            sb.append("\n");
         }

         new FileOutputStream(new File(directory, instanceName + ".csv")).write(sb.toString().getBytes());
      }
   }

   protected class JMeterResultWriter
   {
      private final static String START_ROOT_ELEMENT = "<testResults version=\"1.2\">";
      private final static String END_ROOT_ELEMENT   = "</testResults>";

      private void doOutput() throws Throwable
      {
         StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
         sb.append(START_ROOT_ELEMENT);
         for(Result r : ResultWriter.this.results)
         {
            sb.append("<sampleResult");
            sb.append(" lb=\"" + r.samplerID + "\"");
            sb.append(" tn=\"" + r.threadID + "-" + r.iteration + "\"");
            sb.append(" t=\"" + r.time + "\"");
//            sb.append(" ts=\"" + r.timeStamp + "\"");
            sb.append(" s=\"" + r.error + "\"");
            sb.append("/>");
         }
         sb.append(END_ROOT_ELEMENT);

         new FileOutputStream(new File(directory, instanceName + ".jtl")).write(sb.toString().getBytes());
      }
   }

   protected class STDOUTResultWriter
   {
      private void doOutput()
      {
         for(Result r : ResultWriter.this.results)
         {
            // TODO: aggregate summary
            System.out.println("ID: " + r.samplerID + ", threadID: " + r.threadID + ", iteration: " + r.iteration + ", time: " + r.time + ", error: " + r.error);
         }
      }
   }
}
