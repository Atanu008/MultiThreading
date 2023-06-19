package org.atanu.multithreading.oddeven;

// Synchronized Implementation

public class OddEvenExecution {

    static class OddEven{

        int n;
        int counter;
        OddEven(int n){
            this.n = n;
            counter = 1;
        }

        public void printOdd() throws InterruptedException {

            synchronized (this){
                while (counter < n){ // less than not <= as other thread will increment and it will be mre than
                    while (counter % 2 == 0){ // Wait while the counter is Even
                        this.wait();
                    }
                    System.out.println("Odd "+counter); // Counter became Odd, so resume
                    counter++; // Increment counter
                    this.notifyAll(); // notify other thread, basically Even Thread
                }
            }
        }

        public void printEven() throws InterruptedException {

            synchronized (this){
                while (counter < n){
                    while (counter % 2 == 1){ // Wait while the counter is Odd
                        this.wait();
                    }
                    System.out.println("Even "+ counter); // Counter became Even, so resume
                    counter++; // increment the counter
                    this.notifyAll(); // notify other thread, basically odd Thread
                }
            }
        }
    }


    public static void main(String[] args) {

        OddEven oddEven = new OddEven(9);

        Thread oddThread = new Thread(() -> {
            try {
                oddEven.printOdd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread evenThread = new Thread(() -> {
            try {
                oddEven.printEven();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        oddThread.start();
        evenThread.start();

    }
}
