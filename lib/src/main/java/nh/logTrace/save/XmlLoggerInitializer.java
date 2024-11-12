package nh.logTrace.save;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class XmlLoggerInitializer {

    /**
     * 사용 어플리케이션과 별개로, 라이브러리 자체 로그 설정
     */
    public static void init(String xmlFileName) {
        // 로거 관리 context
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 로거 설정을 위한 configurator
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);

        // 해당 xml 파일에서 설정을 가져와 적용
        try (InputStream configStream = XmlLoggerInitializer.class
                .getClassLoader().getResourceAsStream(xmlFileName)) {

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
