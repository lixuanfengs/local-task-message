package cn.cactusli.wrench.local.task.message.infrastructure.adapter.event;

import cn.cactusli.wrench.local.task.message.domain.adapter.event.ILocalTaskMessageEvent;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.infrastructure.event.SpringTaskMessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 事件消息
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/29 14:48
 * @github https://github.com/lixuanfengs
 */
@Component
public class LocalTaskMessageEvent implements ILocalTaskMessageEvent {

    private final ApplicationEventPublisher eventPublisher;

    public LocalTaskMessageEvent(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishEvent(TaskMessageEntityCommand taskMessageEntityCommand) {
        // 构建事件
        SpringTaskMessageEvent springTaskMessageEvent = new SpringTaskMessageEvent(this, taskMessageEntityCommand);
        // 发布事件
        eventPublisher.publishEvent(springTaskMessageEvent);
    }

}
