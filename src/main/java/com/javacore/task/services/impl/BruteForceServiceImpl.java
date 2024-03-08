package com.javacore.task.services.impl;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.javacore.task.services.BruteForceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BruteForceServiceImpl implements BruteForceService {
    private final LoadingCache<String, Integer> attemptsCache;

    @Value("${brute-force.attempt.count}")
    private int maxAttempt;

    public BruteForceServiceImpl() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(final String key) {
                        return 0;
                    }
                });
    }

    @Override
    public void loginFailed(String key) {
        if (key == null) {
            return;
        }
        int attempts;

        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }

        attempts++;
        attemptsCache.put(key, attempts);
    }

    @Override
    public void resetCache(String key) {
        attemptsCache.put(key, 0);
    }

    @Override
    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= maxAttempt;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
