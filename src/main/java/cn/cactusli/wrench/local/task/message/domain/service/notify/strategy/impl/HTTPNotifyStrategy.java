package cn.cactusli.wrench.local.task.message.domain.service.notify.strategy.impl;

import cn.cactusli.wrench.local.task.message.domain.adapter.port.ILocalTaskMessagePort;
import cn.cactusli.wrench.local.task.message.domain.adapter.repository.ILocalTaskMessageRepository;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.service.notify.strategy.INotifyStrategy;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * HTTP 通知策略
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/5 17:07
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Component("httpNotifyStrategy")
public class HTTPNotifyStrategy implements INotifyStrategy {

    @Resource
    private ILocalTaskMessagePort port;

    @Resource
    private ILocalTaskMessageRepository repository;

    @Override
    public String notify(TaskMessageEntityCommand command) throws Exception {
        try {
            String result = port.notify2http(command);
            // 通知成功，更新状态为成功
            repository.updateTaskStatusToSuccess(command.getTaskId());
            return result;
        } catch (Exception e) {
            log.error("http notify error {}", JSON.toJSONString(command), e);
            // 通知失败，更新状态为失败
            repository.updateTaskStatusToFailed(command.getTaskId());
            throw e;
        }
    }

}
