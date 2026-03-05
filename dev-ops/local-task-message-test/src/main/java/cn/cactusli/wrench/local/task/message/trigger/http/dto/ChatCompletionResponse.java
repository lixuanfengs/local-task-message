package cn.cactusli.wrench.local.task.message.trigger.http.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionResponse {
    private String id;
    private String model;
    private List<ChatMessage> messages;
}