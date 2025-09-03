#!/bin/bash
echo "Building Bajaj Finserv Health Application..."

# Ensure Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven first."
    echo "Visit: https://maven.apache.org/install.html"
    exit 1
fi

# Clean and package
echo "Running Maven clean package..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "JAR file location: target/bajaj-finserv-app-1.0.0.jar"
    echo ""
    echo "To run the application:"
    echo "java -jar target/bajaj-finserv-app-1.0.0.jar"
else
    echo "❌ Build failed!"
    exit 1
fi
