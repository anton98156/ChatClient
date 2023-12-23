package com.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "127.0.0.1"; // Адрес сервера
    private static final int PORT = 12345; // Порт сервера

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Вы присоединились к чату.");
            System.out.println("Введите сообщения:");

            Thread inputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String userInput;
                    while (true) {
                        userInput = scanner.nextLine();
                        out.println(userInput);
                    }
                }
            });
            
            inputThread.start();

            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                System.out.println(fromServer);
            }
        } catch (UnknownHostException e) {
            System.err.println("Не удалось найти хост " + SERVER_ADDRESS);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Не удалось установить соединение с " + SERVER_ADDRESS);
            System.exit(1);
        }
    }
}
