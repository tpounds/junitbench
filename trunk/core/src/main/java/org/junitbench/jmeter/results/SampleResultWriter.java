package org.junitbench.jmeter.results;

import org.apache.jmeter.samplers.SampleResult;

import org.junitbench.runner.ResultWriter;

public interface SampleResultWriter extends ResultWriter
{
   public void addResult(SampleResult results);
   public void addResults(SampleResult[] results);

//   public void write();
}
