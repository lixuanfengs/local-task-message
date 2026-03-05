package cn.cactusli.wrench.local.task.message.test.notify;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.model.vo.enums.TaskNotifyEnum;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageHandleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Notify2HTTPTest {

    @Resource
    private ILocalTaskMessageHandleService handleService;

    @Test
    public void test() throws InterruptedException {
        TaskMessageEntityCommand taskMessageEntityCommand = new TaskMessageEntityCommand(
                "TASK_NEW_25111502",
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

        new CountDownLatch(1).await();
    }

}
