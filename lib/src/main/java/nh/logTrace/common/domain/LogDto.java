package nh.logTrace.common.domain;

import java.time.LocalDateTime;
import java.util.Arrays;

public class LogDto {

    private Long id;
    private String transactionId;
    private String prefix;
    private String className;
    private String methodName;
    private Object[] args;
    private Object result;
    private String throwableMessage;
    private Throwable throwableStackTrace;
    private LocalDateTime createdAt;

    private LogDto(Builder builder) {
        this.args = builder.args;
        this.className = builder.className;
        this.id = builder.id;
        this.methodName = builder.methodName;
        this.prefix = builder.prefix;
        this.result = builder.result;
        this.throwableMessage = builder.throwableMessage;
        this.throwableStackTrace = builder.throwableStackTrace;
        this.transactionId = builder.transactionId;
        this.createdAt = LocalDateTime.now();
    }

    public Object[] getArgs() {
        return args;
    }

    public String getClassName() {
        return className;
    }

    public Long getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getPrefix() {
        return prefix;
    }

    public Object getResult() {
        return result;
    }

    public String getThrowableMessage() {
        return throwableMessage;
    }

    public Throwable getThrowableStackTrace() {
        return throwableStackTrace;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        private Long id;
        private String transactionId;
        private String prefix;
        private String className;
        private String methodName;
        private Object[] args;
        private Object result;
        private String throwableMessage;
        private Throwable throwableStackTrace;

        public Builder setArgs(Object[] args) {
            this.args = args;
            return this;
        }

        public Builder setClassName(String className) {
            this.className = className;
            return this;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder setResult(Object result) {
            this.result = result;
            return this;
        }

        public Builder setThrowableMessage(String throwableMessage) {
            this.throwableMessage = throwableMessage;
            return this;
        }

        public Builder setThrowableStackTrace(Throwable throwableStackTrace) {
            this.throwableStackTrace = throwableStackTrace;
            return this;
        }

        public Builder setTransactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public LogDto build() {
            return new LogDto(this);
        }
    }
}
