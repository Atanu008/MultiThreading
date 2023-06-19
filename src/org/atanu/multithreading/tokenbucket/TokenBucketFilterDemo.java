package org.atanu.multithreading.tokenbucket;

import java.util.HashSet;
import java.util.Set;

// Note how getToken() doesn't return any token type !
// The fact a thread can return from the getToken call would imply that the thread has the token,
// which is nothing more than a permission to undertake some action.

// We need to think about the following three cases to roll out our algorithm. Let's assume the maximum allowed tokens our bucket can hold is 5.
//
// The last request for token was more than 5 seconds ago:
// In this scenario, each elapsed second would have generated
// one token which may total more than five tokens since the last request was more than 5 seconds ago.
// We simply need to set the maximum tokens available to 5 since that is the most the bucket will hold and return one token out of those 5.
//
// The last request for token was within a window of 5 seconds:
// In this scenario, we need to calculate the new tokens generated since the last request and add them to the unused tokens we already have.
// We then return 1 token from the count.
//
// The last request was within a 5-second window and all the tokens are used up:
// In this scenario, there's no option but to sleep for a whole second to guarantee that a token would become available
// and then let the thread return. While we sleep(),
// the monitor would still be held by the token-requesting thread and any new threads invoking getToken would get blocked,
// waiting for the monitor to become available.


public class TokenBucketFilterDemo {

    public static void main(String[] args) throws InterruptedException {
        TokenBucketFilter.runTestMaxTokenIsTen();
    }

    static class TokenBucketFilter{

        private int maxToken;
        private long lastRequestTime;
        private long possibleTokens;

        TokenBucketFilter(int maxToken){
            this.maxToken = maxToken;
            lastRequestTime = System.currentTimeMillis();
            possibleTokens = 0;
        }

        public synchronized void getToken() throws InterruptedException {

            possibleTokens += (System.currentTimeMillis() - lastRequestTime) / 1000;

            if(possibleTokens > maxToken){
                possibleTokens = maxToken;
            }

            if(possibleTokens == 0){
                Thread.sleep(1000);
            }else{
                possibleTokens--;
            }

            lastRequestTime = System.currentTimeMillis();

            System.out.println("Granting "+ Thread.currentThread().getName() +" token at "+ (System.currentTimeMillis()) / 1000);
        }

        public static void runTestMaxTokenIsTen() throws InterruptedException {

            Set<Thread> allThreads = new HashSet<>();
            int maxToken = 5;
            final TokenBucketFilter tokenBucketFilter = new TokenBucketFilter(maxToken);

            Thread.sleep(10000);

            for(int i = 0; i < 12; i++){
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tokenBucketFilter.getToken();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.setName("Thread_ "+ (i + 1));
                allThreads.add(thread);
            }

            for(Thread thread : allThreads){
                thread.start();
            }

            for(Thread thread : allThreads){
                thread.join();
            }
        }

    }
}
