package com.javacore.task.services;
public interface BruteForceService {
    void loginFailed(String key);
    void resetCache(String key);
    boolean isBlocked(String key);
}
