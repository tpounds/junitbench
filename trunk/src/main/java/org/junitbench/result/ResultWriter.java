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
            case CSV:    /*TODO*/ break;
            case JMETER: new JMeterResultWriter().doOutput(); break;
            case NONE:   /*ignore*/ break;
            case STDOUT: new STDOUTResultWriter().doOutput(); break;
         }
      }
   }

   protected class CSVResultWriter
   {
      // TODO:
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
            sb.append(" lb=\"" + r.ID + "\"");
            sb.append(" tn=\"" + r.threadID + "\"");
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
            System.out.println("ID: " + r.ID + ", threadID: " + r.threadID + ", time: " + r.time + ", error: " + r.error);
         }
      }
   }
}
