@echo off
echo  This can not be executed in src folder. Please execute it in target folder

set curPath=%~dp0
@echo Current Folder %cd%
@ECHO Setting up classpath
SET CLASSPATH=.;lib/*
goto extlibe

goto :eof
:extlibe
SET CLASSPATH 
@ECHO Starting application
IF "%JAVA_HOME%" == "" (
    echo Please install JDK.
) ELSE (
   "%JAVA_HOME%\BIN\java.exe" -classpath "%CLASSPATH%;Sample_WebApp.jar" com.vmware.eucenablement.saml.sample.MyWebServer
)