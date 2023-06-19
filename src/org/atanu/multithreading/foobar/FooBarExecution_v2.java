package org.atanu.multithreading.foobar;

// Same As FooBarExecution
// Here its using Runnable
public class FooBarExecution_v2 {

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



    public static void main(String[] args) {

        FooBar fooBar = new FooBar(5);

        Thread fooThread = new Thread(()-> {
            try {
                fooBar.foo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread barThread = new Thread(()-> {
            try {
                fooBar.bar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        barThread.start();
        fooThread.start();
    }
}
