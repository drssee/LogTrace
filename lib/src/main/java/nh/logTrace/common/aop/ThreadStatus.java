package nh.logTrace.common.aop;

public class ThreadStatus {

    private String transactionId;
    private int callDepth;

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
}
