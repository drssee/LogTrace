package nh.logTrace.alert.proxy;

import nh.logTrace.alert.LogAlert;
import nh.logTrace.common.config.ConfigProperties;
import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;
import org.springframework.context.ApplicationContext;

/**
 * 동적 alert 구현 빈 전환을 위한 프록시 클래스
 */
public class LogAlertProxy {

    private LogAlert logAlert;
    private final ApplicationContext applicationContext;
    private final ConfigProperties configProperties;

    public LogAlertProxy(ApplicationContext applicationContext, ConfigProperties configProperties) {
        this.applicationContext = applicationContext;
        this.configProperties = configProperties;
        this.logAlert = (LogAlert) applicationContext.getBean(configProperties.getAlertBeanName());
    }

    public void alert(ThreadStatus threadStatus, LogDto logDto) {
        this.logAlert.alert(threadStatus, logDto);
    }
}
