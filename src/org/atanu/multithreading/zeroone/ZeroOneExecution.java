package org.atanu.multithreading.zeroone;


// Print Zero and One alternatively continuously

public class ZeroOneExecution {

    private static class ZeroOne{

        private int flag;

        ZeroOne(){
            flag = 0;
        }

        public void zero() throws InterruptedException {

           while (true){
                synchronized (this){
                    while (flag == 1){
                        this.wait(); // If Flag is One , wait. will only print Fooo when flag is zero
                    }

                    System.out.println("0"); // Flag became Zero
                    flag = 1; // Set Flag to One to print Bar in another Thread
                    this.notify(); // after setting the flag notify the Other Thread
                }
            }
        }

        public void one() throws InterruptedException {

           while (true){
                synchronized (this){
                    while (flag == 0){
                        this.wait(); // If Flag is Zero , wait. will only print Bar when flag is one
                    }

                    System.out.println("1"); // Flag became One
                    flag = 0; // Set Flag to One to print Foo in another Thread
                    this.notify(); // after setting the flag notify the Other Thread
                }
            }
        }
    }

    static class ZeroOnePrinterThread extends Thread{

        private ZeroOne zeroOne;
        private String methodType;

        ZeroOnePrinterThread(ZeroOne zeroOne, String methodType){
            this.zeroOne = zeroOne;
            this.methodType = methodType;
        }

        @Override
        public void run(){

            if(methodType.equals("zero")){
                try {
                    zeroOne.zero();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(methodType.equals("one")){
                try {
                    zeroOne.one();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        ZeroOne zeroOne = new ZeroOne();
        ZeroOnePrinterThread zeroThread = new ZeroOnePrinterThread(zeroOne, "zero");
        ZeroOnePrinterThread oneThread = new ZeroOnePrinterThread(zeroOne, "one");

        zeroThread.start();
        oneThread.start();
    }
}
