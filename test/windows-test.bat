@ECHO OFF
ECHO export manually in eclipse to ..\dist\TheSignal-QuickStartTutorial.jar
pause
start cmd /C java -jar ..\dist\TheSignal-QuickStartTutorial.jar 1 testkey testdata
SLEEP 5
start cmd /C java -jar ..\dist\TheSignal-QuickStartTutorial.jar 2 testkey