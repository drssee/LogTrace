package nh.logTrace.save.file;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class FileLoggerInitializer {

    private static final Logger logger = LoggerFactory.getLogger(FileLoggerInitializer.class);

    public static void init() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        // 기존 로깅 설정 초기화(루트)
//        context.reset();

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
