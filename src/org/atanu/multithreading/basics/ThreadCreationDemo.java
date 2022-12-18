package org.atanu.multithreading.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//One Hacker will try to guess the passowrd starting from 0 to Max
//Another hacker will try to guess the passowrd starting from Max to 0
//If any of the hacker guessed the password  system  will exit
//Police will count to 10 , waiting in between . if police finishes count 10 before any of the Hacker guessed it will exit
//Sometimes one of the hacker will guess the Vault code
//Sometimes police will catch
public class ThreadCreationDemo {

    public static int PASSWORD = 9999;

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        Vault vault = new Vault(random.nextInt(PASSWORD));

        List<Thread> threads = new ArrayList<>();
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new Police());

        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            thread.join();
        }

    }

    private static class Vault{
        int password;

        public Vault(int password) {
            this.password = password;
        }

        public boolean isCorrect(int guess){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return this.password == guess;
        }
    }

    private static abstract class HackerThread extends Thread{

        public Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public synchronized void start() {
            super.start();
        }
    }

    private static class AscendingHackerThread extends HackerThread{

        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for(int guues = 0; guues <= PASSWORD; guues++){
                if(vault.isCorrect(guues)){
                    System.out.println(this.getName()+ " guessed the password "+ guues);
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingHackerThread extends HackerThread{

        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for(int guues = PASSWORD; guues >= 0; guues--){
                if(vault.isCorrect(guues)){
                    System.out.println(this.getName()+ " guessed the password "+ guues);
                    System.exit(0);
                }
            }
        }
    }

    private static class Police extends Thread{

        @Override
        public void run() {
            for(int i = 0; i < 10; i++){
                try {
                    //Wait and then count
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Counting "+ i);
            }
            System.out.println("Game Over");
            System.exit(0);
        }
    }
}
