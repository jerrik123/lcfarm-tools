@ECHO OFF

SETLOCAL

@REM Initialize the common environment.

set JAVA_HOME=D:\tools\Java\jdk1.7.0_79
set CLASSPATH=$JAVA_HOME/lib/tools.jar;$JAVA_HOME/jre/lib/rt.jar;$CLASSPATH;../lcfarm-tools.jar;..

echo .
echo CLASSPATH=%CLASSPATH%
echo .
echo PATH=%PATH%
echo .
echo ***************************************************
echo *  Shutdown lcfarm-tools		       *
echo ***************************************************

%JAVA_HOME%\bin\java %JAVA_OPTIONS% com.njq.nongfadai.Application


ENDLOCAL
pause