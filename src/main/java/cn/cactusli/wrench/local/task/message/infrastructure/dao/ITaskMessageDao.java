package cn.cactusli.wrench.local.task.message.infrastructure.dao;

import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.TaskMessagePO;

import java.sql.SQLException;
import java.util.List;

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

    /**
     * 根据任务ID修改状态
     * @param taskId 任务ID
     * @param status 状态（0-待处理，1-处理中，2-已完成，3-失败）
     * @return 影响行数
     */
    int updateStatusByTaskId(String taskId, Integer status);

    /**
     * 根据门牌号查询任务消息列表
     * @param houseNumbers 门牌号列表
     * @param id 查询ID大于此值的记录
     * @param limit 限制返回结果数量
     * @return 任务消息列表
     */
    List<TaskMessagePO> selectByHouseNumber(List<Integer> houseNumbers, Long id, Integer limit);

    /**
     * 根据门牌号查询符合条件的最小ID
     * @param houseNumbers 门牌号列表
     * @return 最小ID，如果没有找到则返回null
     */
    Long selectMinIdByHouseNumber(List<Integer> houseNumbers);

}
