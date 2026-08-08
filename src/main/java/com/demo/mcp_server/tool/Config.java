package com.demo.mcp_server.tool;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Pramod Rajane
 */
@Configuration
public class Config {

    /*@Bean
    public List<ToolCallback> toolCallbacks(MCPTool mcpTool) {
        return List.of(ToolCallbacks.from(mcpTool));
    }*/
}
