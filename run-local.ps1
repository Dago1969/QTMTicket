#!/usr/bin/env pwsh
# Script per avviare l'app localmente puntando al DB creato con docker-compose
$env:DB_HOST = $env:DB_HOST -or 'localhost'
$env:DB_PORT = $env:DB_PORT -or '3307'
$env:DB_NAME = $env:DB_NAME -or 'qtmticket'
$env:DB_USERNAME = $env:DB_USERNAME -or 'qtmuser'
$env:DB_PASSWORD = $env:DB_PASSWORD -or 'qtm_pass'

Write-Host "Avvio qtm-ticket con le seguenti variabili:"
Write-Host "DB_HOST=$env:DB_HOST DB_PORT=$env:DB_PORT DB_NAME=$env:DB_NAME DB_USERNAME=$env:DB_USERNAME"

if (-not (Test-Path -Path 'target\qtm-ticket-1.0.0.jar')) {
    Write-Error "Jar non trovato in target/. Esegui 'mvn -DskipTests=true package' prima di avviare questo script."
    exit 1
}

java -jar target\qtm-ticket-1.0.0.jar
