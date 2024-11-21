package nh.logTrace.common.aop;

import nh.logTrace.alert.LogAlert;
import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;
import nh.logTrace.save.LogSave;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Aspect
public class LogTraceAdvice {

    // 쓰레드마다 요청 단위로 관리하는 ThreadLocal(쓰레드별 저장소)
    private static final ThreadLocal<ThreadStatus> threadHolder = new ThreadLocal<>();

    private LogSave logSave;
    private LogAlert logAlert;

    @Autowired(required = false)
    public void setLogAlert(LogAlert logAlert) {
        this.logAlert = logAlert;
    }

    @Autowired(required = false)
    public void setLogSave(LogSave logSave) {
        this.logSave = logSave;
    }

    public Object loggingCall(MethodInvocation invocation) throws Throwable {

        // 처음 접근하는 쓰레드인 경우 초기화
        if (threadHolder.get() == null) {
            threadHolder.set(new ThreadStatus(UUID.randomUUID().toString()));
        }
        ThreadStatus status = threadHolder.get();

        // MethodInvocation에서 클래스 이름, 메서드 이름, 파라미터를 직접 추출
        String className = invocation.getMethod().getDeclaringClass().getName();
        String methodName = invocation.getMethod().getName();
        Object[] args = invocation.getArguments();

        // 실행 depth 증가
        status.incrementCallDepth();
        String prefix = "-".repeat(status.getCallDepth() * 4);

        // 정상 save
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
            result = invocation.proceed(); // 실제 메서드 호출
        } catch (Throwable throwable) {
            // 예외 save, alert
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

        // 정상 save
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
