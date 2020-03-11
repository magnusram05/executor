package org.java.practice.completion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletableFutureWithCustomExecutor {
    static Logger LOGGER = LogManager.getLogger("org.java.practice.completion");

    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 10, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new ThreadFactory() {
        private String threadName = "Primary-Thread-%d";
        private AtomicInteger threadNumber = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, String.format(threadName, threadNumber.getAndIncrement()));
        }
    });

    public static void main(String[] args) throws Exception {
        final CompletableFuture<CallResponses.CallResponse2> completableFuture =
        CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Task1 completed");
            return new CallResponses.CallResponse1("Primary call-1 completed", null);
        }, executorService)
                .thenApplyAsync((r1) -> {
                    LOGGER.info("Task2 completed");
                    return new CallResponses.CallResponse2(r1.response1 + ", Primary call-2 completed", null);
                }, executorService);
        completableFuture.join();
        if (!completableFuture.isCompletedExceptionally()) {
            LOGGER.info("Task completed successfully {}", completableFuture.get());
        } else {
            LOGGER.error("Task failed");
        }
        executorService.shutdown();
    }


}
