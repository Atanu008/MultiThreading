package org.atanu.multithreading.tokenbucket.daemon;

public class TokenBucketFactory {

    private TokenBucketFactory(){

    }

    public static TokenBucketFilter makeTokenBucketFilter(int capacity) {
        MultithreadedTokenBucketFilter tbf = new MultithreadedTokenBucketFilter(capacity);
        tbf.initialize();
        return tbf;
    }

    static class MultithreadedTokenBucketFilter extends TokenBucketFilter{
        private long possibleTokens = 0;
        private final int MAX_TOKENS;
        private final int ONE_SECOND = 1000;

        // MultithreadedTokenBucketFilter object can only
        MultithreadedTokenBucketFilter(int maxTokens) {
            MAX_TOKENS = maxTokens;
        }

        void initialize(){
            Thread daemon = new Thread(() -> createDaemon());
            daemon.setDaemon(true);
            daemon.start();
        }

        private void createDaemon() {
            while (true){
                synchronized (this){
                    possibleTokens = Math.min(possibleTokens + 1, MAX_TOKENS);
                    this.notifyAll();
                }
                try {
                    Thread.sleep(ONE_SECOND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void getToken() throws InterruptedException {

            synchronized (this){
                while (possibleTokens == 0){
                    this.wait();
                }
                possibleTokens--;
                System.out.println(
                        "Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);
            }
        }
    }
}
