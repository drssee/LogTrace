package nh.logTrace.alert.mail;

public interface SendMail {

    void send(String emailId, String emailPwd, String subject, String body);
}
