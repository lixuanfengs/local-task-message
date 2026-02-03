package cn.cactusli.wrench.local.task.message.domain.service.handle;

import cn.cactusli.wrench.local.task.message.domain.adapter.event.ILocalTaskMessageEvent;
import cn.cactusli.wrench.local.task.message.domain.adapter.repository.ILocalTaskMessageRepository;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageHandleService;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 本地消息处理服务
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/29 15:04
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Service
public class LocalTaskMessageHandleService implements ILocalTaskMessageHandleService {

    @Resource
    private ILocalTaskMessageEvent event;

    @Resource
    private ILocalTaskMessageRepository repository;

    @Override
    public void acceptTaskMessage(TaskMessageEntityCommand command) {
        try {
            log.info("受理任务消息: {}", command);

            // 1. 保存任务消息
            repository.saveTaskMessage(command);

            // 2. 发布事件消息
            event.publishEvent(command);

        } catch (Exception e) {
            log.error("受理任务消息执行失败 {}", JSON.toJSONString(command), e);
        }
    }
}
