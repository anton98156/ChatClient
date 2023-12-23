package com.server;

public class App {
    public static void main(String[] args) {
        
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ChatServer.main(null);
            }
        });
        serverThread.start();

        ChatClient.main(null);
    }
}

