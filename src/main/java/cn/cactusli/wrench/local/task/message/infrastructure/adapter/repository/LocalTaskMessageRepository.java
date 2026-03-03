package cn.cactusli.wrench.local.task.message.infrastructure.adapter.repository;

import cn.cactusli.wrench.local.task.message.domain.adapter.repository.ILocalTaskMessageRepository;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.ITaskMessageDao;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.TaskMessagePO;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/3 15:26
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Repository
public class LocalTaskMessageRepository implements ILocalTaskMessageRepository {

    private final ITaskMessageDao taskMessageDao;

    public LocalTaskMessageRepository(ITaskMessageDao taskMessageDao) {
        this.taskMessageDao = taskMessageDao;
    }

    @Override
    public void saveTaskMessage(TaskMessageEntityCommand command) throws Exception {
        TaskMessagePO po = new TaskMessagePO();
        po.setTaskId(command.getTaskId());
        po.setTaskName(command.getTaskName());
        po.setNotifyType(command.getNotifyType());
        po.setStatus(command.getStatus());
        po.setParameterJson(command.getParameterJson());

        // 将NotifyConfig对象转换为JSON字符串
        if (command.getNotifyConfig() != null) {
            po.setNotifyConfig(JSON.toJSONString(command.getNotifyConfig()));
        }

        // 根据任务ID计算哈希值，取正数，获取最后一位数字作为门牌号
        int hashCode = Math.abs(command.getTaskId().hashCode());
        int houseNumber = hashCode % 10;
        po.setHouseNumber(houseNumber);

        po.setCreateTime(LocalDateTime.now());
        po.setUpdateTime(LocalDateTime.now());

        try {
            int result = taskMessageDao.insert(po);
            if (1 != result) {
                throw new RuntimeException("result is not 1 taskId:{}" + command.getTaskId());
            }
        } catch (Exception e) {
            log.error("保存任务消息失败，taskId: {} {}", command.getTaskId(), JSON.toJSONString(command), e);
            throw e;
        }
    }

    @Override
    public void updateTaskStatusToSuccess(String taskId) {
        try {
            // 状态 2 表示已完成
            int result = taskMessageDao.updateStatusByTaskId(taskId, 2);
            if (result > 0) {
                log.info("更新任务状态为成功，taskId: {}", taskId);
            } else {
                log.warn("更新任务状态为成功失败，未找到对应任务，taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("更新任务状态为成功失败，taskId: {}", taskId, e);
            throw e;
        }
    }

    @Override
    public void updateTaskStatusToFailed(String taskId) {
        try {
            // 状态 3 表示失败
            int result = taskMessageDao.updateStatusByTaskId(taskId, 3);
            if (result > 0) {
                log.info("更新任务状态为失败，taskId: {}", taskId);
            } else {
                log.warn("更新任务状态为失败失败，未找到对应任务，taskId: {}", taskId);
            }
        } catch (Exception e) {
            log.error("更新任务状态为失败失败，taskId: {}", taskId, e);
            throw e;
        }
    }

    @Override
    public List<TaskMessageEntityCommand> selectByHouseNumber(List<Integer> houseNumbers, Long id, Integer limit) {
        try {
            List<TaskMessagePO> poList = taskMessageDao.selectByHouseNumber(houseNumbers, id, limit);
            List<TaskMessageEntityCommand> result = new ArrayList<>();
            if (poList == null || poList.isEmpty()) {
                return result;
            }
            for (TaskMessagePO po : poList) {
                result.add(convertToCommand(po));
            }
            return result;
        } catch (Exception e) {
            log.error("根据门牌号查询任务消息列表失败，houseNumbers: {} id: {} limit: {}", houseNumbers, id, limit, e);
            throw e;
        }
    }

    @Override
    public Long selectMinIdByHouseNumber(List<Integer> houseNumbers) {
        try {
            return taskMessageDao.selectMinIdByHouseNumber(houseNumbers);
        } catch (Exception e) {
            log.error("根据门牌号查询最小ID失败，houseNumbers: {}", houseNumbers, e);
            throw e;
        }
    }

    /**
     * 将PO对象转换为领域实体命令
     */
    private TaskMessageEntityCommand convertToCommand(TaskMessagePO po) {
        TaskMessageEntityCommand cmd = new TaskMessageEntityCommand();
        cmd.setId(po.getId());
        cmd.setTaskId(po.getTaskId());
        cmd.setTaskName(po.getTaskName());
        cmd.setNotifyType(po.getNotifyType());
        cmd.setStatus(po.getStatus());
        cmd.setParameterJson(po.getParameterJson());

        if (po.getNotifyConfig() != null) {
            try {
                TaskMessageEntityCommand.NotifyConfig notifyConfig = JSON.parseObject(po.getNotifyConfig(), TaskMessageEntityCommand.NotifyConfig.class);
                cmd.setNotifyConfig(notifyConfig);
            } catch (Exception e) {
                log.warn("解析 notifyConfig 失败，taskId:{} notifyConfig:{}", po.getTaskId(), po.getNotifyConfig(), e);
            }
        }

        return cmd;
    }
}
