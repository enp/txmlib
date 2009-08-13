@if "%1"=="setup" goto setup
@set CP=.
@for /f "delims==" %%i in ('dir lib-build /b /s *.jar') do @call %0 setup %%i
@set CP=%JAVA_HOME%\lib\tools.jar;conf;%CP%
@java -classpath "%CP%" -Dant.home=lib-build org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 -buildfile build.xml
@goto end
:setup
@set CP=%2;%CP%
:end