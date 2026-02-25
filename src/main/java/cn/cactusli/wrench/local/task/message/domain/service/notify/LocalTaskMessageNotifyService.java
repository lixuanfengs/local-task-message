package cn.cactusli.wrench.local.task.message.domain.service.notify;

import cn.cactusli.wrench.local.task.message.domain.adapter.repository.ILocalTaskMessageRepository;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.model.vo.enums.TaskNotifyEnum;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageNotifyService;
import cn.cactusli.wrench.local.task.message.domain.service.notify.strategy.INotifyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/5 17:04
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Service
public class LocalTaskMessageNotifyService implements ILocalTaskMessageNotifyService {

    private final Map<String, INotifyStrategy> notifyStrategyConfig;

    public LocalTaskMessageNotifyService(Map<String, INotifyStrategy> notifyStrategyConfig, ILocalTaskMessageRepository repository) {
        this.notifyStrategyConfig = notifyStrategyConfig;
    }

    @Override
    public String notify(TaskMessageEntityCommand command) throws Exception {
        // 获取通知策略
        String strategyBeanName = TaskNotifyEnum.getStrategyByType(command.getNotifyType());
        INotifyStrategy notifyStrategy = notifyStrategyConfig.get(strategyBeanName);

        // 执行通知操作
        return notifyStrategy.notify(command);
    }

}
