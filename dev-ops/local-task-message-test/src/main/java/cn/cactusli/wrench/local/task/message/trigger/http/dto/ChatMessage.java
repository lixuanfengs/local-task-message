package cn.cactusli.wrench.local.task.message.trigger.http.dto;

import lombok.Data;

@Data
public class ChatMessage {
    private String role;
    private String content;
}