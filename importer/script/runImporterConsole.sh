#!/bin/bash

while read line; do    
   IFS='=' read left right <<< "$line"
   if [ "$left" == "" ]
   then
       echo "Invalid Property $line"
   else
       left=${left//[\.]/_}
       echo "$left=$right"
       export $left=$right
   fi
done < ../conf/db.properties

export HEAP_SIZE=512m
JAVA_OPTS=-XX:-HeapDumpOnOutOfMemoryError
JAVA_OPTS="$JAVA_OPTS -Xms$HEAP_SIZE -Xmx$HEAP_SIZE"

CLASSPATH=$CLASSPATH:../conf:../lib/*

#echo java $JAVA_OPTS -Ddb.url=$db_url -Ddb.user=$db_user -Ddb.password=$db_password -Ddb.driver=$db_driver -cp $CLASSPATH com.trade.importer.console.ImporterConsole "$@"
java $JAVA_OPTS -Ddb.url=$db_url -Ddb.user=$db_user -Ddb.password=$db_password -Ddb.driver=$db_driver -cp $CLASSPATH com.trade.importer.console.ImporterConsole "$@"
