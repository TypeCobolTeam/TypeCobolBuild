IF exist ".\dist" RMDIR /S /Q .\dist
MKDIR .\dist

IF %1 == maven goto :maven
:gradle
CALL gradle dist
XCOPY /S .\build\libs .\dist
CD .\dist
REN JTCB-all-1.0-SNAPSHOT.jar JTCB-1.0.jar
CD ..
goto :end

:maven
CALL mvn package -DskipTests
XCOPY /S .\target .\dist
CD .\dist
REN jtcb-1.0-SNAPSHOT-jar-with-dependencies.jar JTCB-1.0.jar
CD ..
:end
