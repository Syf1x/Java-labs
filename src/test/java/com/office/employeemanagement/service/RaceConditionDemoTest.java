package com.office.employeemanagement.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class RaceConditionDemoTest {

    private static final int THREADS = 60;
    private static final int INCREMENTS_PER_THREAD = 2000;
    private static final int EXPECTED = THREADS * INCREMENTS_PER_THREAD;

    @Test
    void unsafeCounter_losesIncrementsUnderConcurrency() throws InterruptedException {
        RequestCounterService service = new RequestCounterService();

        runConcurrently(service::incrementUnsafe);

        assertThat(service.getUnsafeCount())
                .as("небезопасный int++ под 60 потоками должен терять инкременты")
                .isLessThan(EXPECTED);
    }

    @Test
    void safeCounter_producesCorrectCountUnderConcurrency() throws InterruptedException {
        RequestCounterService service = new RequestCounterService();

        runConcurrently(service::incrementSafe);

        assertThat(service.getSafeCount())
                .as("AtomicInteger должен корректно посчитать все инкременты без потерь")
                .isEqualTo(EXPECTED);
    }

    private void runConcurrently(Runnable increment) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        CountDownLatch latch = new CountDownLatch(THREADS);
        try {
            for (int i = 0; i < THREADS; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                        increment.run();
                    }
                    latch.countDown();
                });
            }
            latch.await(30, TimeUnit.SECONDS);
        } finally {
            executor.shutdown();
        }
    }
}
