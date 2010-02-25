package org.junitbench.internal.result;

import org.junitbench.TestMetadata;

/**
 * A simple struct-like class that encapsulates the 
 * results from an annotated test method.
 *
 * @author Trevor Pounds
 */
public class Result
{
   // test identification
   public String  methodID;
   public String  threadID;
   public int     iteration;

   // test timing
   public long elapsedTime;    // total elapsed time in nanoseconds
   public long startTimeStamp; // milliseconds since midnight Jan 1, 1970 UTC
   public long endTimeStamp;   // milliseconds since midnight Jan 1, 1970 UTC

   // test errors
   public boolean error;
   public String  errorMessage;

   public TestMetadata userMetadata;
}
