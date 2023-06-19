package org.atanu.multithreading.blockingqueue;

// https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/B8OO0L8gRkW

public class GenericBlockingQueue {

    static class BlockingQueue<T> {

        T[] array;
        int capacity = 0;
        int size = 0;
        int head = 0;
        int tail = 0;

        BlockingQueue(int capacity){
            this.capacity = capacity;
            array = (T[]) new Object[capacity];
            size = 0;
            head = 0;
            tail = 0;
        }

        private void enqueue(T item) throws InterruptedException {

            synchronized (this){ // We can have different lock also i.e synchronized (lock) . Object lock = new Object(); in member

                // Pls wait while the Queue is full
                while (size == capacity){
                    this.wait();
                }
                // reset tail to the beginning if the tail is already
                // at the end of the backing array
                if(tail == capacity){
                    tail = 0;
                }
                // place the item in the array
                array[tail] = item;
                tail++;
                size++;
                // don't forget to notify any other threads waiting on
                // a change in value of size. There might be consumers
                // waiting for the queue to have atleast one element
                this.notifyAll();
            }
        }

        private T dequeue() throws InterruptedException {

            T item = null;
            synchronized (this){
                // wait for atleast one item to be enqueued
                while (size == 0){
                    this.wait();
                }
                // reset head to start of array if its past the array
                if(head == capacity){
                    head = 0;
                }
                // store the reference to the object being dequeued
                // and overwrite with null
                item = array[head];
                array[head] = null;
                head++;
                size--;

                // don't forget to call notify, there might be another thread
                // blocked in the enqueue method.
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
                        System.out.println("Enqueued "+ i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
