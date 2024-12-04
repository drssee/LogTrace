package nh.logTrace.alert.mail;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GoogleSendMail implements SendMail {

    private final String HOST = "smtp.gmail.com";
    private final String PORT = "587";

    private static final Logger logger = LoggerFactory.getLogger(GoogleSendMail.class);

    /*
    @Async
    1. 내부적으로 프록시 객체를 생성
    2. 해당 메서드를 별도의 스레드풀(ThreadPoolTaskExecutor)에서 실행
     */
    @Override
    @Async
    public void send(String emailId, String emailPwd, String subject, String body) {

        if (StringUtils.isEmpty(emailId)) {
            throw new RuntimeException("emailId is empty");
        }

        // 구글 메일 프로퍼티 설정
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        // id,pwd 인증
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailId, emailPwd);
            }
        });

        try {
            // 메일 본문 작성
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailId));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailId));
            message.setSubject(subject);
            message.setText(body);

            // 메일 전송
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
