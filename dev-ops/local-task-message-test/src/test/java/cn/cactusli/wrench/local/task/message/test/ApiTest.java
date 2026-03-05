package cn.cactusli.wrench.local.task.message.test;

import cn.cactusli.wrench.local.task.message.domain.model.XxxVO;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.model.vo.enums.TaskNotifyEnum;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageHandleService;
import cn.cactusli.wrench.local.task.message.domain.service.TestAopAnnotationService;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.IGroupBuyOrderListDao;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.GroupBuyOrderList;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private ILocalTaskMessageHandleService localTaskMessageHandleService;

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;

    @Resource
    private TestAopAnnotationService testAopAnnotationService;


    @Test
    public void test() throws InterruptedException {

        // 事务测试
        insertOrderTransactional();

        new CountDownLatch(1).await();
    }

    @Transactional
    public void insertOrderTransactional() throws InterruptedException {

        // 构造插入数据
        GroupBuyOrderList order = GroupBuyOrderList.builder()
                .userId("lxf704")
                .teamId("69268465")
                .orderId("637625111203")
                .activityId(100123L)
                .startTime(new Date())
                .endTime(new Date(System.currentTimeMillis() + 3600_000))
                .goodsId("9890001")
                .source("s01")
                .channel("c01")
                .originalPrice(new BigDecimal("99.00"))
                .deductionPrice(new BigDecimal("10.00"))
                .payPrice(new BigDecimal("89.00"))
                .status(0)
                .outTradeNo("406025111201")
                .bizId("100123_25111201")
                .build();

        // 执行插入
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

        localTaskMessageHandleService.acceptTaskMessage(taskMessageEntityCommand);

        new CountDownLatch(1).await();
    }

    @Test
    public void test_task_01() throws InterruptedException {
        TaskMessageEntityCommand taskMessageEntityCommand = new TaskMessageEntityCommand(
                "TASK_5376251118012",
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

        testAopAnnotationService.insertOrderTransactional_01(taskMessageEntityCommand);
    }

    @Test
    public void test_task_02() throws InterruptedException {
        TaskMessageEntityCommand command = new TaskMessageEntityCommand(
                "TASK_5376251118023",
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

        GroupBuyOrderList order = GroupBuyOrderList.builder()
                .userId("lixf8787")
                .teamId("69268465")
                .orderId("537625111802")
                .activityId(100123L)
                .startTime(new Date())
                .endTime(new Date(System.currentTimeMillis() + 3600_000))
                .goodsId("9890001")
                .source("s01")
                .channel("c01")
                .originalPrice(new BigDecimal("99.00"))
                .deductionPrice(new BigDecimal("10.00"))
                .payPrice(new BigDecimal("89.00"))
                .status(0)
                .outTradeNo("537625111802")
                .bizId("537625111802_lixf8787")
                .build();

        XxxVO xxxVO = new XxxVO();
        xxxVO.setCommand(command);
        xxxVO.setGroupBuyOrderList(order);

        testAopAnnotationService.insertOrderTransactional_02(xxxVO);

        new CountDownLatch(1).await();
    }


}
