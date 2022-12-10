package org.atanu.multithreading;

//Re-entrant locks allow for re-locking or re-entering of a synchronization lock
//This Example is a Demostration of Non Rentrant Lock

//any object of this class if locked twice in succession would result in a deadlock.
//The same thread gets blocked on itself

//If a synchronization primitive doesn't allow reacquisition of itself by a thread that has already acquired it,
//then such a thread would block as soon as it attempts to reacquire the primitive a second time.
public class NonReentrantLockDemo {

    public static void main(String[] args) throws InterruptedException {
        NonReentrantLock nonReentrantLock = new NonReentrantLock();

        // First locking would be successful
        nonReentrantLock.lock();
        System.out.println("Acquired first lock");

        // Second locking results in a self deadlock
        System.out.println("Trying to acquire second lock");
        nonReentrantLock.lock();
        System.out.println("Acquired second lock");

    }
}

class NonReentrantLock{

    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException {

        while(isLocked){
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock(){
        isLocked = false;
        notify();
    }
}
