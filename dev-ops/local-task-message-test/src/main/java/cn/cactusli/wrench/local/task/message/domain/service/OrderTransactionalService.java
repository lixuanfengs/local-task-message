package cn.cactusli.wrench.local.task.message.domain.service;


import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.model.vo.enums.TaskNotifyEnum;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.IGroupBuyOrderListDao;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.GroupBuyOrderList;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderTransactionalService {

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;

    @Resource
    private ILocalTaskMessageHandleService handleService;

    @Transactional
    public void insertOrder(GroupBuyOrderList order) {
        groupBuyOrderListDao.insert(order);

        TaskMessageEntityCommand taskMessageEntityCommand = new TaskMessageEntityCommand(
                "TASK_NEW_06",
                "gpt调用测试任务",
                TaskNotifyEnum.HTTP,
                TaskMessageEntityCommand.NotifyConfig.builder()
                        .http(TaskMessageEntityCommand.NotifyConfig.HTTP.builder()
                                .url("http://127.0.0.1:8091/v1/chat/completions")
                                .method("post")
                                .contentType("application/json")
                                .build())
                        .build(),
                "{\"model\": \"gpt-4o\", \"messages\": [{\"role\": \"user\", \"content\": \"1+1\"}]}"
        );

        handleService.acceptTaskMessage(taskMessageEntityCommand);

    }

}