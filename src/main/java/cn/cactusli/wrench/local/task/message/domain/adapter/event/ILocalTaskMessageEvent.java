package cn.cactusli.wrench.local.task.message.domain.adapter.event;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;

/**
 * 事件消息接口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/29 14:49
 * @github https://github.com/lixuanfengs
 */
public interface ILocalTaskMessageEvent {

    void publishEvent(TaskMessageEntityCommand taskMessageEntityCommand);

}
