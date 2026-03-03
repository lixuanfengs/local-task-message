package cn.cactusli.wrench.local.task.message.config;

import cn.cactusli.wrench.local.task.message.infrastructure.gateway.GenericHttpGateway;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/1/19 17:53
 * @github https://github.com/lixuanfengs
 */

@Configuration
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(value = {
        LocalTaskMessageAutoProperties.class})
@ComponentScan(basePackages = {
        "cn.cactusli.wrench.local.task.message.domain.*",
        "cn.cactusli.wrench.local.task.message.infrastructure.*",
        "cn.cactusli.wrench.local.task.message.trigger.*"
})
public class LocalTaskMessageAutoConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        ConnectionPool pool = new ConnectionPool(10, 5, TimeUnit.MINUTES);
        return new OkHttpClient.Builder()
                .connectionPool(pool)
                .retryOnConnectionFailure(true)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public GenericHttpGateway genericHttpGateway(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(GenericHttpGateway.class);
    }

    @Bean("taskMessageScheduler")
    public ThreadPoolTaskScheduler taskMessageScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("TaskMessageScheduler-");
        scheduler.initialize();
        return scheduler;
    }

}
