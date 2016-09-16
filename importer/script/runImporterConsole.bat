@echo off
 setLocal EnableDelayedExpansion
 set CLASSPATH=".;../conf
 set CLASSPATH=%CLASSPATH%;../lib/*"
 For /F "tokens=1* delims==" %%A IN (../conf/db.properties) DO (
    set %%A=%%B
    )

 SET HEAP_SIZE=512m

 set JAVA_OPTS=-XX:-HeapDumpOnOutOfMemoryError
 set JAVA_OPTS=!JAVA_OPTS! -Xms%HEAP_SIZE% -Xmx%HEAP_SIZE%

 
 echo java %JAVA_OPTS% -Ddb.url=%db.url% -Ddb.user=%db.user% -Ddb.password=%db.password% -Ddb.driver=%db.driver% -cp %CLASSPATH% net.rc.lab.springboot.importer.console.ImporterConsole %*
 java %JAVA_OPTS% -Ddb.url=%db.url% -Ddb.user=%db.user% -Ddb.password=%db.password% -Ddb.driver=%db.driver% -cp %CLASSPATH% net.rc.lab.springboot.importer.console.ImporterConsole %* 