package nh.logTrace.alert.proxy;

import nh.logTrace.alert.LogAlert;
import nh.logTrace.common.config.ConfigProperties;
import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 동적 alert 구현 빈 전환을 위한 프록시 클래스
 */
public class LogAlertProxy {

    private LogAlert logAlert;
    private final ApplicationContext applicationContext;
    private final ConfigProperties configProperties;
    private Map<String, String> beanNames = new HashMap<String, String>();

    public LogAlertProxy(ApplicationContext applicationContext, ConfigProperties configProperties) {
        this.applicationContext = applicationContext;
        this.configProperties = configProperties;
        this.logAlert = (LogAlert) applicationContext.getBean(configProperties.getAlertBeanName());
        this.beanNames.put("MESSAGE", "messageLogAlert");
        this.beanNames.put("MAIL", "mailLogAlert");
    }

    public void alert(ThreadStatus threadStatus, LogDto logDto) {
        this.logAlert.alert(threadStatus, logDto);
    }

    public LogAlert getTarget() {
        return this.logAlert;
    }

    public void changeLogAlert() {
        this.logAlert = applicationContext.getBean(beanNames.get(configProperties.getAlert()), LogAlert.class);
    }
}
