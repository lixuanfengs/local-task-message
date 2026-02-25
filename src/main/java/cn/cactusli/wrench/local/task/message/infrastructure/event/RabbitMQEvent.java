package cn.cactusli.wrench.local.task.message.infrastructure.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/6 16:50
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Component
public class RabbitMQEvent {
    /**
     * required = false 避免用户没有使用 RabbitMQ 而导致报错
     */
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    public void publish(String exchange, String routingKey, String message) {
        try {
            if (null == rabbitTemplate){
                log.error("应用服务方，尚未配置 RabbitMQ Template 不能完成 MQ 发送");
                return;
            }

            rabbitTemplate.convertAndSend(exchange, routingKey, message, m -> {
                // 持久化消息配置
                m.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return m;
            });
        } catch (Exception e) {
            log.error("发送MQ消息失败 exchange:{} routingKey:{} message:{}", exchange, routingKey, message, e);
            throw e;
        }
    }


}
