package cn.cactusli.wrench.local.task.message.infrastructure.dao.impl;

import cn.cactusli.wrench.local.task.message.infrastructure.dao.ITaskMessageDao;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.TaskMessagePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 任务消息DAO实现
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/2 18:01
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Component
public class TaskMessageDaoImpl implements ITaskMessageDao {

    private final DataSource dataSource;

    public TaskMessageDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insert(TaskMessagePO taskMessagePO) throws SQLException {
        String sql = "INSERT INTO local_task_message (task_id, task_name, notify_type, notify_config, status, parameter_json, house_number ,create_time, update_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, taskMessagePO.getTaskId());
            ps.setString(2, taskMessagePO.getTaskName());
            ps.setString(3, taskMessagePO.getNotifyType());
            ps.setString(4, taskMessagePO.getNotifyConfig());
            ps.setInt(5, taskMessagePO.getStatus());
            ps.setString(6, taskMessagePO.getParameterJson());
            ps.setInt(7, taskMessagePO.getHouseNumber());
            ps.setObject(8, taskMessagePO.getCreateTime());
            ps.setObject(9, taskMessagePO.getUpdateTime());

            return ps.executeUpdate();

        } catch (SQLException e) {
            log.error("插入任务消息失败，taskId: {}", taskMessagePO.getTaskId(), e);
            throw e;
        }
    }
}
