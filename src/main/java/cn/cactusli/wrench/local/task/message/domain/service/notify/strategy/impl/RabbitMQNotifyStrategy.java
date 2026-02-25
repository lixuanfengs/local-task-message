package cn.cactusli.wrench.local.task.message.domain.service.notify.strategy.impl;

import cn.cactusli.wrench.local.task.message.domain.adapter.port.ILocalTaskMessagePort;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.service.notify.strategy.INotifyStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 通知策略
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/5 17:10
 * @github https://github.com/lixuanfengs
 */
@Component("rabbitMQNotifyStrategy")
public class RabbitMQNotifyStrategy implements INotifyStrategy {

    @Resource
    private ILocalTaskMessagePort port;

    @Override
    public String notify(TaskMessageEntityCommand command) throws Exception {
        try {
            return port.notify2rabbitmq(command);

        } catch (Exception e) {
            throw e;
        }
    }

}
