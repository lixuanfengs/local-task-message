package cn.cactusli.wrench.local.task.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地任务消息配置属性
 * 支持配置多个任务组，每个组包含： - groupId: 任务组标识 - houseNumbers: 扫描的门牌号列表（如 1,2,3） - cron: 执行调度的 cron 表达式（可选） - fixedDelayMs: 固定延迟毫秒（可选，和 cron 二选一） - limit: 每次拉取的最大任务条数（默认 100）
 * 示例 application.yml 配置：
 *   xfg:
 *     wrench:
 *       task:
 *         config:
 *           groups:
 *             # 使用 cron 表达式调度（每 10 秒执行一次）
 *             - groupId: http-group
 *               houseNumbers: [1, 2, 3]
 *               cron: "0/10 * * * * ?"
 *               limit: 100
 *             # 使用固定延迟调度（每 5 秒执行一次）
 *             - groupId: mq-group
 *               houseNumbers: [4, 5]
 *               fixedDelayMs: 5000
 *               limit: 50
 *
 * 说明： - 每个任务组需配置 `houseNumbers`，用于分片扫描任务记录。 - `cron` 与 `fixedDelayMs` 二选一；若两者都未配置，则默认使用 `fixedDelayMs=5000`。 - `limit` 不配置时默认 100。
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/3/2 15:53
 * @github https://github.com/lixuanfengs
 */
@Data
@ConfigurationProperties(prefix = "cactusli.wrench.task.config", ignoreInvalidFields = true)
public class LocalTaskMessageAutoProperties {

    /**
     * 任务组配置列表
     */
    private List<TaskGroupConfig> groups = new ArrayList<>();

    @Data
    public static class TaskGroupConfig {
        /**
         * 任务组ID或名称，用于日志区分
         */
        private String groupId = "default";

        /**
         * 扫描的门牌号列表，例如 [1,2,3]
         */
        private List<Integer> houseNumbers = new ArrayList<>();

        /**
         * 调度 cron 表达式，例如："0/10 * * * * ?" 表示每10秒
         */
        private String cron;

        /**
         * 固定延迟毫秒；若配置此项则使用固定延迟调度
         */
        private Long fixedDelayMs;

        /**
         * 每次批量处理限制条数
         */
        private Integer limit = 100;
    }

}
