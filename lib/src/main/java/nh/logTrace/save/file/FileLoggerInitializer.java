package nh.logTrace.save.file;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class FileLoggerInitializer {

    public static void init() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);

        try (InputStream configStream = FileLoggerInitializer.class
                .getClassLoader().getResourceAsStream("logback-logtrace.xml")) {

            if (configStream != null) {
                configurator.doConfigure(configStream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 로깅 초기화 상태 출력 (문제 발생 시 도움)
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }
}
