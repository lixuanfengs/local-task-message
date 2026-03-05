package cn.cactusli.wrench.local.task.message.infrastructure.dao;

import cn.cactusli.wrench.local.task.message.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IGroupBuyOrderListDao {

    void insert(GroupBuyOrderList groupBuyOrderListReq);

}
