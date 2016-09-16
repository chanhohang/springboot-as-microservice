#!/bin/sh

if [ "$1" == "" ]
then
	export FILE_PATH="../data/role.csv"
else
	export FILE_PATH=$1
fi

echo FILE_PATH=$FILE_PATH

sh runImporterConsole.sh importRoleJob $FILE_PATH 
