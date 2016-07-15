# TypeCobolBuild

Build engine for TypeCobol : A Prototype of an incremental Cobol compiler front-end for IBM Enterprise Cobol 5.1 for zOS syntax.

# Architecture overview

## Projects

The solution contains 3 projects
- **JTCB** is the main java project, it uses the RTC-SDK to access files located on RTC Streams.
- **TCB** is DLL (Dynamic Link Library) which implements the interoperability between the Java SDK and the .Net Framewok.
- **TypeCobolBuilder** is the Build Engine implementation directly in relation with TypeCobol infrastructure.

## Generating the solution

The solution is generated using various configuration
- **JTCB** can be generated using Maven or Gradle.
- **TCB** is genereted using a Visual Studio solution.
- **TypeCobolBuilder** is generated using a Visual Studio solution.
