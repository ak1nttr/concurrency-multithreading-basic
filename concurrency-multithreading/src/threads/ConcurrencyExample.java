/*
Created by Akın Tatar
- Project name :concurrency-multithreading
- Date :11.10.2023
- School ID :                       (optional)
 */
package threads;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.*;

public class ConcurrencyExample {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<Integer>> futureList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Callable<Integer> task = new RandomNumberGenerator();
            Future<Integer> future = executorService.submit(task);
            futureList.add(future);
        }

        int sum = 0;
        for (Future<Integer> future : futureList) {
            try {
                int result = future.get();
                System.out.println("Rastgele sayı üretildi: " + result);
                sum += result;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Toplam: " + sum);
        executorService.shutdown();
    }
}

class RandomNumberGenerator implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        // Görevin biraz süre alması için bir bekleme ekleyebilirsiniz
        Thread.sleep(1500);
        System.out.println("üretilen random sayı: "+randomNumber);
        return randomNumber;
    }
}class ImprovedConcurrencyExample {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);


        List<CompletableFuture<Integer>> futures = IntStream.range(0, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(ImprovedConcurrencyExample::generateRandomNumber, executorService))
                .collect(Collectors.toList());


        int sum = futures.stream()
                .map(CompletableFuture::join)
                .peek(result -> System.out.println("Görev tamamlandı. Sonuç: " + result))
                .reduce(0, Integer::sum);

        System.out.println("Toplam: " + sum);
        executorService.shutdown();
    }

    private static int generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        // Görevin biraz süre alması için bir bekleme ekleyebilirsiniz
        sleep(1000);
        return randomNumber;
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}

