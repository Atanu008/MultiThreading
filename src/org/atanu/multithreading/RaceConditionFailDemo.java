package org.atanu.multithreading;

import java.util.Random;

//The below program spawns two threads. One thread prints the value of a shared variable whenever the shared variable is divisible by 5.
//A race condition happens when the printer thread executes a test-then-act if clause,
//which checks if the shared variable is divisible by 5 but before the thread can print the variable out,
//its value is changed by the modifier thread.
//Some of the printed values aren't divisible by 5 which verifies the existence of a race condition in the code.
public class RaceConditionFailDemo {

    public static void main(String[] args) throws InterruptedException {
       runTest();
    }

    public static void runTest() throws InterruptedException {

        RaceConditionFail raceCondition = new RaceConditionFail();

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

    }
}

class RaceConditionFail{

    int randInt;
    Random random = new Random();

    public void printer(){

        int i = 1000000;

        while(i -->0){
            if(randInt % 5 == 0) {
                if (randInt % 5 != 0) {
                    System.out.println(randInt);
                }
            }
        }
    }

    public void modifier(){

        int i = 1000000;
        while (i --> 0){
            randInt = random.nextInt(1000);
        }
    }
}
