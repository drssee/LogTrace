package nh.logTrace.common.config;

import nh.logTrace.admin.AdminPageController;
import nh.logTrace.alert.LogAlert;
import nh.logTrace.common.aop.LogTraceAdvice;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.file.FileLoggerInitializer;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "nh.logTrace.common")
public class Config implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(Config.class);
    private final ConfigProperties configProperties;

    public Config(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }
    // TODO nh.logTrace 경로도 로그추적되게하기 + save 구
    /*
    로그 트레이스 빈 등록 시작
     */
    @Bean
    public Advisor logTrace(LogTraceAdvice logTraceAdvice) {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPatterns(configProperties.getBasePackage() + ".*", "nh.logTrace.*");

        // 메소드 호출을 가로챈뒤, loggingCall 실행
        MethodInterceptor interceptor = logTraceAdvice::loggingCall;

        return new DefaultPointcutAdvisor(pointcut, interceptor);
    }

    @Bean
    public LogTraceAdvice logTraceAdvice() {
        return new LogTraceAdvice();
    }
    /*
    로그 트레이스 빈 등록 종료
     */

    /*
    admin 페이지 등록 시작
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(configProperties.getAdminUrl())
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    public AdminPageController adminPageController() {
        return new AdminPageController();
    }
    /*
    admin 페이지 등록 종료
     */

    /*
    save 빈 등록 시작
     */
    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "FILE")
    public LogSave FileLogRepository() {
        FileLoggerInitializer.init();
        return null;
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "DB")
    public LogSave DBLogRepository() {
        return null;
    }
    /*
    save 빈 등록 종료
     */

    /*
    alert 빈 등록 시작
     */
    @Bean
    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MAIL")
    public LogAlert MailLogAlert() {
        return null;
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MESSAGE")
    public LogAlert MessageLogAlert() {
        return null;
    }
    /*
    alert 빈 등록 종료
     */
}
