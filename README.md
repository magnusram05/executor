# executor

This repository contains (will contains) code samples for various Java Executor Frameworks APIs.

## [completablefuture](https://github.com/magnusram05/executor/tree/master/src/main/java/org/java/practice/completablefuture) 

### ``CompletableFutureNormal.java`` 

* Execute a task (T1) > Once it completes, start the next task (T2)
* Mark the ``CompleteableFuture`` that represents the result as completed normally or exceptionally based on the status of T1 & T2
* Since both T1 & T2 completes normally, success is printed 

### ``CompletableFutureWithException.java``

* Execute a task (T1) > Once it completes, start the next task (T2)
* Mark the ``CompleteableFuture`` that represents the result as completed normally or exceptionally based on the status of T1 & T2
* In this example, T1 throws an exception, which is caught, wrapped and given to T2
* T2 checks if T1 has exception.  It does, so T2 without executing proceeds to return error response
* Since one of the task did not complete normally, the result bearing ``CompletetableFuture`` 
is completed exceptionally and error is printed

### ``CompletableFutureWithCustomExecutor.java``

* Same as ``CompletableFutureNormal.java`` but this one uses its own ThreadPool instead of the default 
``ForkJoinPool`` used by ``CompleteableFuture`` internally
* Customer ThreadPool is configured to have 
  * 5 core threads
  * 10 max. threads
  * 2 second as timeToLive for idle threads
  * A blocking queue of capacity 10
  * A threadFactory with custom thread name
  
### ``CompletableFutureList.java``  
* Same as ``CompletableFutureWithCustomExecutor.java`` but executes 2 independent tasks and waits for either one of them to complete 
(normally or exceptionally)
  * ``CompletableFuture.anyOf(runTask1(completableFuture1), runTask2(completableFuture2)).join();``
* The result bearing ``CompleteableFuture`` prints success message as T1 is made to complete normally
