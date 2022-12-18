package org.atanu.multithreading.join;

import java.math.BigInteger;

//In this coding exercise you will use all the knowledge from the previous lectures.
//Before taking the exercise make sure you review the following topics in particular:
//1. Thread Creation - how to create and start a thread using the Thread class and the start() method.
//2. Thread Join - how to wait for another thread using the Thread.join() method.
//In this exercise we will efficiently calculate the following result = base1 ^ power1 + base2 ^ power2

//result1 = x1 ^ y1
//result2 = x2 ^ y2
// Compute In parallel and combine the result in the end : result = result1 + result2
//This way we can speed up the entire calculation.
public class JoinDemo {

    public static void main(String[] args) {

        BigInteger result = calculateResult(new BigInteger("10"), new BigInteger("2"), new BigInteger("20"), new BigInteger("3"));
        System.out.println("Result Calculated from two Thread "+ result);
    }
    public static BigInteger calculateResult(BigInteger base1,
                                      BigInteger power1,
                                      BigInteger base2,
                                      BigInteger power2) {
        BigInteger result;
        PowerCalculatingThread thread1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread thread2 = new PowerCalculatingThread(base2, power2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result = thread1.getResult().add(thread2.getResult());
        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            for(BigInteger i = BigInteger.ZERO;
                i.compareTo(power) !=0;
                i = i.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
