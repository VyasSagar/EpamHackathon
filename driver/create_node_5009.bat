@echo off
java -jar  -Dwebdriver.chrome.driver=%cd%\chromedriver.exe  -Dwebdriver.ie.driver=%cd%\IEDriverServer.exe "%cd%\selenium-server-standalone-3.14.0.jar" -hub http://127.0.0.1:4444/grid/register  -role node -port 5009
pause