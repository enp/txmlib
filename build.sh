#!/bin/sh

JAVA=$JAVA_HOME/bin/java
CP=$JAVA_HOME/lib/tools.jar

for i in `find lib-build/ -name *.jar`
do
    CP=$CP':'$i
done

$JAVA -classpath $CP -Dant.home=lib-build org.apache.tools.ant.Main $@ -buildfile build.xml
