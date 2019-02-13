# TypeCobolBuild

Build engine for [TypeCobol](https://github.com/TypeCobolTeam/TypeCobol) : A Prototype of an incremental Cobol compiler front-end for IBM Enterprise Cobol 5.1 for zOS syntax.
The purpose of the TypeCobolBuild project is to provide a tool for compiling projects using the parser/generator [TypeCobol](https://github.com/TypeCobolTeam/TypeCobol). 


# Architecture overview

## Projects

The solution contains 3 projects
- **JTCB** is the main java project, it uses the [RTC-SDK](https://jazz.net/downloads/rational-team-concert/releases/5.0.2) (Rational Team Concert) to access files located on RTC Streams.
- **TCB** is a DLL (Dynamic Link Library) written using [C++/CLI](https://en.wikipedia.org/wiki/C%2B%2B/CLI) programming language which implements the interoperability between the Java SDK and the .Net Framewok.
- **TypeCobolBuilder** is the Build Engine implementation directly in relation with [TypeCobol](https://github.com/TypeCobolTeam/TypeCobol) infrastructure and written using [C#](https://fr.wikipedia.org/wiki/C_sharp) programming language.

## Projects Dependencies
- **JTCB** depends on:
	- [**JDK 1.7**](http://www.oracle.com/technetwork/java/javase/overview/index.html) or later.
	- [**Rational Team Concert SDK 5.0.2**](https://jazz.net/downloads/rational-team-concert/releases/5.0.2?p=allDownloads)
	- [**RTC-Client-plainJavaLib-5.0.2**](https://jazz.net/downloads/rational-team-concert/releases/5.0.2?p=allDownloads)
	- [**args4j**](http://args4j.kohsuke.org/) and maven repository https://mvnrepository.com/artifact/args4j/args4j
	- [**hamcrest-core**](http://hamcrest.org/JavaHamcrest/) and maven repository https://mvnrepository.com/artifact/org.hamcrest/hamcrest-core
	- [**junit**](http://junit.org/junit4/) and maven repository https://mvnrepository.com/artifact/junit/junit
	- [**TCB**](https://github.com/TypeCobolTeam/TypeCobolBuild)

- **TCB** depends on:
	- [**JDK 1.7 JNI**](http://www.oracle.com/technetwork/java/javase/overview/index.html) or later.
	- [**.Net Framework 4.5**](https://msdn.microsoft.com/fr-fr/library/5a4x27ek%28v=vs.110%29.aspx)
	- [**TypeCobolBuilder**](https://github.com/TypeCobolTeam/TypeCobolBuild)

- **TypeCobolBuilder** depends on:
	- [**TCB**](https://github.com/TypeCobolTeam/TypeCobolBuild)
	- [**.Net Framework 4.5**](https://msdn.microsoft.com/fr-fr/library/5a4x27ek%28v=vs.110%29.aspx)
	- [**TypeCobol**](https://github.com/TypeCobolTeam/TypeCobol)
	- [**lo4gnet**](https://logging.apache.org/log4net/) with nuget at https://www.nuget.org/packages/log4net/
	- [**CommandLine**](https://github.com/gsscoder/commandline) with nuget at https://www.nuget.org/packages/CommandLineParser/
	- [**Castle.Core**](http://www.castleproject.org/) with nuget at https://www.nuget.org/packages/Castle.Core/
	- [**Antr4.Runtime**](https://github.com/sharwell/antlr4cs) with nuget at https://www.nuget.org/packages/Antlr4.Runtime
	- [**SimpleMsgPack**](https://github.com/ymofen/SimpleMsgPack.Net) with nuget at https://www.nuget.org/packages/SimpleMsgPack/1.0.0/ReportAbuse
	- [**Jet.Brains.Annotations**](https://www.jetbrains.com/help/resharper/10.0/Code_Analysis__Code_Annotations.html) with nuget at https://www.nuget.org/packages/JetBrains.Annotations/10.1.5/ReportAbuse
	
### Modules data flow diagram
![Alt text](https://g.gravizo.com/g?%20%20digraph%20G%20{%0A%20%20%20%20aize%20=%224,4%22%3B%0A%20%20%20%20//labelloc=%22t%22%3B%0A%20%20%20%20//label=%22Modules%20data%20flow%20diagram%22%3B%0A%20%20%20%20RTC_SDK%20[shape=doubleoctagon,label=%22RTC%20SDK%20.jars%22]%3B%0A%20%20%20%20JTCB%20[shape=box,label=%22JTCB.jar%22]%3B%0A%20%20%20%20TCB%20[shape=box,label=%22TCB.dll%22]%3B%0A%20%20%20%20TypeCobolBuilder%20[shape=box,label=%22TypeCobolBuilder.dll%22]%3B%0A%20%20%20%20TypeCobol%20[shape=box,label=%22TypeCobol.exe%22]%3B%0A	JTCB%20-%3E%20RTC_SDK%3B%0A	RTC_SDK%20-%3E%20JTCB%3B%0A%20%20%20%20JavaJni%20-%3E%20JTCB%0A%20%20%20%20JavaJni%20-%3E%20TCB%3B%0A%20%20%20%20TCB%20-%3E%20TypeCobolBuilder%3B%0A	TypeCobolBuilder%20-%3E%20TCB%3B%0A%20%20%20%20TypeCobolBuilder%20-%3E%20TypeCobol%3B%0A	TypeCobol%20-%3E%20TypeCobolBuilder%3B%0A%20%20%20%20JavaJni%20[label=%22Java/JNI%22,style=filled,color=%22.4%20.3%202.1%22]%3B%0A%20%20}%0A)

## Generating the solution

The solution is generated using various configuration [(see Wiki Home Page)](https://github.com/TypeCobolTeam/TypeCobolBuild/wiki)
- **JTCB** can be generated using:
	- [**Maven**](https://maven.apache.org/)
	- [**Gradle**](https://gradle.org/)
- **TCB and TypeCobolBuilder** are generated using a [Visual Studio 2015](https://www.visualstudio.com/fr-fr/products/visual-studio-community-vs.aspx) (at least Community Edition) solution.

