# MCP Server

This repository contains the MCP (Model Context Protocol) server application.

## Overview

A Spring Boot-based MCP server. Build and run using the included Maven wrapper or your system Maven.

## Prerequisites

- Java 11+ (JDK)
- Node.js (for inspector tooling)

## Build and run

Windows (using wrapper):

    .\mvnw.cmd spring-boot:run

Build jar and run:

    .\mvnw.cmd package
    java -jar target\*.jar

(Or use `mvn` if you prefer.)

## Configuration

Application configuration can be found at `src/main/resources/application.yaml`.

## Inspecting the MCP Server (MCP Inspector)

Open a terminal and run:

    npx -y @modelcontextprotocol/inspector@latest

This will load the MCP Inspector at: http://localhost:6274/

To connect and inspect the MCP server:

1. Go to Add Server in the inspector UI.
2. Set the URL to: `http://localhost:8080/mcp`
3. Set Transport to: `streamable http`
4. Set Server ID to any unique name.

Once connected you can view tools, prompts, and resources exposed by the MCP server.

## Notes

- Adjust host/port if your server runs on a different port.
- If using a packaged JAR, ensure it serves the MCP endpoint at `/mcp` as above.

