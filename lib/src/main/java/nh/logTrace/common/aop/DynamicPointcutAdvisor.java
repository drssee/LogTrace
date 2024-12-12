package nh.logTrace.common.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 스프링AOP에서 Advisor 생성 후, 포인트컷 변경이 안되는 문제 해결 위함
 */
public class DynamicPointcutAdvisor extends AbstractPointcutAdvisor {

    private final JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
    private final Advice advice;
    private String basePackage;
    private ConfigurableApplicationContext applicationContext;

    public DynamicPointcutAdvisor(Advice advice, String basePackage, ConfigurableApplicationContext applicationContext) {
        this.advice = advice;
        this.updateBasePackage(basePackage);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void updateBasePackage(String basePackage) {
        this.basePackage = basePackage;
        pointcut.setPattern(this.basePackage + ".*");
    }

//    @Autowired
//    private ConfigurableApplicationContext applicationContext;
//
//    public void updateBasePackage(String newBasePackage) {
//        logTrace.updateBasePackage(newBasePackage);
//
//        // 컨텍스트에서 기존 Bean을 재등록
//        applicationContext.getBeanFactory().destroySingleton("logTraceAdvisor");
//        applicationContext.getBeanFactory().registerSingleton("logTraceAdvisor", logTrace);
//    }

}
