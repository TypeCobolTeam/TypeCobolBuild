IF exist ".\dist" RMDIR /S /Q .\dist
MKDIR .\dist
MKDIR .\dist\lib
XCOPY /S .\rtc_libs .\dist\lib

IF %1 == maven goto :maven
:gradle
CALL gradle dist
XCOPY /S .\build\libs .\dist
CD .\dist
REN JTCB-all-1.0-SNAPSHOT.jar JTCB.jar
CD ..
goto :end

:maven
CALL mvn package -DskipTests
XCOPY /S .\target .\dist
CD .\dist
REN jtcb-1.0-SNAPSHOT-jar-with-dependencies.jar JTCB.jar
CD ..
:end
