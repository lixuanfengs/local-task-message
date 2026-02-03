package cn.cactusli.wrench.local.task.message.domain.adapter.repository;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;

/**
 * 本地任务消息仓储接口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/2 18:05
 * @github https://github.com/lixuanfengs
 */
public interface ILocalTaskMessageRepository {

    /**
     * 保存任务消息
     * @param command 任务消息实体命令
     */
    void saveTaskMessage(TaskMessageEntityCommand command) throws Exception;

}
