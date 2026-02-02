package cn.cactusli.wrench.local.task.message.domain.model.entity;

import lombok.Data;

/**
 * 任务消息实体命令
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/19 17:55
 * @github https://github.com/lixuanfengs
 */
@Data
public class TaskMessageEntityCommand {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

}
