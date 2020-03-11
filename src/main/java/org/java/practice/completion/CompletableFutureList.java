package org.java.practice.completion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletableFutureList {
    private static Logger LOGGER = LogManager.getLogger("org.java.practice.completion");
    private static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 10, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new ThreadFactory() {
        private String threadName = "Primary-Thread-%d";
        private AtomicInteger threadNumber = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, String.format(threadName, threadNumber.getAndIncrement()));
        }
    });

    public static void main(String[] args) throws Exception {
        final CompletableFuture<CallResponses.CallResponse2> completableFuture1 = new CompletableFuture<>();
        final CompletableFuture<CallResponses.CallResponseFinal> completableFuture2 = new CompletableFuture<>();
        CompletableFuture.anyOf(runTask1(completableFuture1), runTask2(completableFuture2)).join();
        if (completableFuture1.isCompletedExceptionally() && completableFuture2.isCompletedExceptionally()) {
            LOGGER.error("Both the calls failed");
            executorService.shutdownNow();
        } else if (!completableFuture1.isCompletedExceptionally()) {
            LOGGER.info("Task1 completed successfully", completableFuture1.get());
            completableFuture2.cancel(true);
        } else {
            LOGGER.info("Task2 completed successfully", completableFuture2.get());
            completableFuture1.cancel(true);
        }
        executorService.shutdownNow();
    }

    private static CompletableFuture<CallResponses.CallResponse2> runTask1(CompletableFuture<CallResponses.CallResponse2> completableFuture) {
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Task1: Call1");
            return new CallResponses.CallResponse1("Primary call-1 completed", null);
        }, executorService)
                .thenApplyAsync((t1c1Result) -> {
                    try {
                        Thread.sleep(5000);
                    } catch (Exception ex) {
                        LOGGER.error("Task1 call1 interrupted");
                    }
                    LOGGER.info("Task1: Call2");
                    return new CallResponses.CallResponse2(t1c1Result.response1 + ", Primary call-2 completed", null);
                }, executorService).whenComplete((t1c1c2Result, e) -> {
                    if (e != null || t1c1c2Result.exception != null)
                        completableFuture.completeExceptionally(t1c1c2Result != null ? t1c1c2Result.exception : e);
                    else
                        completableFuture.complete(t1c1c2Result);
                });
    }

    private static CompletableFuture<CallResponses.CallResponseFinal> runTask2(CompletableFuture<CallResponses.CallResponseFinal> completableFuture) {
        return CompletableFuture.supplyAsync(() -> {
            CallResponses.CallResponse1 response1;
            try {
                Thread.sleep(15000);
                LOGGER.info("Task2: Call1");
                throw new Exception("Primary call-1 completed with exception");
            } catch (Exception ex) {
                LOGGER.error("Exception in Task2 call1");
                response1 = new CallResponses.CallResponse1(null, ex);
            }
            return response1;
        }, executorService)
                .thenApplyAsync((t2c1Result) -> {
                    LOGGER.info("Task2: Call2");
                    if (t2c1Result.exception != null)
                        return new CallResponses.CallResponseFinal(null, t2c1Result.exception);
                    else {
                        String result2 = t2c1Result.response1 + ", Primary call-2 completed";
                        return new CallResponses.CallResponseFinal(result2, null);
                    }
                }, executorService).whenComplete((t2c1c2Result, e) -> {
                    if (e != null || t2c1c2Result.exception != null)
                        completableFuture.completeExceptionally(t2c1c2Result != null ? t2c1c2Result.exception : e);
                    else
                        completableFuture.complete(t2c1c2Result);
                });
    }
}
