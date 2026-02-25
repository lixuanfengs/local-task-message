package cn.cactusli.wrench.local.task.message.domain.service.notify.strategy.impl;

import cn.cactusli.wrench.local.task.message.domain.adapter.port.ILocalTaskMessagePort;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.service.notify.strategy.INotifyStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * HTTP 通知策略
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/5 17:07
 * @github https://github.com/lixuanfengs
 */
@Component("httpNotifyStrategy")
public class HTTPNotifyStrategy implements INotifyStrategy {

    @Resource
    private ILocalTaskMessagePort port;

    @Override
    public String notify(TaskMessageEntityCommand command) throws Exception {
        try {
            return port.notify2http(command);
        } catch (Exception e) {
            throw e;
        }
    }

}
