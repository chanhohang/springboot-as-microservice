@echo off
if "%1"=="" (
  set FILE_PATH="../data/user.csv"
) else (
  set FILE_PATH="%1"
)
echo Use file path %FILE_PATH%
runImporterConsole.bat importUserJob %FILE_PATH% 