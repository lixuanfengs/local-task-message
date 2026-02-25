package cn.cactusli.wrench.local.task.message.domain.model.vo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 任务通知类型枚举
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/2/2 17:56
 * @github https://github.com/lixuanfengs
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TaskNotifyEnum {

    HTTP("http", "httpNotifyStrategy", "HTTP通知"),
    RABBIT_MQ("rabbit_mq", "rabbitMQNotifyStrategy", "MQ通知"),
    ;

    private String type;
    private String strategy;
    private String desc;

    public static TaskNotifyEnum of(String type) {
        if (type == null) return null;
        for (TaskNotifyEnum value : TaskNotifyEnum.values()) {
            if (value.getType() != null && value.getType().equalsIgnoreCase(type)) {
                return value;
            }
        }
        return null;
    }

    public static String getStrategyByType(String type) {
        TaskNotifyEnum e = of(type);
        return e == null ? null : e.getStrategy();
    }

}
