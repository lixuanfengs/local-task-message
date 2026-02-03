package cn.cactusli.wrench.local.task.message.infrastructure.adapter.repository;

import cn.cactusli.wrench.local.task.message.domain.adapter.repository.ILocalTaskMessageRepository;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.ITaskMessageDao;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.TaskMessagePO;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
}
