package org.atanu.multithreading;

import java.util.Random;

public class RaceConditionSuccessDemo {

    public static void main(String[] args) throws InterruptedException {
        runTest();
    }

    public static void runTest() throws InterruptedException {

        RaceConditionSuccess raceCondition = new RaceConditionSuccess();

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                raceCondition.printer();
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                raceCondition.modifier();
            }
        });

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        System.out.println("No Integer prints. Means No Race Condition");
    }
}

class RaceConditionSuccess {

    int randInt;
    Random random = new Random();

    public void printer() {

        int i = 1000000;

        synchronized (this) {
            while (i-- > 0) {
                if (randInt % 5 == 0) {
                    if (randInt % 5 != 0) {
                        System.out.println(randInt);
                    }
                }
            }
        }

    }

    public void modifier() {

        int i = 1000000;
        synchronized (this) {
            while (i-- > 0) {
                randInt = random.nextInt(1000);
            }
        }
    }
}
