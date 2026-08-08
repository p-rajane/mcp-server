# Building an MCP Server with Spring Boot and Spring AI: A Practical Implementation Guide

Model Context Protocol (MCP) is becoming an important integration pattern for AI applications.

If you are familiar with REST APIs, you may initially wonder:

> **Why do we need MCP when we can already expose data through REST APIs?**

The answer becomes clearer when we look at MCP from an AI integration perspective.

REST APIs expose application capabilities to software applications. MCP provides a standardized way for AI applications and LLM-powered agents to discover and invoke capabilities such as tools and resources.

In this article, we will build and understand a simple **MCP Server using Spring Boot and Spring AI**.

The example project exposes a tool called:

```text
getAboutTendulkar()
```

The tool reads information from a Markdown file packaged inside the application and makes that information available through MCP.

The objective of this project is not to build a complex business application, but to understand the fundamental pieces involved in creating a Spring AI MCP Server.

---

# What We Are Building

The application has a very simple architecture:

```text
                     ┌─────────────────────┐
                     │    MCP Client /     │
                     │    AI Application   │
                     └──────────┬──────────┘
                                │
                         MCP / Streamable HTTP
                                │
                                ▼
                     ┌─────────────────────┐
                     │    Spring Boot     │
                     │     MCP Server     │
                     │                     │
                     │  getAboutTendulkar │
                     │        ()           │
                     └──────────┬──────────┘
                                │
                                ▼
                     ┌─────────────────────┐
                     │ sachin tendulkar.md│
                     │                     │
                     │  Knowledge/Data     │
                     └─────────────────────┘
```

The flow is:

```text
MCP Client
     ↓
MCP Server
     ↓
getAboutTendulkar()
     ↓
Read Markdown file
     ↓
Return content
```

This is a simple example, but the same architecture can later be extended to retrieve information from:

* REST APIs
* Databases
* External services
* Enterprise applications
* Vector databases
* Internal documents
* Business systems

---

# Project Structure

The important project files are organized as follows:

```text
mcp-server
│
├── pom.xml
│
├── README.md
│
└── src
    ├── main
    │   ├── java
    │   │   └── com.demo.mcp_server
    │   │       │
    │   │       ├── McpServerApplication.java
    │   │       │
    │   │       └── tool
    │   │           ├── Config.java
    │   │           └── MCPTool.java
    │   │
    │   └── resources
    │       ├── application.yaml
    │       └── sachin tendulkar.md
    │
    └── test
        └── java
            └── com.demo.mcp_server
                └── McpServerApplicationTests.java
```

Let's understand each important file.

---

# 1. pom.xml — Project Dependencies

The first important file is `pom.xml`.

The project uses:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.7</version>
</parent>
```

The project is therefore based on **Spring Boot 4.0.7**.

The Java version is configured as:

```xml
<properties>
    <java.version>17</java.version>
    <spring-ai.version>2.0.0</spring-ai.version>
</properties>
```

So the project uses:

```text
Java       → 17
Spring Boot → 4.0.7
Spring AI   → 2.0.0
```

---

# Spring WebMVC Dependency

The project includes:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

This provides the Spring MVC infrastructure required by the application.

---

# Spring AI MCP Server Dependency

The most important dependency for this project is:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

This starter integrates Spring AI with MCP Server functionality over Spring MVC.

This is what allows the application to behave as an MCP server rather than just a conventional Spring Boot web application.

---

# Spring AI BOM

The project also imports the Spring AI BOM:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>${spring-ai.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

The BOM helps manage compatible versions of Spring AI dependencies.

Instead of manually specifying versions for every Spring AI dependency, the project centralizes the version through:

```xml
<spring-ai.version>2.0.0</spring-ai.version>
```

This reduces the possibility of accidentally mixing incompatible Spring AI versions.

---

# 2. McpServerApplication.java — Spring Boot Entry Point

The main application class is very small:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

}
```

This is the standard Spring Boot bootstrap class.

The important annotation is:

```java
@SpringBootApplication
```

It enables the major Spring Boot features required to start the application.

Conceptually:

```text
main()
  ↓
SpringApplication.run()
  ↓
Spring Boot starts
  ↓
ApplicationContext created
  ↓
Components discovered
  ↓
MCP infrastructure initialized
  ↓
Server starts
```

There is no MCP-specific code in the main class.

That is one of the benefits of using Spring AI: much of the MCP infrastructure is provided by the framework.

---

# 3. MCPTool.java — The Most Important Class

