package nh.logTrace.common.config;

import nh.logTrace.admin.AdminPageController;
import nh.logTrace.admin.AdminPageService;
import nh.logTrace.alert.LogAlert;
import nh.logTrace.alert.MESSAGE.MessageLogAlert;
import nh.logTrace.alert.mail.GoogleSendMail;
import nh.logTrace.alert.mail.MailLogAlert;
import nh.logTrace.alert.mail.SendMail;
import nh.logTrace.alert.proxy.LogAlertProxy;
import nh.logTrace.common.aop.LogTraceAdvice;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.db.DbAdapter;
import nh.logTrace.save.db.DbLogSave;
import nh.logTrace.save.db.repository.JdbcLogRepository;
import nh.logTrace.save.file.FileLogSave;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
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

    private final ConfigProperties configProperties;
    private final ApplicationContext applicationContext;

    public Config(ConfigProperties configProperties, ApplicationContext applicationContext) {
        this.configProperties = configProperties;
        this.applicationContext = applicationContext;
    }

    /*
    로그 트레이스 빈 등록 시작
     */
    @Bean
    public Advisor logTrace(LogTraceAdvice logTraceAdvice) {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPattern(configProperties.getBasePackage() + ".*");

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
        return new AdminPageController(adminPageService(), configProperties);
    }

    @Bean
    public AdminPageService adminPageService() {
        return new AdminPageService();
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
        return new FileLogSave();
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "DB")
    public LogSave dbLogSave(JdbcLogRepository jdbcLogRepository) {
        return new DbLogSave(jdbcLogRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "DB")
    public DbAdapter dbAdapter(DataSource dataSource) {
        return new DbAdapter(dataSource);
    }

    @Bean
    @ConditionalOnProperty(name = "logtrace.save", havingValue = "DB")
    public JdbcLogRepository jdbcLogRepository(DataSource dataSource) {
        return new JdbcLogRepository(dataSource, dbAdapter(dataSource));
    }
    /*
    save 빈 등록 종료
     */

    /*
    alert 빈 등록 시작
     */
    @Bean
//    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MAIL")
    public LogAlert mailLogAlert() {
        return new MailLogAlert(configProperties.getEmailId(), configProperties.getEmailPwd(), googleSendMail());
    }

    @Bean
//    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MAIL")
    public SendMail googleSendMail() {
        return new GoogleSendMail();
    }

    @Bean
//    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MESSAGE", matchIfMissing = true)
    public LogAlert messageLogAlert() {
        return new MessageLogAlert();
    }

    @Bean
    public LogAlertProxy logAlertProxy() {
        return new LogAlertProxy(applicationContext, configProperties);
    }
    /*
    alert 빈 등록 종료
     */
}
