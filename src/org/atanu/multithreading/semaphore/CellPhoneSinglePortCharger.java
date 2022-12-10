package org.atanu.multithreading.semaphore;

import java.util.concurrent.Semaphore;

//AT a Time only ONE cell phone will be allowed to charge, Binary Semaphore, its acts like a Mutex
//others needs to wait until any of the cellphone is done charging
public class CellPhoneSinglePortCharger {

    final static int SINGLE_CHARGER_PORT = 1;

    public static void main(String[] args) {

        Semaphore binarySemaphore = new Semaphore(SINGLE_CHARGER_PORT);

        for (int i = 0; i < 10; i++){
            new Thread(new CellPhone(binarySemaphore)).start();
        }
    }
}