The actual MCP functionality is implemented in:

```text
src/main/java/com/demo/mcp_server/tool/MCPTool.java
```

The class starts with:

```java
@Component
public class MCPTool {
```

The `@Component` annotation tells Spring to create this class as a Spring-managed bean.

Therefore, Spring discovers `MCPTool` during component scanning.

---

# Reading a Resource from the Classpath

The project contains:

```text
src/main/resources/sachin tendulkar.md
```

The class injects that resource using:

```java
@Value("classpath:sachin tendulkar.md")
private Resource resource;
```

This is an important Spring feature.

Instead of hard-coding a physical filesystem path, the application accesses the resource from the application's classpath.

The conceptual structure is:

```text
src/main/resources
        │
        └── sachin tendulkar.md
                 │
                 ▼
             Classpath
                 │
                 ▼
        Spring Resource
                 │
                 ▼
          MCPTool.resource
```

This also means the resource can be packaged into the application's JAR.

---

# Defining an MCP Tool

The most important method in the application is:

```java
@McpTool(
    name = "getAboutTendulkar",
    description = "This tool will return information about Sachin Tendulkar."
)
public String getAboutTendulkar() {
    try {
        return readFile();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```

The key annotation is:

```java
@McpTool
```

This tells Spring AI that the method should be exposed as an MCP tool.

The tool has two important pieces of metadata.

### Tool name

```java
name = "getAboutTendulkar"
```

This becomes the name by which the MCP client can identify and invoke the capability.

### Tool description

```java
description = "This tool will return information about Sachin Tendulkar."
```

The description is important in AI-powered applications because an LLM can use tool metadata to understand what a tool is capable of doing.

Conceptually:

```text
MCP Server
     │
     └── Tool
          │
          ├── Name:
          │   getAboutTendulkar
          │
          └── Description:
              Returns information
              about Sachin Tendulkar
```

The tool is therefore more than just a Java method.

It becomes an AI-consumable capability.

---

# What Happens When the Tool Is Called?

The MCP client can request the tool:

```text
getAboutTendulkar()
```

The MCP server routes the request to:

```java
public String getAboutTendulkar()
```

The method then invokes:

```java
return readFile();
```

The file contents are returned to the MCP caller.

The complete flow is:

```text
MCP Client
     │
     │ getAboutTendulkar()
     ▼
MCP Server
     │
     ▼
MCPTool.getAboutTendulkar()
     │
     ▼
readFile()
     │
     ▼
sachin tendulkar.md
     │
     ▼
String response
     │
     ▼
MCP Client
```

---

# 4. readFile() — Loading the Knowledge

The actual file-reading implementation is:

```java
public String readFile() throws Exception {
    try (InputStream inputStream = resource.getInputStream()) {
        return new String(
            inputStream.readAllBytes(),
            StandardCharsets.UTF_8
        );
    }
}
```

Let's break this down.

First:

```java
resource.getInputStream()
```

opens an input stream for the classpath resource.

Then:

```java
inputStream.readAllBytes()
```

reads the complete file into a byte array.

Finally:

```java
new String(..., StandardCharsets.UTF_8)
```

converts the bytes into a Java `String`.

The complete flow is:

```text
Markdown file
     ↓
InputStream
     ↓
readAllBytes()
     ↓
byte[]
     ↓
UTF-8 decoding
     ↓
String
```

The try-with-resources block:

```java
try (InputStream inputStream = ...) {
```

automatically closes the stream when the operation finishes.

---

# The Knowledge Source: sachin tendulkar.md

The project contains:

```text
src/main/resources/sachin tendulkar.md
```

This file contains information about Sachin Tendulkar, including personal information, international career information, career statistics, achievements, and biographical information.

The important architectural point is that the information is **externalized from the Java code**.

Instead of doing this:

```java
return "Sachin Tendulkar was born...";
```

the application stores the information in a resource:

```text
sachin tendulkar.md
```

and dynamically reads it.

That makes the example easier to extend.

For example, you could later have:

```text
resources/
│
├── sachin tendulkar.md
├── virat kohli.md
├── ms dhoni.md
└── rohit sharma.md
```

and expose different tools for different knowledge sources.

---

# 5. application.yaml — MCP Server Configuration

The MCP server configuration is located in:

```text
src/main/resources/application.yaml
```

The project defines:

```yaml
spring:
  application:
    name: mcp-server
  ai:
    mcp:
      server:
        name: mcp-server
        version: 1.0.0
        protocol: STREAMABLE
        stdio: false
        type: SYNC
```

