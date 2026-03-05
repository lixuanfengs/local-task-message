package cn.cactusli.wrench.local.task.message.trigger.http.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionRequest {
    private String model;
    private List<ChatMessage> messages;
}