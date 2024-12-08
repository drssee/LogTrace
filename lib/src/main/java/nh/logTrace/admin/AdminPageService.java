package nh.logTrace.admin;

import nh.logTrace.common.domain.LogEntity;
import nh.logTrace.save.db.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// TODO 공통 외부 환경 변수 사용하여 수정할 필요 있음
@Service
public class AdminPageService {

    // TODO 동적 config에 등록되어 사용될 빈들 주입시 생성자 주입으로 사용하도록 변경 필요
    @Autowired(required = false)
    private LogRepository logRepository;

    private final String PATH = "logs";

    // 현재 저장 방식과 관계없이 파일과 db 모두 날짜 기준으로 뒤져서 확인해야함
    public List<String> findLogListByDateTime(LocalDateTime dateTime) {

        List<String> logs = new ArrayList<>();

        // 파일
        // ex) logTrace.2024-12-04-11.log
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));
        String logFileName = PATH + "/logTrace." + formattedDateTime + ".log";
        File logFile = new File(logFileName);
        if (logFile.exists() && logFile.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    LocalDateTime extractDateTime = extractDateTime(line);
                    if (extractDateTime != null &&
                            extractDateTime(line).getMinute() == dateTime.getMinute()) {
                        logs.add("[FILE]" + line);
                    }
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
                line = getLogString(logEntity);
                logs.add(line);
            }
        }

        return logs;
    }

    public List<String> findErrLogListByDate(LocalDate date) {

        List<String> logs = new ArrayList<>();

        // 파일
        // ex) logTrace.2024-12-04-11.log
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<File> logFiles = findLogFilesByDate(PATH, formattedDate);
        logFiles.forEach(logFile -> {

            if (logFile.exists() && logFile.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {

                        // line 의 마지막 값은 throwableMessage
                        if (!line.endsWith("null")) {
                            logs.add("[FILE]" + line);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // DB
        if (logRepository != null) {
            // 선택된 분을 between 조회 하기 위한 start, end 생성
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);
            List<LogEntity> logEntities = logRepository.findLogsByCreatedAt(start, end);

            String line;
            for (LogEntity logEntity : logEntities) {
                if (logEntity.getThrowableMessage() != null) {
                    line = getLogString(logEntity);
                    logs.add(line);
                }
            }
        }

        return logs;
    }

    private LocalDateTime extractDateTime(String line) {
        try {
            String timestamp = line.substring(0, 19);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(timestamp, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private List<File> findLogFilesByDate(String directoryPath, String targetDate) {
        File directory = new File(directoryPath);
        List<File> matchingFiles = new ArrayList<>();

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    // 파일 이름이 날짜 패턴을 포함하는지 확인
                    if (fileName.startsWith("logTrace." + targetDate)) {
                        matchingFiles.add(file);
                    }
                }
            }
        }
        return matchingFiles;
    }

    private String getLogString(LogEntity logEntity) {
        String log = "";

        if (logEntity.getArgs().length != 0) {
            StringBuilder args = new StringBuilder();
            for (int i = 0; i < logEntity.getArgs().length; i++) {
                if (i != 0) args.append(",");
                args.append(logEntity.getArgs()[i]);
            }

            log = "[DB]" +
                    logEntity.getCreatedAt().toString() + " " +
                    logEntity.getClass() + "." +
                    logEntity.getMethodName() +
                    " (" + args + ") " +
                    " return - " + logEntity.getResult() + " " +
                    logEntity.getThrowableMessage();
        } else {
            log = "[DB]" +
                    logEntity.getCreatedAt().toString() + " " +
                    logEntity.getClass() + "." +
                    logEntity.getMethodName() +
                    " return - " + logEntity.getResult() + " " +
                    logEntity.getThrowableMessage();
        }
        return log;
    }
}
