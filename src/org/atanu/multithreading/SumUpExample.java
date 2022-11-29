package org.atanu.multithreading;

public class SumUpExample {

    final static long MAX_NUM = Integer.MAX_VALUE;

    public static void main(String[] args) throws InterruptedException {
        sumUPUsingOneThread();
        sumUPUsingTwoThread();
    }

    public static void sumUPUsingOneThread(){

        long start = System.currentTimeMillis();
        SumUpRange sumUpRange = new SumUpRange(0, MAX_NUM);
        sumUpRange.sum();
        long end = System.currentTimeMillis();

        System.out.println("Single thread final count = " + sumUpRange.sum + " took " + (end - start));
    }


    public static void sumUPUsingTwoThread() throws InterruptedException {

        long start = System.currentTimeMillis();

        SumUpRange sumUpRangeA = new SumUpRange(0, MAX_NUM / 2);
        SumUpRange sumUpRangeB = new SumUpRange(1 + (MAX_NUM / 2), MAX_NUM);

        Thread threadA = new Thread(() -> {
            sumUpRangeA.sum();
        });

        Thread threadB = new Thread(() -> {
            sumUpRangeB.sum();
        });

        // run Both threads
        threadA.start();
        threadB.start();

        // wait for threadA and threadB to complete.
        threadA.join();
        threadB.join();

        long finalCount = sumUpRangeA.sum + sumUpRangeB.sum;
        long end = System.currentTimeMillis();

        System.out.println("Two threads final count = " + finalCount + " took " + (end - start));
    }

}

class SumUpRange{

    long startRange;
    long endRange;
    long sum;

    public SumUpRange(long startRange, long endRange){
        this.startRange = startRange;
        this.endRange = endRange;
    }

    public void sum(){
        for(long i = startRange; i <= endRange; i++){
            sum += i;
        }
    }
}
