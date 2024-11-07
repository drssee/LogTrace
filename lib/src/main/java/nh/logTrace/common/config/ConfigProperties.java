package nh.logTrace.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@ConfigurationProperties(prefix = "logtrace")
public class ConfigProperties {

    private final String[] SAVE_METHODS = {"FILE", "DB"};
    private final String[] ALERT_METHODS = {"MAIL", "MESSAGE"};
    private String DEFAULT_ADMIN_URL = "/log/**";
    private final int DEFAULT = 0;

    private String save;
    private String alert;
    private String adminUrl;
    private String basePackage;

    public ConfigProperties() {
        this.save = SAVE_METHODS[DEFAULT];
        this.alert = ALERT_METHODS[DEFAULT];
        this.adminUrl = DEFAULT_ADMIN_URL;
        this.basePackage = "";
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
}
