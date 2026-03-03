package cn.cactusli.wrench.local.task.message.domain.service;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;

import java.util.List;

/**
 * 数据服务接口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/3/2 15:57
 * @github https://github.com/lixuanfengs
 */
public interface ILocalTaskMessageDataService {

    /**
     * 根据门牌号查询任务消息列表
     * @param houseNumbers 门牌号列表
     * @param id 查询ID大于此值的记录
     * @param limit 限制返回结果数量
     * @return 任务消息列表
     */
    List<TaskMessageEntityCommand> selectByHouseNumber(List<Integer> houseNumbers, Long id, Integer limit);

    /**
     * 根据门牌号查询符合条件的最小ID
     * @param houseNumbers 门牌号列表
     * @return 最小ID，如果没有找到则返回null
     */
    Long selectMinIdByHouseNumber(List<Integer> houseNumbers);
    
}
