package nh.logTrace.common.domain;

public class ThreadStatus {

    private String transactionId;
    private int callDepth;
    private boolean isAlertException;

    public ThreadStatus(String transactionId) {
        this.transactionId = transactionId;
        this.callDepth = 0;
    }

    public int getCallDepth() {
        return callDepth;
    }
    public String getTransactionId() {
        return transactionId;
    }

    public void incrementCallDepth() {
        this.callDepth++;
    }

    public void decrementCallDepth() {
        this.callDepth--;
    }

    public boolean isAlertException() {
        return isAlertException;
    }

    public void setAlertException(boolean alertException) {
        isAlertException = alertException;
    }
}
