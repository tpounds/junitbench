package org.junitbench.jmeter.results;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.jmeter.samplers.SampleResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JTLSampleResultWriter implements SampleResultWriter
{
   private final static String JTL_VERSION = "1.2";

   private Document jtlDoc = null;

   public JTLSampleResultWriter()
   {
      try
         { jtlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); }
      catch(Exception e)
         { /*TODO*/ }
      Element testResults = jtlDoc.createElement("testResults");
      testResults.setAttribute("version", JTL_VERSION);
      jtlDoc.appendChild(testResults);
   }

   public void addResult(SampleResult result)
   {
System.out.println(result.getTime() + " " + result.getTimeStamp() + " " + result.getSampleLabel() + " " + result.isSuccessful());
      Element sample = jtlDoc.createElement("sample");
      sample.setAttribute("t",  Long.toString(result.getTime()));
      sample.setAttribute("ts", Long.toString(result.getTimeStamp()));
      sample.setAttribute("lb", result.getSampleLabel());
      sample.setAttribute("s",  Boolean.toString(result.isSuccessful()));
//      sample.setAttribute("tn", result.getThreadName());
      jtlDoc.getFirstChild().appendChild(sample);
   }

   public void addResults(SampleResult[] results)
   {
      for(SampleResult result : results)
         { addResult(result); }
   }

//   public void write(TODO)
//   {
//   }
}
