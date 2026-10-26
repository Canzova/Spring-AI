package com.openAI.firstProject.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class DateTimeTool {

    @Tool(description = "Get the current date and time in users zone.")
    private String getCurrentDateAndTime(){
        log.info("Get the current date and time in users zone.");
        ZoneId systemZone = ZoneId.systemDefault();

        return LocalDateTime
                .now()
                .atZone(ZoneId.of(systemZone.getId()))
                .toString();
    }

    @Tool(description = "Use this tool to set the alarm for user with the give date and time")
    public void setAlarm(@ToolParam(description = "Current date and time") String dateAndTime){
        log.info("set alarm for user with the give date and time");
    }

}
