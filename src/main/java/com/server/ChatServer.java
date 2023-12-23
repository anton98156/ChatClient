package com.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            while (true) {
                new Handler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }

    private static class Handler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(out);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    for (PrintWriter writer : clientWriters) {
                        writer.println(inputLine);
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка при общении с клиентом: " + e.getMessage());
            } finally {
                if (out != null) {
                    clientWriters.remove(out);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
