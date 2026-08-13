package com.office.employeemanagement.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class RequestCounterService {

    private int unsafeCount = 0;
    private final AtomicInteger safeCount = new AtomicInteger(0);

    public void incrementUnsafe() {
        unsafeCount++;
    }

    public void incrementSafe() {
        safeCount.incrementAndGet();
    }

    public int getUnsafeCount() {
        return unsafeCount;
    }

    public int getSafeCount() {
        return safeCount.get();
    }

    public void reset() {
        unsafeCount = 0;
        safeCount.set(0);
    }
}
