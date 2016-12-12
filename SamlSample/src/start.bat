@echo off
echo  This should be executed in the same folder with SamlSample-jar-with-dependencies.jar and webapp folder

@echo Current Folder %cd%
@ECHO Setting up classpath
FOR %%F IN (%cd%\lib\*.jar) DO call :addcp %%F
goto extlibe
:addcp
SET CLASSPATH=%CLASSPATH%;%1
goto :eof
:extlibe
SET CLASSPATH 
@ECHO Starting application
java -classpath %CLASSPATH%;SamlSample.jar  sample.MyServer