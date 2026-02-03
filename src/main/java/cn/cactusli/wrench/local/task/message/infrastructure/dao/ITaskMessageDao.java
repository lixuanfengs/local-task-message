package cn.cactusli.wrench.local.task.message.infrastructure.dao;

import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.TaskMessagePO;

import java.sql.SQLException;

/**
 * 任务消息DAO接口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/2 18:00
 * @github https://github.com/lixuanfengs
 */
public interface ITaskMessageDao {

    /**
     * 插入任务消息
     * @param taskMessagePO 任务消息PO对象
     * @return 影响行数
     */
    int insert(TaskMessagePO taskMessagePO) throws SQLException;

}
