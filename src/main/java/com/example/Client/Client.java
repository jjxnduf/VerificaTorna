package com.example.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws java.io.IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserisci l'IP del server: ");
        String ip = scanner.nextLine();

        System.out.print("Inserisci la porta del server: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        Socket socket = new Socket(ip, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println(in.readLine());
        while (in.readLine() == "BYE") {

            System.out.println(in.readLine());
            System.out.println("Tutte le lettere devono essere maiuscole");
            System.out.println(
                    "Comando GUESS per indovinare il numero \n Comando RANGE per cambiare il range del numero casuale \n Comando NEW per resettare il gioco \n Comando STATS per vedere i tentativi e range \n Comando QUIT per abbandonare");
            String command = scanner.nextLine();
            out.println(command);

        }

        in.close();
        out.close();
        socket.close();
        scanner.close();
    }
}