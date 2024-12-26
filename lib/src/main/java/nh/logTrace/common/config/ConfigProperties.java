package nh.logTrace.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "logtrace")
public class ConfigProperties {

    private final String[] SAVE_METHODS = {"FILE", "DB"};
    private final String[] ALERT_METHODS = {"MESSAGE", "MAIL"};
    private final String DEFAULT_ADMIN_URL = "/log/**";
    private final String DEFAULT_BASE_PACKAGE = "nh.logTrace";
    private final int DEFAULT_INDEX = 0;
    private final String ALERT_MESSAGE_BEAN_NAME = "messageLogAlert";
    private final String MAIL_MESSAGE_BEAN_NAME = "mailLogAlert";

    private String save;
    private String alert;
    private String adminUrl;
    private String basePackage;
    private String emailId;
    private String emailPwd;

    private Map<String, String> alertName = new HashMap<>();

    public ConfigProperties() {
        this.save = SAVE_METHODS[DEFAULT_INDEX];
        this.alert = ALERT_METHODS[DEFAULT_INDEX];
        this.adminUrl = DEFAULT_ADMIN_URL;
        this.basePackage = DEFAULT_BASE_PACKAGE;
        alertName.put("MESSAGE", ALERT_MESSAGE_BEAN_NAME);
        alertName.put("MAIL", MAIL_MESSAGE_BEAN_NAME);
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        boolean hasAlert = Arrays.asList(ALERT_METHODS).contains(alert);
        if (hasAlert) {
            this.alert = alert;
        }
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        boolean hasSave = Arrays.asList(SAVE_METHODS).contains(save);
        if (hasSave) {
            this.save = save;
        }
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailPwd() {
        return emailPwd;
    }

    public void setEmailPwd(String emailPwd) {
        this.emailPwd = emailPwd;
    }

    public String getAlertBeanName() {
        return this.alertName.get(this.alert);
    }

    public String[] getALERT_METHODS() {
        return ALERT_METHODS;
    }
}
