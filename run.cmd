@echo off
setlocal

cd /d "%~dp0"

set /p MYSQL_USER=MySQL username [root]: 
if "%MYSQL_USER%"=="" set MYSQL_USER=root
set /p MYSQL_PASSWORD=MySQL password, leave blank if none: 

set SPRING_DATASOURCE_USERNAME=%MYSQL_USER%
set SPRING_DATASOURCE_PASSWORD=%MYSQL_PASSWORD%

echo.
echo Starting Spring Boot application...
echo URL: http://localhost:8080
echo.

call gradlew.bat bootRun

endlocal
