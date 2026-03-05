package cn.cactusli.wrench.local.task.message.config.aop;

import cn.cactusli.wrench.local.task.message.LocalTaskMessage;
import cn.cactusli.wrench.local.task.message.domain.model.entity.TaskMessageEntityCommand;
import cn.cactusli.wrench.local.task.message.domain.service.ILocalTaskMessageHandleService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author 仙人球⁶ᴳ |
 * @date 2026/3/3 16:51
 * @github https://github.com/lixuanfengs
 */
@Slf4j
@Component
@Aspect
public class LocalTaskMessageAop {

    private final ILocalTaskMessageHandleService handleService;
    private final TransactionTemplate transactionTemplate;

    public LocalTaskMessageAop(ILocalTaskMessageHandleService handleService,
                               TransactionTemplate transactionTemplate) {
        this.handleService = handleService;
        this.transactionTemplate = transactionTemplate;
    }

    @Pointcut("@annotation(cn.cactusli.wrench.local.task.message.LocalTaskMessage)")
    public void aopPoint() {
    }

    @Around("aopPoint() && @annotation(localTaskMessage)")
    public Object notify(ProceedingJoinPoint joinPoint, LocalTaskMessage localTaskMessage) throws Throwable {
        String signature = joinPoint.getSignature().toShortString();
        String entityAttributeName = localTaskMessage.entityAttributeName();

        // 判断当前是否已有事务；有则共用，无则统一开启并包裹目标方法与切面逻辑
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();

        if (active) {
            try {
                Object result = joinPoint.proceed();
                TaskMessageEntityCommand command = resolveCommand(joinPoint, entityAttributeName);
                if (command != null) {
                    log.info("LocalTaskMessageAop 提取到命令对象(同一事务): 方法={} 路径={} 命令={}", signature, entityAttributeName, command);
                    handleService.acceptTaskMessage(command);
                } else {
                    log.warn("LocalTaskMessageAop 未能提取命令对象(同一事务): 方法={} 路径={}", signature, entityAttributeName);
                }
                return result;
            } catch (Throwable e) {
                log.error("LocalTaskMessageAop 处理失败: 方法={} 路径={} 错误={}", signature, entityAttributeName, e.getMessage(), e);
                throw e;
            }
        }

        // 无事务时，开启新事务并将方法执行与切面处理统一包裹
        try {
            return transactionTemplate.execute(status -> {
                try {
                    Object result = joinPoint.proceed();
                    TaskMessageEntityCommand command = resolveCommand(joinPoint, entityAttributeName);
                    if (command != null) {
                        log.info("LocalTaskMessageAop 提取到命令对象(新开事务): 方法={} 路径={} 命令={}", signature, entityAttributeName, command);
                        handleService.acceptTaskMessage(command);
                    } else {
                        log.warn("LocalTaskMessageAop 未能提取命令对象(新开事务): 方法={} 路径={}", signature, entityAttributeName);
                    }
                    return result;
                } catch (Throwable t) {
                    log.error("LocalTaskMessageAop 事务内处理失败: 方法={} 路径={} 错误={}", signature, entityAttributeName, t.getMessage(), t);
                    status.setRollbackOnly();
                    throw new RuntimeException(t);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() != null) throw e.getCause();
            throw e;
        }
    }

    /**
     * 根据 entityAttributeName 从方法入参中解析出 TaskMessageEntityCommand
     * 支持两种用法：
     * 1) "command" 直接为方法参数名，且参数类型为 TaskMessageEntityCommand
     * 2) "xxx.command" 首段为方法参数名，后续为对象属性路径，例如 xxx.getCommand()
     * 当 entityAttributeName 为空时，回退为从参数列表中直接寻找第一个 TaskMessageEntityCommand 类型的对象。
     */
    private TaskMessageEntityCommand resolveCommand(ProceedingJoinPoint joinPoint, String entityAttributeName) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 如果未配置路径，直接从参数中找第一个 TaskMessageEntityCommand
        if (entityAttributeName == null || entityAttributeName.trim().isEmpty()) {
            for (Object arg : args) {
                if (arg instanceof TaskMessageEntityCommand) {
                    return (TaskMessageEntityCommand) arg;
                }
            }
            return null;
        }

        String[] path = entityAttributeName.split("\\.");
        String paramName = path[0];

        // 通过参数名定位入参对象；优先使用反射参数名，其次回退到类型匹配
        Object root = findArgumentByNameOrType(method, args, paramName);
        if (root == null) {
            return null;
        }

        // 如果仅有首段且类型就是命令，直接返回
        if (path.length == 1) {
            return (root instanceof TaskMessageEntityCommand) ? (TaskMessageEntityCommand) root : null;
        }

        // 继续沿路径下钻对象属性
        Object current = root;
        for (int i = 1; i < path.length; i++) {
            if (current == null) return null;
            current = extractProperty(current, path[i]);
        }

        return (current instanceof TaskMessageEntityCommand) ? (TaskMessageEntityCommand) current : null;
    }

    /**
     * 根据参数名或类型在方法入参中查找对应对象。
     */
    private Object findArgumentByNameOrType(Method method, Object[] args, String paramName) {
        // 先尝试使用反射拿到参数名（JDK8+ 需 -parameters 或带调试信息）
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        if (parameters.length == args.length) {
            for (int i = 0; i < parameters.length; i++) {
                if (Objects.equals(parameters[i].getName(), paramName)) {
                    return args[i];
                }
            }
        }

        // 如果参数名无法匹配，尝试按类型回退（避免误匹配，仅在 paramName 看起来像类型时使用）
        if ("command".equals(paramName)) {
            for (Object arg : args) {
                if (arg instanceof TaskMessageEntityCommand) {
                    return arg;
                }
            }
        }

        return null;
    }

    /**
     * 从对象中按属性名提取属性值，优先调用 getter，然后尝试直接访问字段。
     */
    private Object extractProperty(Object target, String propertyName) {
        Class<?> clazz = target.getClass();
        String getterName = "get" + capitalize(propertyName);
        try {
            Method getter = clazz.getMethod(getterName);
            return getter.invoke(target);
        } catch (Exception ignore) {
            // ignore and fallback to field
        }

        try {
            Field field = clazz.getDeclaredField(propertyName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            return null;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) return s;
        return Character.toUpperCase(first) + s.substring(1);
    }

}