Let's understand the important properties.

---

# Application Name

```yaml
spring:
  application:
    name: mcp-server
```

This gives the Spring Boot application its name:

```text
mcp-server
```

---

# MCP Server Name

```yaml
server:
  name: mcp-server
```

This identifies the MCP server.

The server advertises itself using this name when MCP clients connect.

---

# MCP Server Version

```yaml
version: 1.0.0
```

This specifies the server version.

Version information is useful when multiple versions of an MCP server exist or when clients need to identify the server implementation.

---

# Streamable Protocol

The project configures:

```yaml
protocol: STREAMABLE
```

The project therefore uses the streamable MCP transport configuration.

This is important because MCP communication needs a transport mechanism through which the client and server exchange messages.

The README in this project also instructs the MCP Inspector to connect using:

```text
streamable http
```

---

# stdio Configuration

The application has:

```yaml
stdio: false
```

This means the server is not configured to use standard input/output as its transport.

Instead, this project is designed to be accessed over HTTP.

That aligns with the README instructions to connect to:

```text
http://localhost:8080/mcp
```

---

# Synchronous Tool Execution

The project uses:

```yaml
type: SYNC
```

This configures the MCP server for synchronous operation.

For this simple example, the tool reads a local Markdown file and returns its contents, so synchronous execution is a natural fit.

---

# 6. Config.java — An Alternative Tool Registration Approach

The project also contains:

```text
tool/Config.java
```

The class is:

```java
@Configuration
public class Config {
```

However, the tool registration code inside it is currently commented out:

```java
/*
@Bean
public List<ToolCallback> toolCallbacks(MCPTool mcpTool) {
    return List.of(ToolCallbacks.from(mcpTool));
}
*/
```

This is interesting because it shows another approach to registering tool callbacks.

The code uses:

```java
ToolCallbacks.from(mcpTool)
```

to create tool callbacks from the `MCPTool` object.

However, this project currently uses the annotation-based approach:

```java
@McpTool
```

instead.

So conceptually, the project contains two approaches:

```text
Approach 1 — Current
--------------------
@McpTool
   ↓
Spring AI MCP infrastructure


Approach 2 — Commented alternative
-----------------------------------
ToolCallbacks.from(mcpTool)
   ↓
ToolCallback
   ↓
MCP infrastructure
```

For understanding this project, the important implementation is the annotation-based `@McpTool`.

---

# 7. The Unit Test

The project contains:

```text
src/test/java/com/demo/mcp_server/McpServerApplicationTests.java
```

The test is:

```java
@SpringBootTest
class McpServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
```

This is a basic Spring Boot context-loading test.

The purpose is essentially to verify that the application context can start successfully.

Conceptually:

```text
Test starts
    ↓
Spring Boot context created
    ↓
Beans discovered
    ↓
Configuration loaded
    ↓
Context starts successfully
    ↓
Test passes
```

The current test does not directly invoke:

```text
getAboutTendulkar()
```

So it verifies application startup rather than specifically testing the MCP tool's returned content.

For a production application, additional tests could be introduced for the actual tool behavior.

---

# 8. Running the Application

The repository includes Maven wrapper scripts:

```text
mvnw
mvnw.cmd
```

On Windows, the README provides:

```bash
.\mvnw.cmd spring-boot:run
```

Alternatively, the project can be packaged:

```bash
.\mvnw.cmd package
```

and then executed using:

```bash
java -jar target\*.jar
```

Using the Maven wrapper is convenient because it avoids requiring the developer to have a particular Maven installation configured globally.

---

# 9. Connecting MCP Inspector

One of the most useful parts of this project is the ability to test the MCP server using **MCP Inspector**.

The README specifies:

```bash
npx -y @modelcontextprotocol/inspector@latest
```

This starts the MCP Inspector.

The Inspector UI is available at:

```text
http://localhost:6274/
```

The project README instructs you to configure the MCP server with:

```text
URL:
http://localhost:8080/mcp

Transport:
streamable http
```

This gives us a very useful testing architecture:

```text
                 Browser
                    │
                    ▼
          ┌──────────────────┐
          │  MCP Inspector   │
          │ localhost:6274   │
          └────────┬─────────┘
                   │
             Streamable HTTP
                   │
                   ▼
          ┌──────────────────┐
          │  Spring Boot     │
          │   MCP Server     │
          │ localhost:8080   │
          └────────┬─────────┘
                   │
                   ▼
          getAboutTendulkar()
                   │
                   ▼
       sachin tendulkar.md
```

