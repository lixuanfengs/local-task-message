package cn.cactusli.wrench.local.task.message.domain.service;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;

/**
 * 通知服务接口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/5 17:03
 * @github https://github.com/lixuanfengs
 */
public interface ILocalTaskMessageNotifyService {

    String notify(TaskMessageEntityCommand command) throws Exception;

}
