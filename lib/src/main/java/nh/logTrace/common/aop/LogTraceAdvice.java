package nh.logTrace.common.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Aspect
public class LogTraceAdvice {

    private static final Logger logger = LoggerFactory.getLogger(LogTraceAdvice.class);

    // 쓰레드마다 요청 단위로 관리하는 ThreadLocal(쓰레드별 저장소)
    private static final ThreadLocal<ThreadStatus> threadHolder = new ThreadLocal<>();

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

        // 메서드 호출 시작 로그
        logger.info("{}[TransactionId: {}] Entering {}.{}() with arguments: {}",
                prefix, status.getTransactionId(), className, methodName, args);

        Object result;
        try {
            result = invocation.proceed(); // 실제 메서드 호출
        } catch (Throwable throwable) {
            // 예외 발생 시 로그 기록
            logger.error("{}[TransactionId: {}] Exception in {}.{}() with cause: {}",
                    prefix, status.getTransactionId(), className, methodName, throwable.getMessage(), throwable);
            throw throwable;
        }

        // 메서드 호출 종료 로그
        logger.info("{}[TransactionId: {}] Exiting {}.{}() with result: {}",
                prefix, status.getTransactionId(), className, methodName, result);

        status.decrementCallDepth();
        if (status.getCallDepth() == 0) {
            threadHolder.remove();
        }

        return result;
    }
}
