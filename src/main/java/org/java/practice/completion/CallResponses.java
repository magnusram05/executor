package org.java.practice.completion;

public class CallResponses {
    static class CallResponse1 {
        final Object response1;
        final Throwable exception;

        CallResponse1(Object response, Throwable exception) {
            this.response1 = response;
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "CallResponse1{" +
                    "response=" + response1 +
                    ", exception=" + exception +
                    '}';
        }
    }

    static class CallResponse2 {
        final Object response2;
        final Throwable exception;

        CallResponse2(Object response, Throwable exception) {
            this.response2 = response;
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "CallResponse2{" +
                    "response=" + response2 +
                    ", exception=" + exception +
                    '}';
        }
    }

    static class CallResponseFinal {
        final Object response2;
        final Throwable exception;

        CallResponseFinal(Object response, Throwable exception) {
            this.response2 = response;
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "CallResponseFinal{" +
                    "response=" + response2 +
                    ", exception=" + exception +
                    '}';
        }
    }
}
