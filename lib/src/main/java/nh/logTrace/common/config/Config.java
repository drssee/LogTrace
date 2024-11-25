package nh.logTrace.common.config;

import nh.logTrace.admin.AdminPageController;
import nh.logTrace.alert.LogAlert;
import nh.logTrace.alert.MESSAGE.MessageLogAlert;
import nh.logTrace.alert.mail.GoogleSendMail;
import nh.logTrace.alert.mail.MailLogAlert;
import nh.logTrace.alert.mail.SendMail;
import nh.logTrace.common.aop.LogTraceAdvice;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.db.DbAdapter;
import nh.logTrace.save.db.DbLogSave;
import nh.logTrace.save.db.repository.JdbcLogRepository;
import nh.logTrace.save.db.repository.LogRepository;
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

import javax.sql.DataSource;

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
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "FILE", matchIfMissing = true)
    public LogSave fileLogSave() {
        logger.info("init FileLogSave");
        return new FileLogSave();
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "DB")
    public LogSave dbLogSave(LogRepository logRepository) {
        logger.info("init DBLogSave");
        return new DbLogSave(logRepository);
    }

    // TODO 사용 dataSource 빈이 여러개일 경우 문제가 생김, 해결방법 생각해보기
    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "DB")
    public DbAdapter dbAdapter(DataSource dataSource) {
        logger.info("init DBAdapter");
        return new DbAdapter(dataSource);
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "DB")
    public LogRepository JdbcLogRepository(DataSource dataSource) {
        logger.info("init JdbcLogRepository");
        return new JdbcLogRepository(dataSource, dbAdapter(dataSource));
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
        logger.info("init MailLogAlert");
        return new MailLogAlert(configProperties.getEmailId(), configProperties.getEmailPwd(), googleSendMail());
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MAIL")
    public SendMail googleSendMail() {
        logger.info("init GoogleSendMail");
        return new GoogleSendMail();
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MESSAGE", matchIfMissing = true)
    public LogAlert messageLogAlert() {
        logger.info("init MessageLogAlert");
        return new MessageLogAlert();
    }

    /*
    alert 빈 등록 종료
     */
}
