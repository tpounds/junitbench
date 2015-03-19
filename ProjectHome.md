# JUnitBench #
Already using [JUnit](http://www.junit.org/) 4.x for testing? Adding performance tests to the mix has never been easier. JUnitBench is a set of JUnit 4.x extensions for benchmark and performance testing inspired by popular frameworks such as [Japex](https://japex.dev.java.net/), [JMeter](http://jakarta.apache.org/jmeter/), and [JUnitPerf](http://clarkware.com/software/JUnitPerf.html) without the overhead of maintaining separate code bases and configuration files. As an extension, performance tests can run alongside JUnit tests, utilizing the same infrastructure already provided by your toolchain (e.g. [Ant](http://ant.apache.org/manual/OptionalTasks/junit.html), [Maven](http://maven.apache.org/plugins/maven-surefire-plugin/), etc.).

## Requirements ##
Due to the heavy use of [annotations](http://java.sun.com/j2se/1.5.0/docs/guide/language/annotations.html) in both JUnitBench and JUnit 4.x, Java 1.5 is needed. However, Java 1.5 is only need to compile and run the performance test source, so older libraries compiled with older JDKs can still be tested using this framework.
  * **Java 1.5** - test source compiling/running (e.g. annotations)
  * **JUnit 4.x** - general framework support (e.g. annotations, runner architecture)

## Features ##
  * JUnit 4.x pluggable [Runner](http://kentbeck.github.com/junit/javadoc/latest/org/junit/runner/Runner.html) architecture with sequential and multi-threading support.
  * Performance metric target formats:
    * comma-separated values (CSV) file format
    * JMeter log format (e.g. .jtl)
    * standard streams (e.g. stderr/stdout)