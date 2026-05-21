@echo off
setlocal

cd /d "%~dp0"

echo This script creates the project MySQL user and grants database permissions.
echo You need a MySQL admin account, usually root.
echo.

set /p ADMIN_USER=MySQL admin username [root]: 
if "%ADMIN_USER%"=="" set ADMIN_USER=root

set /p APP_USER=Project MySQL username [project_java_application]: 
if "%APP_USER%"=="" set APP_USER=project_java_application

set /p APP_PASSWORD=Project MySQL password: 
if "%APP_PASSWORD%"=="" (
    echo Password cannot be empty.
    exit /b 1
)

set TEMP_SQL=%TEMP%\create_project_mysql_user_%RANDOM%.sql

(
    echo CREATE DATABASE IF NOT EXISTS smart_academic_lab CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    echo CREATE USER IF NOT EXISTS '%APP_USER%'@'localhost' IDENTIFIED BY '%APP_PASSWORD%';
    echo ALTER USER '%APP_USER%'@'localhost' IDENTIFIED BY '%APP_PASSWORD%';
    echo GRANT ALL PRIVILEGES ON smart_academic_lab.* TO '%APP_USER%'@'localhost';
    echo FLUSH PRIVILEGES;
) > "%TEMP_SQL%"

echo.
echo Enter the MySQL admin password when prompted.
mysql -u %ADMIN_USER% -p < "%TEMP_SQL%"
set MYSQL_RESULT=%ERRORLEVEL%

del "%TEMP_SQL%" > nul 2>&1

if not "%MYSQL_RESULT%"=="0" (
    echo Failed to create MySQL user. Check the admin username/password.
    exit /b %MYSQL_RESULT%
)

echo.
echo MySQL user is ready.
echo Next run:
echo setup-db.cmd
echo run.cmd

endlocal