---

# 10. What Happens During MCP Tool Discovery?

One of the most important concepts to understand is that an MCP client doesn't necessarily need to know the Java implementation.

The client interacts with the MCP server.

The server exposes the tool:

```text
getAboutTendulkar
```

along with its description.

Conceptually:

```text
MCP Client
     │
     │ "What tools do you provide?"
     ▼
MCP Server
     │
     │
     ├── getAboutTendulkar
     │     └── Returns information about Sachin Tendulkar
     │
     └── Other capabilities
```

This is fundamentally different from simply hard-coding a REST endpoint into an application.

The AI client can use the tool metadata to understand what capability is available.

---

# 11. Where Does the LLM Fit?

This project itself is an **MCP Server**.

It does not contain an LLM.

That distinction is important.

The architecture can be extended as follows:

```text
                     ┌───────────────┐
                     │     User      │
                     └───────┬───────┘
                             │
                             ▼
                     ┌───────────────┐
                     │      LLM      │
                     │               │
                     │ Understands    │
                     │ user intent    │
                     └───────┬───────┘
                             │
                      Tool selection
                             │
                             ▼
                     ┌───────────────┐
                     │  MCP Client   │
                     └───────┬───────┘
                             │
                            MCP
                             │
                             ▼
              ┌──────────────────────────┐
              │     This Project         │
              │    Spring Boot MCP       │
              │        Server            │
              └────────────┬─────────────┘
                           │
                           ▼
                 getAboutTendulkar()
                           │
                           ▼
                  sachin tendulkar.md
```

The responsibilities are therefore separated:

```text
LLM
 ↓
Understands intent
 ↓
Chooses appropriate tool

MCP Client
 ↓
Communicates with MCP Server

MCP Server
 ↓
Exposes capabilities

Tool
 ↓
Performs actual operation

Data source
 ↓
Provides information
```

---

# 12. Why Use MCP Instead of Direct REST?

Suppose the same information were exposed through a REST API:

```http
GET /tendulkar
```

A traditional application could call:

```text
REST Client
    ↓
GET /tendulkar
    ↓
Spring Boot
    ↓
Markdown / Database
```

That's perfectly valid.

But an AI agent needs more than just an endpoint.

It needs to understand:

```text
What capabilities are available?
What does each capability do?
What parameters are required?
Which capability is appropriate for the user's request?
```

MCP standardizes the interaction between AI clients and such capabilities.

Therefore:

```text
REST
 ↓
Application integration


MCP
 ↓
AI capability integration
```

MCP doesn't make REST obsolete.

In fact, an MCP server can itself call REST APIs.

A more realistic enterprise architecture could be:

```text
                 LLM
                  │
                  ▼
              MCP Client
                  │
                  ▼
              MCP Server
                  │
       ┌──────────┼──────────┐
       │          │          │
      REST       REST       DB
       │          │          │
   Customer     Order     Enterprise
    Service    Service     System
```

The MCP server becomes an AI-oriented facade over existing enterprise systems.

---

# 13. How This Example Can Be Extended

The current application is deliberately simple:

```text
MCP Tool
    ↓
Markdown file
    ↓
String response
```

But the same pattern can be extended to real-world systems.

For example:

## REST API

```java
@McpTool(
    name = "getCustomer",
    description = "Retrieve customer information"
)
public Customer getCustomer(String customerId) {
    return restClient.get()
        .uri("/customers/" + customerId)
        .retrieve()
        .body(Customer.class);
}
```

The MCP tool becomes an AI-friendly wrapper over an existing REST API.

---

## Database

A tool could retrieve information from a database:

```text
LLM
 ↓
MCP Client
 ↓
MCP Server
 ↓
getCustomer()
 ↓
Repository
 ↓
PostgreSQL
```

---

## Enterprise Workflow

The same approach can expose business operations:

```text
createTicket()
refundPayment()
searchOrders()
checkPaymentStatus()
getCustomer()
```

The AI agent can then choose among these capabilities depending on the user's request.

---

# 14. From Static File to Enterprise AI Architecture

The current example:

```text
                 MCP Client
                     │
                     ▼
                 MCP Server
                     │
                     ▼
           getAboutTendulkar()
                     │
                     ▼
            Markdown Resource
```

can evolve into:

