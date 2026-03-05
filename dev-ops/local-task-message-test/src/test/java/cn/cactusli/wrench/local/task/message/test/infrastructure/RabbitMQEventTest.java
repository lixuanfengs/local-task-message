package cn.cactusli.wrench.local.task.message.test.infrastructure;

import cn.cactusli.wrench.local.task.message.infrastructure.event.RabbitMQEvent;
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
public class RabbitMQEventTest {

    @Resource
    private RabbitMQEvent rabbitMQEvent;

    @Value("${spring.rabbitmq.config.producer.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.config.producer.topic_order_success.routing_key}")
    private String routingKey;

    @Test
    public void test() throws InterruptedException {
        rabbitMQEvent.publish(exchange, routingKey, "测试消息01");
        rabbitMQEvent.publish(exchange, routingKey, "测试消息02");
        rabbitMQEvent.publish(exchange, routingKey, "测试消息03");

        new CountDownLatch(1).await();
    }

}
