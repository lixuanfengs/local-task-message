package cn.cactusli.wrench.local.task.message.infrastructure.event;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 事件消息
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/29 10:54
 * @github https://github.com/lixuanfengs
 */
@Getter
public class SpringTaskMessageEvent extends ApplicationEvent {

    private static final long serialVersionUID = -7580485467582771923L;

    private final TaskMessageEntityCommand taskMessageEntityCommand;


    public SpringTaskMessageEvent(Object source, TaskMessageEntityCommand taskMessageEntityCommand) {
        super(source);
        this.taskMessageEntityCommand = taskMessageEntityCommand;
    }
    @Override
    public String toString() {
        return "TaskMessageEvent{" +
                "taskMessage='" + taskMessageEntityCommand + '\'' +
                '}';
    }

}
