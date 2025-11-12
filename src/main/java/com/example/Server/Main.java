package com.example.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
         ServerSocket serverSocket = new ServerSocket(3001);
        System.out.println("Server avviato sulla porta 3001");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            MioThread t = new MioThread(clientSocket);
            t.start();
        }
    }
}