////@echo off
setlocal

cd /d "%~dp0"

echo This will create/reset the smart_academic_lab database.
echo Existing tables in this database may be dropped by schema.sql.
echo.
set /p MYSQL_USER=MySQL username [root]: 
if "%MYSQL_USER%"=="" set MYSQL_USER=root
set /p MYSQL_PASSWORD=MySQL password, leave blank if none: 
echo.
set /p CONFIRM=Type YES to continue: 

if /I not "%CONFIRM%"=="YES" (
    echo Cancelled.
    exit /b 1
)

set MYSQL_AUTH=-u %MYSQL_USER%
if not "%MYSQL_PASSWORD%"=="" set MYSQL_AUTH=-u %MYSQL_USER% -p%MYSQL_PASSWORD%

echo.
echo Running schema.sql...
mysql %MYSQL_AUTH% < src\main\resources\db\schema.sql
if errorlevel 1 (
    echo Failed to run schema.sql.
    echo Check your MySQL username/password, then run setup-db.cmd again.
    exit /b 1
)

echo.
echo Running seed.sql...
mysql %MYSQL_AUTH% < src\main\resources\db\seed.sql
if errorlevel 1 (
    echo Failed to run seed.sql.
    echo Check your MySQL username/password, then run setup-db.cmd again.
    exit /b 1
)

echo.
echo Database setup completed.

endlocal
