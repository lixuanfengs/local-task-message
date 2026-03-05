package cn.cactusli.wrench.local.task.message.test.notify;


import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.model.vo.enums.TaskNotifyEnum;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageHandleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Notify2RabbitMQTest {

    @Resource
    private ILocalTaskMessageHandleService handleService;

    @Value("${spring.rabbitmq.config.producer.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.config.producer.topic_order_success.routing_key}")
    private String routingKey;

    @Test
    public void test() throws InterruptedException {
        TaskMessageEntityCommand command1 = new TaskMessageEntityCommand();
        command1.setTaskId("TASK_NEW_2026020602");
        command1.setTaskName("gpt调用测试任务");
        command1.setNotifyType(TaskNotifyEnum.RABBIT_MQ.getType());
        command1.setNotifyConfig(
                TaskMessageEntityCommand.NotifyConfig.builder()
                        .mq(TaskMessageEntityCommand.NotifyConfig.MQ.builder()
                                .exchange(exchange)
                                .topic(routingKey)
                                .build())
                        .build());
        command1.setStatus(0);
        command1.setParameterJson("{\"model\": \"gpt-4o\", \"messages\": [{\"role\": \"user\", \"content\": \"1+1\"}]}");

        handleService.acceptTaskMessage(command1);

        new CountDownLatch(1).await();
    }

}
