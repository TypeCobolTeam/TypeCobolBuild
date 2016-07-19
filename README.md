# TypeCobolBuild

Build engine for TypeCobol : A Prototype of an incremental Cobol compiler front-end for IBM Enterprise Cobol 5.1 for zOS syntax.
The purpose of the TypeCobolBuild project is to provide a tool for compiling project using the TypeCobol gparser/generator. 

# Architecture overview

## Projects

The solution contains 3 projects
- **JTCB** is the main java project, it uses the RTC-SDK to access files located on RTC Streams.
- **TCB** is DLL (Dynamic Link Library) which implements the interoperability between the Java SDK and the .Net Framewok.
- **TypeCobolBuilder** is the Build Engine implementation directly in relation with TypeCobol infrastructure.

## Projects Dependencies
- **JTCB** depends on :
	- **JDK 1.7**(http://www.oracle.com/technetwork/java/javase/overview/index.html) or later.
	- **RTC-Client-plainJavaLib-5.0.2**(https://jazz.net/downloads/rational-team-concert/releases/5.0.2?p=allDownloads)
	- **args4j**(http://args4j.kohsuke.org/) and maven repository https://mvnrepository.com/artifact/args4j/args4j/2.33
	- **hamcrest-core**(http://hamcrest.org/JavaHamcrest/) and maven repository https://mvnrepository.com/artifact/org.hamcrest/hamcrest-core/1.3
	- **junit**(http://junit.org/junit4/) and maven repository https://mvnrepository.com/artifact/junit/junit/4.12
	- **TCB**

- **TCB** depends on:
	- **JDK 1.7 JNI**
	- **.Net Framework 4.5**("https://msdn.microsoft.com/fr-fr/library/5a4x27ek(v=vs.110).aspx")
	- **lo4gnet**(https://logging.apache.org/log4net/) with nuget at https://www.nuget.org/packages/log4net/
	- **TypeCobolBuilder**

- **TypeCobolBuilder**
	- **TCB**
	- **.Net Framework 4.5**()
	- **TypeCobol**
	- **lo4gnet**(https://logging.apache.org/log4net/) with nuget at https://www.nuget.org/packages/log4net/
	- **CommandLine**(https://github.com/gsscoder/commandline) with nuget at https://www.nuget.org/packages/CommandLineParser/
	- **Castle.Core**(http://www.castleproject.org/) with nuget at https://www.nuget.org/packages/Castle.Core/
	- **Antr4.Runtime**(https://github.com/sharwell/antlr4cs) with nuget at https://www.nuget.org/packages/Antlr4.Runtime

![Alt text](http://g.gravizo.com/g?
  digraph G {
    aize ="4,4";
    RTC_SDK [shape=box];
    JTCB [shape=box,label="JTCB.jar"];
    TCB [shape=box,label="TCB.dll"];
    TypeCobolBuilder [shape=box,label="TypeCobolBuilder.dll"];
    TypeCobol [shape=box,label="TypeCobol.exe"];
    JavaJni -> JTCB
    TCB -> JavaJni;
    TCB <-> TypeCobolBuilder;
    TypeCobolBuilder <-> TypeCobol;
    JavaJni [label="Java/JNI",style=filled,color=".7 .3 1.0"];
  }
)

![class diagram](http://yuml.me/diagram/class/
  [RTC SDK Jars]<->[JTCB.jar], 
  [JTCB.jar]<-(Java/JNI)->[TCB.dll], [note: Interoperability betwen JTCB and TCB is achieved by Java/JNI{bg:cornsilk}],
  [TCB.dll]<->[TypeCobolBuilder.dll],
  [TypeCobolBuilder.dll]<->[TypeCobol.exe]
)

## Generating the solution

The solution is generated using various configuration
- **JTCB** can be generated using:
	- **Maven (version 3.3.9)** at: https://maven.apache.org/
	- **Gradle (version 2.14)** at: https://gradle.org/
- **TCB and TypeCobolBuilder** are generated using a Visual Studio 2015 (at least Community Edition) solution.

