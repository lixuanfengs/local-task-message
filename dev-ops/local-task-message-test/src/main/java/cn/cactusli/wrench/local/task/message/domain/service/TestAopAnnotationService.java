package cn.cactusli.wrench.local.task.message.domain.service;

import cn.cactusli.wrench.local.task.message.LocalTaskMessage;
import cn.cactusli.wrench.local.task.message.domain.model.XxxVO;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.IGroupBuyOrderListDao;
import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.GroupBuyOrderList;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/3/3 17:07
 * @github https://github.com/lixuanfengs
 */
@Service
public class TestAopAnnotationService {

    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;

    @Transactional
    @LocalTaskMessage(entityAttributeName = "command")
//    @LocalTaskMessage(entityAttributeName = "req.command")
    public void insertOrderTransactional_01(TaskMessageEntityCommand command) throws InterruptedException {
        GroupBuyOrderList order = GroupBuyOrderList.builder()
                .userId("lxf89887")
                .teamId("69268465")
                .orderId("537625111801")
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
                .outTradeNo("537625111801")
                .bizId("537625111801_lixf771_4")
                .build();

        // 执行插入
        groupBuyOrderListDao.insert(order);

    }

    //    @Transactional
    @LocalTaskMessage(entityAttributeName = "xxxVO.command")
    public void insertOrderTransactional_02(XxxVO xxxVO) throws InterruptedException {
        // 执行插入
        groupBuyOrderListDao.insert(xxxVO.getGroupBuyOrderList());
    }
}
