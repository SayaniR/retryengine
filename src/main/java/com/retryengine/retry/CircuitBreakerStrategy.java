package com.retryengine.retry;

public class CircuitBreakerStrategy implements RetryStrategy{

    private final int maxFailures;
    private final long openTimeoutMillis;
    private long lastFailureTime = 0;
    private int failureCount = 0;

    public CircuitBreakerStrategy(int maxFailures, long openTimeoutMillis) {
        this.maxFailures = maxFailures;
        this.openTimeoutMillis = openTimeoutMillis;
    }

    @Override
    public long getNextDelay(int attempt) {
        return 1000;
    }

    @Override
    public boolean shouldRetry(int attempt, Exception e) {
        long currentTime = System.currentTimeMillis();
        if (failureCount >= maxFailures) {
            if (currentTime - lastFailureTime > openTimeoutMillis) {
                failureCount = 0;
                return true;
            }
            return false;
        }
        if (e != null) {
            failureCount++;
            lastFailureTime = currentTime;
        }
        return true;
    }
}
