package cn.cactusli.wrench.local.task.message.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/19 17:53
 * @github https://github.com/lixuanfengs
 */

@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = {
        "cn.cactusli.wrench.local.task.message.domain.*",
        "cn.cactusli.wrench.local.task.message.infrastructure.*",
        "cn.cactusli.wrench.local.task.message.trigger.*"
})
public class LocalTaskMessageAutoConfig {

}
