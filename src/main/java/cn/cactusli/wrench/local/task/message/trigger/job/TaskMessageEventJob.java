package cn.cactusli.wrench.local.task.message.trigger.job;

import cn.cactusli.wrench.local.task.message.config.LocalTaskMessageAutoProperties;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageDataService;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageNotifyService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 动态加载配置的任务组，扫描门牌号并发布事件
 *
 * @author 仙人球⁶ᴳ |
 * @date 2026/3/2 15:55
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Component
public class TaskMessageEventJob {

    /**
     * 记录每个任务组最近一次处理的最大ID
     */
    private final Map<String, AtomicLong> groupLastIdMap = new ConcurrentHashMap<>();

    private final LocalTaskMessageAutoProperties properties;
    private final ThreadPoolTaskScheduler scheduler;
    private final ILocalTaskMessageNotifyService notifyService;
    private final ILocalTaskMessageDataService dataService;

    public TaskMessageEventJob(LocalTaskMessageAutoProperties properties, ThreadPoolTaskScheduler scheduler, ILocalTaskMessageNotifyService notifyService, ILocalTaskMessageDataService dataService) {
        this.properties = properties;
        this.scheduler = scheduler;
        this.notifyService = notifyService;
        this.dataService = dataService;
    }

    @PostConstruct
    public void init() {
        List<LocalTaskMessageAutoProperties.TaskGroupConfig> groups = properties.getGroups();
        if (groups == null || groups.isEmpty()) {
            log.info("TaskMessageEventJob 未配置任务组，跳过调度初始化");
            return;
        }

        for (LocalTaskMessageAutoProperties.TaskGroupConfig group : groups) {
            scheduleGroup(group);
        }
    }

    private void scheduleGroup(LocalTaskMessageAutoProperties.TaskGroupConfig group) {
        String groupId = group.getGroupId();
        List<Integer> houseNumbers = group.getHouseNumbers();
        if (houseNumbers == null || houseNumbers.isEmpty()) {
            log.warn("任务组 [{}] 未配置 houseNumbers，跳过该组调度", groupId);
            return;
        }

        // 初始化 lastId
        groupLastIdMap.computeIfAbsent(groupId, k -> {
            Long minId = dataService.selectMinIdByHouseNumber(houseNumbers);
            long startId = (minId == null ? 0L : minId);
            log.info("任务组 [{}] 初始化起始ID为 {}，houseNumbers={}", groupId, startId, houseNumbers);
            return new AtomicLong(startId);
        });

        Runnable task = () -> {
            try {
                long lastId = groupLastIdMap.get(groupId).get();
                List<TaskMessageEntityCommand> cmdList = dataService.selectByHouseNumber(houseNumbers, lastId, group.getLimit());
                if (cmdList == null || cmdList.isEmpty()) {
                    return;
                }

                // 发布事件
                for (TaskMessageEntityCommand cmd : cmdList) {
                    notifyService.notify(cmd);
                }

                // 更新 lastId
                long maxId = cmdList.stream().map(TaskMessageEntityCommand::getId).max(Comparator.naturalOrder()).orElse(lastId);
                groupLastIdMap.get(groupId).set(maxId);

                log.info("任务组 [{}] 处理完成：拉取{}条，lastId: {} -> {}", groupId, cmdList.size(), lastId, maxId);
            } catch (Exception e) {
                log.error("任务组 [{}] 执行异常: {}", groupId, e.getMessage(), e);
            }
        };

        if (group.getCron() != null && !group.getCron().trim().isEmpty()) {
            scheduler.schedule(task, new CronTrigger(group.getCron()));
            log.info("任务组 [{}] 已按 cron [{}] 调度", groupId, group.getCron());
        } else {
            long delay = group.getFixedDelayMs() != null ? group.getFixedDelayMs() : 5000L;
            scheduler.scheduleWithFixedDelay(task, delay);
            log.info("任务组 [{}] 已按 fixedDelayMs [{}] 调度", groupId, delay);
        }
    }

}
