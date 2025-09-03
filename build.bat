@echo off
echo Building Bajaj Finserv Health Application...

where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven first: https://maven.apache.org/install.html
    pause
    exit /b 1
)

echo Running Maven clean package...
mvn clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo ✅ Build successful!
    echo JAR file location: target\bajaj-finserv-app-1.0.0.jar
    echo.
    echo To run the application:
    echo java -jar target\bajaj-finserv-app-1.0.0.jar
) else (
    echo ❌ Build failed!
    pause
    exit /b 1
)

pause
