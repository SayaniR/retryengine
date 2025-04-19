package com.retryengine.retry;

public interface RetryStrategy {

    long getNextDelay(int attempt);
    boolean shouldRetry(int attempt, Exception e);
}
