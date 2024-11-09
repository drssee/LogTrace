package nh.logTrace.common.config;

import io.micrometer.common.util.StringUtils;
import nh.logTrace.admin.AdminPageController;
import nh.logTrace.alert.LogAlert;
import nh.logTrace.alert.mail.GoogleSendMail;
import nh.logTrace.alert.mail.MailLogAlert;
import nh.logTrace.alert.mail.SendMail;
import nh.logTrace.common.aop.LogTraceAdvice;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.file.FileLogSave;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
@ComponentScan(basePackages = "nh.logTrace.common")
public class Config implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(Config.class);
    private final ConfigProperties configProperties;

    public Config(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    /*
    로그 트레이스 빈 등록 시작
     */
    @Bean
    public Advisor logTrace(LogTraceAdvice logTraceAdvice) {
        logger.info("init logTrace");
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPattern(configProperties.getBasePackage() + ".*");

        // 메소드 호출을 가로챈뒤, loggingCall 실행
        MethodInterceptor interceptor = logTraceAdvice::loggingCall;

        return new DefaultPointcutAdvisor(pointcut, interceptor);
    }

    @Bean
    public LogTraceAdvice logTraceAdvice() {
        logger.info("init logTraceAdvice");
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
        logger.info("init adminPageController");
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
    public LogSave fileLogSave() {
        logger.info("init FileLogSave");
        return new FileLogSave();
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "DB")
    public LogSave dbLogSave() {
        logger.info("init DBLogSave");
        // TODO DB 저장 구현, springsession 테이블 생성해 저장시켜주는거 따라하면 될듯?
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
    public LogAlert mailLogAlert() {
        if (StringUtils.isEmpty(configProperties.getEmailId())) {
            throw new RuntimeException("email is empty");
        }
        logger.info("init MailLogAlert");
        return new MailLogAlert(configProperties.getEmailId(), configProperties.getEmailPwd(), googleSendMail());
    }

    @Bean
    public SendMail googleSendMail() {
        return new GoogleSendMail();
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MESSAGE")
    public LogAlert messageLogAlert() {
        logger.info("init MessageLogAlert");
        return null;
    }
    /*
    alert 빈 등록 종료
     */
}
