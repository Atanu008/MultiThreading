package org.atanu.multithreading.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class CellPhone implements Runnable{
    private Semaphore chargerPort;

    public CellPhone(Semaphore chargerPort){
        this.chargerPort = chargerPort;
    }

    @Override
    public void run() {

        try {
            chargerPort.acquire(); // Acquire the charger
            System.out.println(Thread.currentThread().getName() + " is charging ....");

            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " is DONE charging ....");
            chargerPort.release(); // Release the Charger
        }
    }
}
