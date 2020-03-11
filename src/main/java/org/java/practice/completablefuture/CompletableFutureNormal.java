package org.java.practice.completablefuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureNormal {
    private static Logger LOGGER = LogManager.getLogger("org.java.practice.completablefuture");

    public static void main(String[] args) throws Exception {
        final CompletableFuture<CallResponses.CallResponseFinal> completableFuture = new CompletableFuture<>();
        final CompletableFuture<CallResponses.CallResponse2> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            return new CallResponses.CallResponse1("Primary call-1 completed", null);
        })
                .thenApplyAsync((r1) -> {
                    return new CallResponses.CallResponse2(r1.response1 + ", Primary call-2 completed", null);
                });
        completableFuture1.join();
        if (!completableFuture1.isCompletedExceptionally()) {
            LOGGER.info("Task completed successfully {}", completableFuture1.get());
        } else {
            LOGGER.error("Task failed");
        }
    }
}
