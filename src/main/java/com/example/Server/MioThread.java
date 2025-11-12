package com.example.Server;

import java.util.concurrent.ThreadLocalRandom;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MioThread extends Thread {

    Socket MySocket;

    int segreto = ThreadLocalRandom.current().nextInt(1, 101); // 1 incluso, 101 escluso

    int min = 1, max = 100;
    int tentativi = 0;

    public MioThread(Socket socket) {
        this.MySocket = socket;
    }

    @Override
    public void run() {

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(MySocket.getInputStream()));

                PrintWriter out = new PrintWriter(MySocket.getOutputStream(), true);) {

            System.out.println("Giocatore connesso: " + MySocket.getInetAddress());
            out.println("WELCOME INDOVINA v1 RANGE 1 100");

            String line = in.readLine();
            String[] parts = line.split(" ");

            parts[0].toUpperCase();
            while (!parts[0].equals("QUIT")) {
                if (parts.length == 0) {
                    out.println("ERR SYNTAX");
                } else {
                    if (parts[0].equals("RANGE") || parts[0].equals("GUESS") || parts[0].equals("NEW")
                            || parts[0].equals("STATS")) {
                        if (parts[0] == "RANGE") {
                            if (parts.length <= 2) {
                                out.println("ERR SINTAX");
                            }
                            if (tentativi != 0) {
                                out.println("ERR NOTALLOWD");
                            }

                            int massimo, minimo;
                            try {
                                massimo = Integer.valueOf(parts[1]);
                                minimo = Integer.valueOf(parts[0]);
                            } catch (NumberFormatException e) {
                                out.println("VALORI NON NUMERICI");
                                continue;
                            }
                            min = minimo;
                            max = massimo;
                            if (minimo > massimo) {
                                out.println("ERR OUTOFRANGE");

                            }
                            segreto = ThreadLocalRandom.current().nextInt(min, max + 1);
                            out.println("OK RANGE" + min + " " + max);
                        }

                        if (parts[0] == "GUESS") {
                            if (parts[1] == "" || parts.length > 1) {
                                out.println("ERR SINTAX");
                            }
                            int guess;

                            try {
                                guess = Integer.parseInt(parts[1]);
                            } catch (NumberFormatException e) {
                                out.println("ERR SINTAX");
                                continue;
                            }
                            tentativi++;

                            if (guess > max) {
                                if (guess == segreto) {
                                    out.println("OK CORRRECT in t=" + tentativi);
                                }
                                if (guess > segreto) {
                                    out.println("HINT LOWER");
                                }
                                if (guess < segreto) {
                                    out.println("HINT HIGHER");
                                }
                            } else {
                                out.println("ERR OUTOFRANGE " + min + " " + max);
                            }

                        }
                        if (parts[0] == "NEW") {
                            if (parts.length == 1) {
                                segreto = ThreadLocalRandom.current().nextInt(1, 101);
                                out.println("OK NEW");
                            } else {
                                out.println("ERR SYNTAX");
                            }
                        }
                        if (parts[0] == "STATS") {
                            if (parts.length == 1) {
                                out.println("INFO RANGE" + min + " " + max + "; TRIES " + tentativi);
                            }
                        }

                    } else {
                        out.print("ERR UNKNOWNCMD");
                    }
                }

                out.print("BYE");
                MySocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
