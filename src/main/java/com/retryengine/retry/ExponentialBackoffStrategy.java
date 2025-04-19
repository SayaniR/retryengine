package com.retryengine.retry;

public class ExponentialBackoffStrategy implements RetryStrategy {

    private final long baseDelay;
    private final int maxAttempts;

    public ExponentialBackoffStrategy(long baseDelay, int maxAttempts) {
        this.baseDelay = baseDelay;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public long getNextDelay(int attempt) {
        return (long) (baseDelay * Math.pow(2, attempt - 1));
    }

    @Override
    public boolean shouldRetry(int attempt, Exception e) {
        return attempt < maxAttempts;
    }
}
