# TypeCobolBuild

Build engine for [TypeCobol](https://github.com/TypeCobolTeam/TypeCobol) : A Prototype of an incremental Cobol compiler front-end for IBM Enterprise Cobol 5.1 for zOS syntax.
The purpose of the TypeCobolBuild project is to provide a tool for compiling project using the TypeCobol parser/generator. 

# Architecture overview

## Projects

The solution contains 3 projects
- **JTCB** is the main java project, it uses the [RTC-SDK](https://jazz.net/downloads/rational-team-concert/releases/5.0.2) (Rational Team Concert) to access files located on RTC Streams.
- **TCB** is DLL (Dynamic Link Library) written using [C++/CLI](https://en.wikipedia.org/wiki/C%2B%2B/CLI) programming language which implements the interoperability between the Java SDK and the .Net Framewok.
- **TypeCobolBuilder** is the Build Engine implementation directly in relation with [TypeCobol](https://github.com/TypeCobolTeam/TypeCobol) infrastructure and written using [C#](https://fr.wikipedia.org/wiki/C_sharp) programming language.

## Projects Dependencies
- **JTCB** depends on:
	- [**JDK 1.7**](http://www.oracle.com/technetwork/java/javase/overview/index.html) or later.
	- [**RTC-Client-plainJavaLib-5.0.2**](https://jazz.net/downloads/rational-team-concert/releases/5.0.2?p=allDownloads)
	- [**args4j**](http://args4j.kohsuke.org/) and maven repository https://mvnrepository.com/artifact/args4j/args4j
	- [**hamcrest-core**](http://hamcrest.org/JavaHamcrest/) and maven repository https://mvnrepository.com/artifact/org.hamcrest/hamcrest-core
	- [**junit**](http://junit.org/junit4/) and maven repository https://mvnrepository.com/artifact/junit/junit
	- [**TCB**](https://github.com/TypeCobolTeam/TypeCobolBuild)

- **TCB** depends on:
	- [**JDK 1.7 JNI**](http://www.oracle.com/technetwork/java/javase/overview/index.html) or later.
	- [**.Net Framework 4.5**](https://msdn.microsoft.com/fr-fr/library/5a4x27ek%28v=vs.110%29.aspx)
	- [**lo4gnet**](https://logging.apache.org/log4net/) with nuget at https://www.nuget.org/packages/log4net/
	- [**TypeCobolBuilder**](https://github.com/TypeCobolTeam/TypeCobolBuild)

- **TypeCobolBuilder** depends on:
	- [**TCB**](https://github.com/TypeCobolTeam/TypeCobolBuild)
	- [**.Net Framework 4.5**](https://msdn.microsoft.com/fr-fr/library/5a4x27ek%28v=vs.110%29.aspx)
	- [**TypeCobol**](https://github.com/TypeCobolTeam/TypeCobol)
	- [**lo4gnet**](https://logging.apache.org/log4net/) with nuget at https://www.nuget.org/packages/log4net/
	- [**CommandLine**](https://github.com/gsscoder/commandline) with nuget at https://www.nuget.org/packages/CommandLineParser/
	- [**Castle.Core**](http://www.castleproject.org/) with nuget at https://www.nuget.org/packages/Castle.Core/
	- [**Antr4.Runtime**](https://github.com/sharwell/antlr4cs) with nuget at https://www.nuget.org/packages/Antlr4.Runtime
	
### Modules data flow diagram
![Alt text](http://g.gravizo.com/g?
  digraph G {
    aize ="4,4";
    labelloc="t";
    label="Modules data flow diagram";
    RTC_SDK [shape=doubleoctagon,label="RTC SDK .jars"];
    JTCB [shape=box,label="JTCB.jar"];
    TCB [shape=box,label="TCB.dll"];
    TypeCobolBuilder [shape=box,label="TypeCobolBuilder.dll"];
    TypeCobol [shape=box,label="TypeCobol.exe"];
	JTCB -> RTC_SDK;
	RTC_SDK -> JTCB;
    JavaJni -> JTCB
    JavaJni -> TCB;
    TCB -> TypeCobolBuilder;
	TypeCobolBuilder -> TCB;
    TypeCobolBuilder -> TypeCobol;
	TypeCobol -> TypeCobolBuilder;
    JavaJni [label="Java/JNI",style=filled,color=".4 .3 2.1"];
  }
)

## Generating the solution

The solution is generated using various configuration
- **JTCB** can be generated using:
	- [**Maven (version 3.3.9)**](https://maven.apache.org/)
	- [**Gradle (version 2.14)**](https://gradle.org/)
- **TCB and TypeCobolBuilder** are generated using a [Visual Studio 2015](https://www.visualstudio.com/fr-fr/products/visual-studio-community-vs.aspx) (at least Community Edition) solution.

