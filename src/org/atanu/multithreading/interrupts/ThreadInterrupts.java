package org.atanu.multithreading.interrupts;

import java.math.BigInteger;

public class ThreadInterrupts {

    public static void main(String[] args) throws InterruptedException {

        //Small numbers it okay . For Big number it will take time
        //Thread thread = new Thread(new LongComputation(new BigInteger("2"), new BigInteger("5")));
        //thread.start();

        Thread longComputationThread = new Thread(new LongComputation(new BigInteger("20000"), new BigInteger("50000")));
        longComputationThread.start();
        //Wait for sometime to finish the cal culculation
        Thread.sleep(200);
        longComputationThread.interrupt(); //interrupt the Thread

        longComputationThread.join();
    }

    private static class LongComputation implements Runnable{

        private BigInteger base;
        private BigInteger power;

        public LongComputation(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private Object pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            for(BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)){

                if(Thread.currentThread().isInterrupted()){
                    System.out.println("Prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
