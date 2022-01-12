package interview.assignments.zhanggang.support;

import interview.assignments.zhanggang.config.exception.base.SystemException;
import interview.assignments.zhanggang.config.properties.ShortenerConfig;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockHandler {
    private final ShortenerConfig.LockConfig lockConfig;
    private final Map<String, Lock> lockMap;

    public LockHandler(ShortenerConfig shortenerConfig) {
        this.lockConfig = shortenerConfig.getLockConfig();
        this.lockMap = new HashMap<>();
    }

    public <T> T lock(String lockId, Callable<? extends T> callable) {
        try {
            final Lock lock = getLock(lockId);
            if (lock.tryLock(lockConfig.getTimeout(), lockConfig.getTimeunit())) {
                try {
                    return callable.call();
                } finally {
                    lock.unlock();
                }
            }
            return null;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new SystemException(e);
        }
    }

    private Lock getLock(String id) {
        synchronized (lockMap) {
            return lockMap.computeIfAbsent(id, k -> new ReentrantLock());
        }
    }
}
