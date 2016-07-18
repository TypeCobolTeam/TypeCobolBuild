# TypeCobolBuild

Build engine for TypeCobol : A Prototype of an incremental Cobol compiler front-end for IBM Enterprise Cobol 5.1 for zOS syntax.

# Architecture overview

## Projects

The solution contains 3 projects
- **JTCB** is the main java project, it uses the RTC-SDK to access files located on RTC Streams.
- **TCB** is DLL (Dynamic Link Library) which implements the interoperability between the Java SDK and the .Net Framewok.
- **TypeCobolBuilder** is the Build Engine implementation directly in relation with TypeCobol infrastructure.

## Projects Dependencies
- **JTCB** depends on :
> - **JDK 1.7** or later downloadable at 
> - **RTC-Client-plainJavaLib-5.0.2** downloadable at https://jazz.net/downloads/rational-team-concert/releases/5.0.2?p=allDownloads
> - **args4j** at http://args4j.kohsuke.org/ and maven repository https://mvnrepository.com/artifact/args4j/args4j/2.33
> - **hamcrest-core** at http://hamcrest.org/JavaHamcrest/ and maven repository https://mvnrepository.com/artifact/org.hamcrest/hamcrest-core/1.3
> - **junit** at http://junit.org/junit4/ and maven repository https://mvnrepository.com/artifact/junit/junit/4.12

## Generating the solution

The solution is generated using various configuration
- **JTCB** can be generated using Maven or Gradle.
- **TCB and TypeCobolBuilder** are generated using a Visual Studio 2015 solution.
