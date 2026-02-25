package cn.cactusli.wrench.local.task.message.infrastructure.adapter.port;

import cn.cactusli.wrench.local.task.message.domain.adapter.port.ILocalTaskMessagePort;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.infrastructure.event.RabbitMQEvent;
import cn.cactusli.wrench.local.task.message.infrastructure.gateway.GenericHttpGateway;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务端口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/6 16:49
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Component
public class LocalTaskMessagePort implements ILocalTaskMessagePort {

    private final GenericHttpGateway genericHttpGateway;

    private final RabbitMQEvent rabbitMQEvent;

    public LocalTaskMessagePort(GenericHttpGateway genericHttpGateway, RabbitMQEvent rabbitMQEvent) {
        this.genericHttpGateway = genericHttpGateway;
        this.rabbitMQEvent = rabbitMQEvent;
    }

    @Override
    public String notify2http(TaskMessageEntityCommand taskMessageEntityCommand) throws Exception {
        try {
            // 1. 获取参数
            TaskMessageEntityCommand.NotifyConfig notifyConfig = taskMessageEntityCommand.getNotifyConfig();
            TaskMessageEntityCommand.NotifyConfig.HTTP http = notifyConfig.getHttp();

            // 2. 设置头信息
            Map<String, String> headers = new HashMap<>();

            if (StringUtils.isNotBlank(http.getContentType())) {
                headers.put("Content-Type", http.getContentType());
            }

            if (StringUtils.isNotBlank(http.getAuthorization())) {
                headers.put("Authorization", http.getAuthorization());
            }

            // 3. 处理调用
            Call<ResponseBody> call = genericHttpGateway.post(http.getUrl(), headers, RequestBody.create(taskMessageEntityCommand.getParameterJson(), MediaType.parse("application/json")));
            Response<ResponseBody> response = call.execute();

            // 4. 返回结果
            return response.body() != null ? response.body().string() : "";
        } catch (Exception e) {
            log.error("http 请求失败", e);
            throw e;
        }
    }

    @Override
    public String notify2rabbitmq(TaskMessageEntityCommand taskMessageEntityCommand) throws Exception {
        TaskMessageEntityCommand.NotifyConfig.MQ mq = taskMessageEntityCommand.getNotifyConfig().getMq();
        rabbitMQEvent.publish(mq.getExchange(), mq.getTopic(), taskMessageEntityCommand.getParameterJson());
        return "success";
    }
}
