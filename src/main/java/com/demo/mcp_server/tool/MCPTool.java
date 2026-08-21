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
    private Resource sachinResource;

    @Value("classpath:virat kohli.md")
    private Resource viratResource;



    @McpTool(name = "getAboutTendulkar", description = "This tool will return information about Sachin Tendulkar.")
    public String getAboutTendulkar() {
        try {
            return readFile(sachinResource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @McpTool(name = "getAboutKohli", description = "This tool will return information about Virat Kohli.")
    public String getAboutKohli() {
        try {
            return readFile(viratResource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readFile(Resource resource) throws Exception {
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
