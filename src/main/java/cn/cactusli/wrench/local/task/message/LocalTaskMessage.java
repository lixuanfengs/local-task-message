package cn.cactusli.wrench.local.task.message;

import java.lang.annotation.*;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/3/3 16:48
 * @github https://github.com/lixuanfengs
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LocalTaskMessage {

    /**
     * 实体属性名称
     */
    String entityAttributeName() default "";

}
