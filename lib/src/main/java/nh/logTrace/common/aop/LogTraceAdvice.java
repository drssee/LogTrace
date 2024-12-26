package nh.logTrace.common.aop;

import nh.logTrace.alert.LogAlert;
import nh.logTrace.alert.proxy.LogAlertProxy;
import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;
import nh.logTrace.save.LogSave;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LogTraceAdvice implements MethodInterceptor {

    private static final ThreadLocal<ThreadStatus> threadHolder = new ThreadLocal<>();

    private LogSave logSave;
    private LogAlertProxy logAlert;

    @Autowired
    public void setLogAlert(LogAlertProxy logAlert) {
        this.logAlert = logAlert;
    }

    @Autowired
    public void setLogSave(LogSave logSave) {
        this.logSave = logSave;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (threadHolder.get() == null) {
            threadHolder.set(new ThreadStatus(UUID.randomUUID().toString()));
        }
        ThreadStatus status = threadHolder.get();

        String className = invocation.getMethod().getDeclaringClass().getName();
        String methodName = invocation.getMethod().getName();
        Object[] args = invocation.getArguments();

        status.incrementCallDepth();
        String prefix = "-".repeat(status.getCallDepth() * 4);

        LogDto enteringLogDto = new LogDto.Builder()
                .setPrefix(prefix)
                .setTransactionId(status.getTransactionId())
                .setClassName(className)
                .setMethodName(methodName)
                .setArgs(args)
                .build();
        this.logSave.save(enteringLogDto);

        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable throwable) {
            LogDto exceptionLogDto = new LogDto.Builder()
                    .setPrefix(prefix)
                    .setTransactionId(status.getTransactionId())
                    .setClassName(className)
                    .setMethodName(methodName)
                    .setArgs(args)
                    .setThrowableMessage(throwable.getMessage())
                    .setThrowableStackTrace(throwable)
                    .build();
            this.logSave.save(exceptionLogDto);

            if (!status.isAlertException()) {
                this.logAlert.alert(status, exceptionLogDto);
                status.setAlertException(true);
            }

            throw throwable;
        }

        LogDto exitingLogDto = new LogDto.Builder()
                .setPrefix(prefix)
                .setTransactionId(status.getTransactionId())
                .setClassName(className)
                .setMethodName(methodName)
                .setResult(result)
                .build();
        this.logSave.save(exitingLogDto);

        status.decrementCallDepth();
        if (status.getCallDepth() == 0) {
            threadHolder.remove();
        }

        return result;
    }
}
