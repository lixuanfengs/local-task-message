package cn.cactusli.wrench.local.task.message.domain.service.handle;

import cn.cactusli.wrench.local.task.message.domain.adapter.event.ILocalTaskMessageEvent;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageHandleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 本地消息处理服务
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/29 15:04
 * @github https://github.com/lixuanfengs
 */
@Service
public class LocalTaskMessageHandleService implements ILocalTaskMessageHandleService {

    @Resource
    private ILocalTaskMessageEvent event;

    @Override
    public void acceptTaskMessage(TaskMessageEntityCommand command) {
        event.publishEvent(command);
    }

}
