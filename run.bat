@echo off
echo  prepare to build this package

set curPath=%~dp0

IF "%M2_HOME%" == "" (
    echo Please install maven and set M2_HOME path.
) ELSE (
   echo excute maven commands ...
   mvn clean package
   cd "%curPath%\SamlSample\target\"
   call "start.bat"
)
