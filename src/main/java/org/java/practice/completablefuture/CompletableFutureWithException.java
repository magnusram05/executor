package org.java.practice.completablefuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureWithException {
    private static Logger LOGGER = LogManager.getLogger("org.java.practice.completablefuture");

    public static void main(String[] args) throws Exception {
        final CompletableFuture<CallResponses.CallResponseFinal> completableFuture = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
            CallResponses.CallResponse1 response1;
            try {
                throw new Exception("Primary call-1 completed with exception");
            } catch (Exception ex) {
                response1 = new CallResponses.CallResponse1(null, ex);
            }
            return response1;
        })
                .thenApplyAsync((r1) -> {
                    if (r1.exception != null)
                        return new CallResponses.CallResponse2(null, r1.exception);
                    else {
                        String result2 = r1.response1 + ", Primary call-2 completed";
                        return new CallResponses.CallResponse2(result2, null);
                    }
                })
                .whenComplete((finalResult, exception) -> {
                    CallResponses.CallResponseFinal callResponseFinal;
                    if (exception != null || finalResult.exception != null) {
                        completableFuture.completeExceptionally(exception != null ? exception : finalResult.exception);
                    } else {
                        callResponseFinal = new CallResponses.CallResponseFinal(finalResult, null);
                        completableFuture.complete(callResponseFinal);
                    }
                });
        if (!completableFuture.isCompletedExceptionally()) {
            LOGGER.info("Task completed successfully {}", completableFuture.get());
        } else {
            LOGGER.error("Task failed");
        }
    }
}
