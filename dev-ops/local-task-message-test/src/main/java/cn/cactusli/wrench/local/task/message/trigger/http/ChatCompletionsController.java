package cn.cactusli.wrench.local.task.message.trigger.http;

import cn.cactusli.wrench.local.task.message.trigger.http.dto.ChatCompletionRequest;
import cn.cactusli.wrench.local.task.message.trigger.http.dto.ChatCompletionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ChatCompletionsController {

    /**
     *  curl -sS http://127.0.0.1:8091/v1/chat/completions -H "Content-Type: application/json" -d '{"model":"gpt-4o","messages":[{"role":"user","content":"1+1"}]}' | jq
     */
    @PostMapping("/chat/completions")
    public ChatCompletionResponse completions(@RequestBody ChatCompletionRequest request,
                                              @RequestHeader(value = "Authorization", required = false) String authorization) {
        // 打印请求日志
        log.info("[ChatCompletions] auth={}, model={}, messages={}", authorization, request.getModel(), request.getMessages());

        // 仅提供出入参：这里返回一个最小响应并打印日志
        ChatCompletionResponse response = new ChatCompletionResponse();
        response.setId(UUID.randomUUID().toString());
        response.setModel(request.getModel());
        response.setMessages(request.getMessages());

        // 打印响应日志
        log.info("[ChatCompletions] response id={}, model={}, messages_size={}",
                response.getId(), response.getModel(), response.getMessages() == null ? 0 : response.getMessages().size());

        return response;
    }
}