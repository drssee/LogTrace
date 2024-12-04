package nh.logTrace.admin;

import nh.logTrace.common.domain.LogEntity;
import nh.logTrace.save.db.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminPageService {

    // TODO 동적 config에 등록되어 사용될 빈들 주입시 생성자 주입으로 사용하도록 변경 필요
    @Autowired(required = false)
    private LogRepository logRepository;

    // 현재 저장 방식과 관계없이 파일과 db 모두 날짜 기준으로 뒤져서 확인해야함
    public List<String> findLogListByDateTime(LocalDateTime dateTime) {

        List<String> logs = new ArrayList<>();

        // 파일
        // ex) logTrace.2024-12-04-11.log
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));
        String logFileName = "logs/logTrace." + formattedDateTime + ".log";
        File logFile = new File(logFileName);
        if (logFile.exists() && logFile.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    logs.add("[FILE]" + line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // DB
        if (logRepository != null) {
            // 선택된 분을 between 조회 하기 위한 start, end 생성
            LocalDateTime start = dateTime.withSecond(0);
            LocalDateTime end = dateTime.withSecond(59);
            List<LogEntity> logEntities = logRepository.findLogsByCreatedAt(start, end);

            String line;
            for (LogEntity logEntity : logEntities) {
                line = "[DB]" +
                        logEntity.getCreatedAt().toString() + " " +
                        logEntity.getClass() + "." +
                        logEntity.getMethodName() +
                        " (" + logEntity.getResult() + ") " +
                        logEntity.getThrowableMessage();
                logs.add(line);
            }
        }

        return logs;
    }
}
