package org.atanu.multithreading.throughput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Test by invoking
//http://localhost:8000/search?word=talk
public class HTTPServerThroughput {

    private static final String INPUT_FILE = "resources/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 1;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    //HttpServer.create
    //Create a HttpServer instance which will bind to the specified InetSocketAddress (IP address and port number) A maximum backlog can also be specified.
    //This is the maximum number of queued incoming connections to allow on the listening socket.
    //Queued TCP connections exceeding this limit may be rejected by the TCP implementation.
    //The HttpServer is acquired from the currently installed HttpServerProvider

    //InetSocketAddress
    //Creates a socket address where the IP address is the wildcard address and the port number a specified value.
    //A valid port value is between 0 and 65535. A port number of zero will let the system pick up an ephemeral port in a bind operation.

    private static void startServer(String text) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
        httpServer.createContext("/search", new WordCountHandler(text));
        //Create Threads which will handle the incoming requests
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        httpServer.setExecutor(executor);
        httpServer.start();

    }

    private static class WordCountHandler implements HttpHandler {

        private String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String query = httpExchange.getRequestURI().getQuery();
            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];
            //Query param is wrong
            if(!action.equals("word")){
                httpExchange.sendResponseHeaders(400, 0);
            }

            long wordCount = countWord(word);
            byte[] response = Long.toString(wordCount).getBytes(StandardCharsets.UTF_8);
            //Set the HTTP Response Header
            httpExchange.sendResponseHeaders(200, response.length);
            //Write Data to HTTP Output Stream
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();//This will send
        }

        private long countWord(String word) {
            long wordCount = 0;
            int index = 0;

            while (index >= 0){
                index = text.indexOf(word, index);
                if(index >= 0){
                    wordCount++;
                    index++; // increment index so the search happens from that position
                }
            }
            return wordCount;
        }
    }
}
