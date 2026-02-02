package cn.cactusli.wrench.local.task.message.domain.service;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;

/**
 * 本地消息处理服务接口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/19 17:55
 * @github https://github.com/lixuanfengs
 */
public interface ILocalTaskMessageHandleService {

    void acceptTaskMessage(TaskMessageEntityCommand command);

}
