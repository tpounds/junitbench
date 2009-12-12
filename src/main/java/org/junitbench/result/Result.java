package org.junitbench.result;

public class Result
{
   // sample error info
   public String  samplerID;
   public String  threadID;
   public int     iteration;

   // sample timing info
   public long elapsedTime;    // total elapsed time in nanoseconds
   public long startTimeStamp; // milliseconds since midnight Jan 1, 1970 UTC
   public long endTimeStamp;   // milliseconds since midnight Jan 1, 1970 UTC

   // sample error info
   public boolean error;
   public String  errorMessage;
}
