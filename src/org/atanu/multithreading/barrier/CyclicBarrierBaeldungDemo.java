package org.atanu.multithreading.barrier;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CyclicBarrier.html

//Constructor Summary
//Constructor 1
//CyclicBarrier(int parties)
//Creates a new CyclicBarrier that will trip when the given number of parties (threads) are waiting upon it,
//and does not perform a predefined action when the barrier is tripped.

//Constructor 2
//CyclicBarrier(int parties, Runnable barrierAction)
//Creates a new CyclicBarrier that will trip when the given number of parties (threads) are waiting upon it,
// and which will execute the given barrier action when the barrier is tripped, performed by the last thread entering the barrier.


//https://www.baeldung.com/java-cyclic-barrier
//https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-concurrency-advanced/src/main/java/com/baeldung/concurrent/cyclicbarrier/CyclicBarrierDemo.java
public class CyclicBarrierBaeldungDemo {

    public static void main(String[] args) {
        new CyclicBarrierBaeldungDemo().runSimulation(5, 3);
    }

    private static class NumberCruncherTask implements Runnable{

        private CyclicBarrier barrier;
        private int NUM_PARTIAL_RESULTS;
        private List<List<Integer>> partialResults;

        public NumberCruncherTask(CyclicBarrier barrier, int NUM_PARTIAL_RESULTS, List<List<Integer>> partialResults) {
            this.barrier = barrier;
            this.NUM_PARTIAL_RESULTS = NUM_PARTIAL_RESULTS;
            this.partialResults = partialResults;
        }


        @Override
        public void run() {
            String thisThreadName = Thread.currentThread().getName();
            List<Integer> partialResult = new ArrayList<>();
            for(int i = 0; i < NUM_PARTIAL_RESULTS; i++){
                int num = new Random().nextInt(10);
                System.out.println(thisThreadName + ": Crunching some numbers! Final result - " + num);
                partialResult.add(num);
            }
            //Add to the partialResults
            partialResults.add(partialResult);

            try {
                System.out.println(thisThreadName + " waiting for others to reach barrier.");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Aggregator implements Runnable{

        private List<List<Integer>> partialResults;
        private int NUM_WORKERS;
        private int NUM_PARTIAL_RESULTS;

        public Aggregator(int NUM_WORKERS, int NUM_PARTIAL_RESULTS, List<List<Integer>> partialResults) {
            this.NUM_WORKERS = NUM_WORKERS;
            this.NUM_PARTIAL_RESULTS = NUM_PARTIAL_RESULTS;
            this.partialResults = partialResults;
        }

        @Override
        public void run() {
            String thisThreadName = Thread.currentThread().getName();
            System.out.println(thisThreadName + ": Computing final sum of " + NUM_WORKERS + " workers, having " + NUM_PARTIAL_RESULTS + " results each.");
            int sum = 0;

            for(List<Integer> threadResult : partialResults){
                System.out.print("Adding ");
                for (Integer partialResult : threadResult) {
                    System.out.print(partialResult + " ");
                    sum += partialResult;
                }
                System.out.println();
            }
            System.out.println(Thread.currentThread().getName() + ": Final result = " + sum);
        }
    }

    private void runSimulation(int numWorkers, int numberOfPartialResults) {

        List<List<Integer>> partialResults = new ArrayList<>();

        Aggregator aggregator = new Aggregator(numWorkers, numberOfPartialResults, partialResults);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numWorkers, aggregator);

        System.out.println("Spawning " + numWorkers + " worker threads to compute " + numberOfPartialResults + " partial results each");
        for (int i = 0; i < numWorkers; i++) {
            Thread worker = new Thread(new NumberCruncherTask(cyclicBarrier, numberOfPartialResults, partialResults));
            worker.setName("Thread " + i);
            worker.start();
        }
    }
}
