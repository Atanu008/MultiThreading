package org.atanu.multithreading.blockingqueue;

import java.util.LinkedList;
import java.util.Queue;

// Excellent Video : https://www.youtube.com/watch?v=UOr9kMCCa5g

public class BlockingQueueUsingSynchronized {

    static class BlockingQueue<E>{

        Queue<E> queue;
        int capacity;

        BlockingQueue(int capacity){
            this.capacity = capacity;
            queue = new LinkedList<>();
        }

        public void enqueue(E item) throws InterruptedException {
            synchronized (this) {
                while (queue.size() == capacity){
                    this.wait();
                }
                queue.offer(item);
                this.notifyAll();
            }
        }

        public E dequeue() throws InterruptedException {

            E item = null;
            synchronized (this){
                while (queue.size() == 0){
                    this.wait();
                }
                item = queue.poll();
                this.notifyAll();
            }
            return item;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int n = 5;
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(n);

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    try {
                        blockingQueue.enqueue(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Enqueued "+ i);
                }
            }
        });

        Thread consumerA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 25; i++) {
                    try {
                        System.out.println("Consumer A Dequeued "+ blockingQueue.dequeue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread consumerB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 25; i++) {
                    try {
                        System.out.println("Consumer B Dequeued "+ blockingQueue.dequeue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        producer.start(); // Start Producer
        Thread.sleep(5000); // Sleep the Main Thread

        consumerA.start();
        consumerA.join();

        consumerB.start();

        producer.join();
        consumerB.join();
    }
}
