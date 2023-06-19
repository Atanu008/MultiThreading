package org.atanu.multithreading.foobar;

public class FooBarExecution {

    private static class FooBar{

        private int n;
        private int flag;

        FooBar(int n){
            this.n = n;
            flag = 0;
        }

        public void foo() throws InterruptedException {

            for (int i = 0; i < n; i++) {
               synchronized (this){
                   while (flag == 1){
                       this.wait(); // If Flag is One , wait. will only print Fooo when flag is zero
                   }

                   System.out.println("Foo"); // Flag became Zero
                   flag = 1; // Set Flag to One to print Bar in another Thread
                   this.notify(); // after setting the flag notify the Other Thread
               }
            }
        }

        public void bar() throws InterruptedException {

            for (int i = 0; i < n; i++) {
                synchronized (this){
                    while (flag == 0){
                        this.wait(); // If Flag is Zero , wait. will only print Bar when flag is one
                    }

                    System.out.println("Bar"); // Flag became One
                    flag = 0; // Set Flag to One to print Foo in another Thread
                    this.notify(); // after setting the flag notify the Other Thread
                }
            }
        }
    }

    static class FooBarPrinterThread extends Thread{

        private FooBar fooBar;
        private String methodType;

        FooBarPrinterThread(FooBar fooBar, String methodType){
            this.fooBar = fooBar;
            this.methodType = methodType;
        }

        @Override
        public void run(){

            if(methodType.equals("foo")){
                try {
                    fooBar.foo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(methodType.equals("bar")){
                try {
                    fooBar.bar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        FooBar fooBar = new FooBar(5);
        FooBarPrinterThread fooThread = new FooBarPrinterThread(fooBar, "foo");
        FooBarPrinterThread barThread = new FooBarPrinterThread(fooBar, "bar");

        barThread.start();
        fooThread.start();
    }
}
