package nh.logTrace.alert.mail;

import nh.logTrace.alert.LogAlert;
import nh.logTrace.common.domain.LogDto;
import nh.logTrace.common.domain.ThreadStatus;

public class MailLogAlert implements LogAlert {

    private final String emailId;
    private final String emailPwd;
    private final SendMail sendMail;

    public MailLogAlert(String emailId, String emailPwd, SendMail sendMail) {
        this.emailId = emailId;
        this.emailPwd = emailPwd;
        this.sendMail = sendMail;
    }

    @Override
    public void alert(ThreadStatus threadStatus, LogDto logDto) {
        sendMail.send(
                emailId,
                emailPwd,
                threadStatus.getTransactionId() + " - " + logDto.getThrowableMessage() + "(" + logDto.getCreatedAt() + ")",
                logDto.getThrowableStackTrace().toString()
        );
    }
}
