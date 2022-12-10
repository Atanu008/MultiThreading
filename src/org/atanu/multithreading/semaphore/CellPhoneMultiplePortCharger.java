package org.atanu.multithreading.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

//AT a Time only FOUR cell phones will be allowed to charge,
//others needs to wait until any of the cellphone is done charging

public class CellPhoneMultiplePortCharger {

    final static int MULTIPLE_CHARGER_PORT = 4;

    public static void main(String[] args) {

        Semaphore chargerPort = new Semaphore(MULTIPLE_CHARGER_PORT);

        for (int i = 0; i < 10; i++){
            new Thread(new CellPhone(chargerPort)).start();
        }
    }

}
