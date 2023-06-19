package org.atanu.multithreading.blockingqueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Excellent Video : https://www.youtube.com/watch?v=UOr9kMCCa5g
// In both the enqueue() and dequeue() methods we use the notifyAll() method instead of the notify() method.
// The reason behind the choice is very crucial to understand.
// Consider a situation with two producer threads and one consumer thread all working with a queue of size one.
// It's possible that when an item is added to the queue by one of the producer threads,
// the other two threads are blocked waiting on the condition variable.
// If the producer thread after adding an item
// invokes notify() it is possible that the other producer thread is chosen by the system to resume execution.
// The woken-up producer thread would find the queue full and go back to waiting on the condition variable,
// causing a deadlock. Invoking notifyAll() assures that the consumer thread also gets a chance to wake up
// and resume execution.

public class BlockingQueueUsingLock {

    static class BlockingQueue<E> {

        Queue<E> queue;
        int capacity;
        ReentrantLock reentrantLock;
        Condition queueFullCondition;
        Condition queueEmptyCondition;

        BlockingQueue(int capacity){
            this.capacity = capacity;
            queue = new LinkedList<>();
            reentrantLock = new ReentrantLock();
            queueFullCondition = reentrantLock.newCondition();
            queueEmptyCondition = reentrantLock.newCondition();
        }

        public void enqueue(E item){
            try {
                reentrantLock.lock();
                // Wait while the Queue is Full
                while (queue.size() == capacity){ // Need while to safe guard for spurious wake up
                    queueFullCondition.await();
                }
                queue.offer(item);
                // Signal that one item is added
                queueEmptyCondition.signalAll(); // Notify Consumer Threads . basically all threads
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                reentrantLock.unlock();
            }
        }

        public E dequeue() throws InterruptedException {

            E item = null;
            try {
                reentrantLock.lock();
                while(queue.size() == 0){
                    queueEmptyCondition.await();
                }
                item = queue.poll();
                // Signal that one item is removed , so a new item can be pushed
                queueFullCondition.signalAll();
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {

                reentrantLock.unlock();
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
                    blockingQueue.enqueue(i);
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
