package cn.cactusli.wrench.local.task.message.domain.adapter.port;

import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;

/**
 * 服务端口
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/5 17:08
 * @github https://github.com/lixuanfengs
 */
public interface ILocalTaskMessagePort {

    String notify2http(TaskMessageEntityCommand taskMessageEntityCommand) throws Exception;

    String notify2rabbitmq(TaskMessageEntityCommand taskMessageEntityCommand) throws Exception;

}
