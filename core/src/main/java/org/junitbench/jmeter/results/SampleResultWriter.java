package org.junitbench.jmeter.results;

import org.apache.jmeter.samplers.SampleResult;

public interface SampleResultWriter
{
   public void addResult(SampleResult results);
   public void addResults(SampleResult[] results);

//   public void write();
}