```text
                         AI Application
                              │
                              ▼
                         MCP Client
                              │
                              ▼
                         MCP Server
                              │
             ┌────────────────┼────────────────┐
             │                │                │
             ▼                ▼                ▼
        Customer Tool    Order Tool       Payment Tool
             │                │                │
             ▼                ▼                ▼
        Customer API      Order API       Payment API
             │                │                │
             └────────────────┼────────────────┘
                              ▼
                     Enterprise Systems
```

This is where MCP becomes particularly useful in enterprise AI applications.

---

# 15. Important Implementation Observation

There is a small implementation detail in the current `MCPTool` class worth understanding.

The method contains:

```java
Resource resource =
    new org.springframework.core.io.ClassPathResource("patients.json");
```

but this local variable is not subsequently used.

The actual file returned by `readFile()` is the class-level resource:

```java
@Value("classpath:sachin tendulkar.md")
private Resource resource;
```

Therefore, the effective data source for the current implementation is:

```text
sachin tendulkar.md
```

and not `patients.json`.

If the intention is to use the Sachin Tendulkar Markdown resource, that unused `ClassPathResource("patients.json")` statement can be removed to make the implementation clearer.

This is also a good example of why reviewing the complete execution path matters: the declared/injected resource and the locally created resource are not the same object.

---

# 16. What This Project Teaches

Although the project is small, it demonstrates several important concepts.

### 1. Spring Boot can host an MCP Server

The application uses Spring Boot as the runtime foundation.

### 2. Spring AI provides MCP integration

The dependency:

```xml
spring-ai-starter-mcp-server-webmvc
```

provides the MCP server infrastructure.

### 3. `@McpTool` exposes Java methods as MCP tools

The annotation:

```java
@McpTool
```

is the key bridge between Java application code and MCP capabilities.

### 4. MCP tools can access application resources

The tool reads:

```text
sachin tendulkar.md
```

from the classpath.

### 5. Tool descriptions are important

The description:

```text
This tool will return information about Sachin Tendulkar.
```

helps clients understand the purpose of the capability.

### 6. MCP is not the LLM

The project doesn't contain an LLM.

Instead, the architecture can be:

```text
LLM
 ↓
MCP Client
 ↓
This MCP Server
 ↓
Tool
 ↓
Data Source
```

This separation is fundamental when designing AI-enabled applications.

---

# Complete Architecture

Putting everything together:

```text
                           User
                            │
                            │ Natural language
                            ▼
                    ┌─────────────────┐
                    │      LLM        │
                    │                 │
                    │ Intent /        │
                    │ Reasoning       │
                    └────────┬────────┘
                             │
                       Tool selection
                             │
                             ▼
                    ┌─────────────────┐
                    │   MCP Client    │
                    └────────┬────────┘
                             │
                        MCP Protocol
                             │
                             ▼
             ┌─────────────────────────────┐
             │     Spring Boot MCP Server  │
             │                             │
             │        @McpTool             │
             │             │               │
             │             ▼               │
             │    getAboutTendulkar()      │
             └─────────────┬───────────────┘
                           │
                           ▼
                  ┌──────────────────┐
                  │ Resource Loader  │
                  └────────┬─────────┘
                           │
                           ▼
                sachin tendulkar.md
```

The key idea is:

```text
LLM
 ↓
"What should I do?"

MCP
 ↓
"How can the AI invoke the capability?"

Tool
 ↓
"Perform the operation."

Backend/Data
 ↓
"Provide the actual information."
```

---

# Conclusion

The project demonstrates a minimal but complete example of building an **MCP Server using Spring Boot and Spring AI**.

The implementation is intentionally simple:

```text
Spring Boot
     ↓
Spring AI MCP Server
     ↓
@McpTool
     ↓
getAboutTendulkar()
     ↓
sachin tendulkar.md
```

But this simple example establishes the foundation for much more sophisticated architectures.

The Markdown file can eventually be replaced by:

```text
REST API
Database
Vector Database
Enterprise Service
Cloud Service
Workflow Engine
Internal Application
```

while the MCP interface can remain the AI-facing abstraction.

The most important architectural takeaway is:

> **MCP is not a replacement for your existing backend systems. It provides a standardized interface through which AI applications can discover and interact with application capabilities.**

In an enterprise environment, this can result in an architecture where existing REST APIs, databases, and business services remain unchanged while an MCP layer exposes selected capabilities to AI agents.

And that is where the combination of **Spring Boot + Spring AI + MCP + LLMs** becomes particularly interesting.
