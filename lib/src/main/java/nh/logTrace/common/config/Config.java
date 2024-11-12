package nh.logTrace.common.config;

import io.micrometer.common.util.StringUtils;
import nh.logTrace.admin.AdminPageController;
import nh.logTrace.alert.LogAlert;
import nh.logTrace.alert.mail.GoogleSendMail;
import nh.logTrace.alert.mail.MailLogAlert;
import nh.logTrace.alert.mail.SendMail;
import nh.logTrace.common.aop.LogTraceAdvice;
import nh.logTrace.save.LogSave;
import nh.logTrace.save.db.DbLogSave;
import nh.logTrace.save.db.repository.JdbcLogRepository;
import nh.logTrace.save.db.repository.JpaLogRepository;
import nh.logTrace.save.db.repository.LogRepository;
import nh.logTrace.save.db.repository.MybatisLogRepository;
import nh.logTrace.save.file.FileLogSave;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
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
    public LogSave dbLogSave(LogRepository logRepository) {
        logger.info("init DBLogSave");
        return new DbLogSave(logRepository);
    }

    @Bean
    @ConditionalOnBean(SqlSessionFactory.class)
    public LogRepository mybatisLogRepository() {
        logger.info("init MybatisLogRepository");
        return new MybatisLogRepository();
    }

    @Bean
    @ConditionalOnBean(EntityManagerFactoryBuilder.class)
    public LogRepository jpaLogRepository() {
        logger.info("init JpaLogRepository");
        return new JpaLogRepository();
    }

    @Bean
    @ConditionalOnMissingBean({SqlSessionTemplate.class, EntityManagerFactoryBuilder.class})
    public LogRepository JdbcLogRepository() {
        logger.info("init JdbcLogRepository");
        return new JdbcLogRepository();
    }

    // TODO DB어댑터 등록
    // 1. 어플리케이션 라이브러리에 사용중인 DB드라이버 빈 조회 -> 없으면 예외
    // 2. 사용중인 DB와 매칭되는 ddl,sql 가져와 사용
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
    @ConditionalOnProperty(name = "logtrace.alert", havingValue = "MESSAGE")
    public LogAlert messageLogAlert() {
        logger.info("init MessageLogAlert");
        return null;
    }

    // TODO DEFAULT save, alert 빈 추가 필요

    /*
    alert 빈 등록 종료
     */
}
