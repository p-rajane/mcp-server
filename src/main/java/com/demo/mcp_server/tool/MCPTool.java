package com.demo.mcp_server.tool;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Pramod Rajane
 *
 */

@Component
public class MCPTool {

    @Value("classpath:sachin tendulkar.md")
    private Resource resource;

    @McpTool(name = "getAboutTendulkar", description = "This tool will return information about Sachin Tendulkar.")
    public String getAboutTendulkar() {
        Resource resource = new org.springframework.core.io.ClassPathResource("patients.json");
        try {
            return readFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readFile() throws Exception {
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
