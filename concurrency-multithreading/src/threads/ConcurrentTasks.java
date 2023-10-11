/*
Created by Akın Tatar
- Project name :concurrency-multithreading
- Date :11.10.2023
- School ID :                       (optional)
 */
package threads;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentTasks {


    public static void main(String[] args) {

        Runnable run = () -> System.out.println("ben bir runnable programım");
      // Runnable run1 = () -> {
      //     try {
      //         new RandomNumberGenerator().call();
      //     } catch (Exception e) {
      //         throw new RuntimeException(e);
      //     }
      // };
        Thread thread = new Thread(run);
        thread.run();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Runnable> runnableList = Arrays.asList(
                () -> System.out.println("thread 1"),
                () -> System.out.println("thread 2"),
                () -> System.out.println("thread 3"),
                () -> System.out.println("thread 4"),
                () -> System.out.println("thread 5"),
                () -> System.out.println("thread 6"),
                () -> System.out.println("thread 7"),
                () -> System.out.println("thread 8"),
                () -> System.out.println("thread 9"),
                () -> System.out.println("thread 10")
        );
       List<Callable<Integer>> callableList = Arrays.asList(
               () -> {System.out.print("callable1 -> \n") ; return new RandomNumberGenerator().call();},
               () -> {System.out.print("callable2 -> \n") ; return new RandomNumberGenerator().call();},
               () -> {System.out.print("callable3 -> \n") ; return new RandomNumberGenerator().call();},
               () -> {System.out.print("callable4 -> \n") ; return new RandomNumberGenerator().call();}
       );
        try {
           List<Future<Integer>> results = executorService.invokeAll(callableList);
           results.stream().forEach(r -> {
               try {
                   System.out.println(r.get());
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               } catch (ExecutionException e) {
                   throw new RuntimeException(e);
               }
           });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0 ; i<runnableList.size();i++){
            executorService.submit(runnableList.get(i));
        }
        executorService.shutdown();

    }

}
