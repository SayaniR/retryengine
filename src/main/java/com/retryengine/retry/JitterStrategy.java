package com.retryengine.retry;

import java.util.Random;

public class JitterStrategy implements RetryStrategy{
    private final long baseDelay;
    private final long jitter;
    private final int maxAttempts;
    private final Random random = new Random();

    public JitterStrategy(long baseDelay, long jitter, int maxAttempts) {
        this.baseDelay = baseDelay;
        this.jitter = jitter;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public long getNextDelay(int attempt) {
        long jitterValue = (long) (random.nextDouble() * jitter);
        return baseDelay + jitterValue;
    }

    @Override
    public boolean shouldRetry(int attempt, Exception e) {
        return attempt < maxAttempts;
    }
}
