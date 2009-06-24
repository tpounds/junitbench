package org.junitbench.jmeter.results;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.jmeter.samplers.SampleResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JTLSampleResultWriter implements SampleResultWriter
{
   private final static String JTL_VERSION = "1.2";

   private File     jtlFile = null;
   private Document jtlDoc  = null;

   public JTLSampleResultWriter(File file)
   {
      this.jtlFile = file;

      try
         { jtlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(); }
      catch(Throwable t)
         { t.printStackTrace(); }
      Element testResults = jtlDoc.createElement("testResults");
      testResults.setAttribute("version", JTL_VERSION);
      jtlDoc.appendChild(testResults);
   }

   public void addResult(SampleResult result)
   {
      Element sample = jtlDoc.createElement("sample");
      sample.setAttribute("t",  Long.toString(result.getTime()));
      sample.setAttribute("ts", Long.toString(result.getTimeStamp()));
      sample.setAttribute("lb", result.getSampleLabel());
      sample.setAttribute("s",  Boolean.toString(result.isSuccessful()));
      sample.setAttribute("tn", result.getThreadName());
      jtlDoc.getFirstChild().appendChild(sample);
   }

   public void addResults(SampleResult[] results)
   {
      for(SampleResult result : results)
         { addResult(result); }
   }

   public void write()
   {
      try
      {
         Source source = new DOMSource(jtlDoc);
         Result result = new StreamResult(jtlFile);
         Transformer transformer = TransformerFactory.newInstance().newTransformer();
         transformer.transform(source, result);
      }
      catch(Throwable t)
         { t.printStackTrace(); }
   }
}
