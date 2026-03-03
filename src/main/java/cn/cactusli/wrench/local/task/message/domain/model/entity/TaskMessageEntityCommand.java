package cn.cactusli.wrench.local.task.message.domain.model.entity;

import cn.cactusli.wrench.local.task.message.domain.model.vo.enums.TaskNotifyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务消息实体命令
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/19 17:55
 * @github https://github.com/lixuanfengs
 */
@Data
public class TaskMessageEntityCommand {

    private Long id;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private String notifyType;

    /**
     * 通知配置
     */
    private NotifyConfig notifyConfig;

    /**
     * 任务状态
     */
    private Integer status;

    /**
     * 参数配置
     */
    private String parameterJson;

    public TaskMessageEntityCommand() {
    }

    public TaskMessageEntityCommand(String taskId, String taskName, TaskNotifyEnum taskNotifyEnum, NotifyConfig notifyConfig, String parameterJson) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.notifyType = taskNotifyEnum.getType();
        this.notifyConfig = notifyConfig;
        this.status = 0;
        this.parameterJson = parameterJson;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotifyConfig {
        // mq 配置
        private MQ mq;
        // http 配置
        private HTTP http;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MQ {
            private String topic;
            private String exchange;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class HTTP {
            private String url;
            private String method;
            private String contentType;
            private String authorization;
        }
    }
}
