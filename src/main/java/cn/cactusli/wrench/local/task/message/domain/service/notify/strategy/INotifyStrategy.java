package cn.cactusli.wrench.local.task.message.domain.service.notify.strategy;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;

/**
 * 通知策略接口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/5 17:05
 * @github https://github.com/lixuanfengs
 */
public interface INotifyStrategy {

    String notify(TaskMessageEntityCommand command) throws Exception;

}
