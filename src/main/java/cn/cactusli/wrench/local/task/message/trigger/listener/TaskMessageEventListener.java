package cn.cactusli.wrench.local.task.message.trigger.listener;

import cn.cactusli.wrench.local.task.message.infrastructure.event.SpringTaskMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Spring Event 事件消息
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/29 10:53
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Service
public class TaskMessageEventListener {

    @EventListener
    @Async
    public void handleTaskMessageEvent(SpringTaskMessageEvent event) {
        try {
            log.info("收到任务消息事件 - 消息内容: {}, 事件时间戳: {}", event.getTaskMessageEntityCommand(), event.getTimestamp());

        } catch (Exception e) {
            log.error("处理任务消息事件失败 - 消息: {}, 错误: {}",
                    event.getTaskMessageEntityCommand(), e.getMessage(), e);
        }
    }

}
