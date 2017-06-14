package task4.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientWithStrings {
    private static String IP = "127.0.0.1";
    private static int port = 8090;
    private static boolean isRunning = true;
    private static Scanner scanner = new Scanner(System.in);
    private static String prompt = "$> ";
    private static Socket socket;
    private static PrintWriter outputStream;
    private static BufferedReader inputStream;

    public static void main(String[] args) throws IOException {


        try {
            socket = new Socket(IP, port);
            outputStream = new PrintWriter(socket.getOutputStream(), true);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println(inputStream.readLine());
            while (isRunning) processUserInput(getUserInput());

            inputStream.close();
            outputStream.close();
            socket.close();
            scanner.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + IP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + IP);
            System.exit(1);
        }
    }

    private static String getUserInput() {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void processUserInput(String userInput) throws IOException {
        if (userInput.equals("e")) end();
        else {
            send(userInput);
            System.out.println(receive());
        }
        System.out.println();
    }

    private static void send(String message) {
        outputStream.println(message);
    }

    private static String receive() throws IOException {
        return inputStream.readLine();
    }

    private static void end() {
        System.out.println("goodBye");
        isRunning = false;
    }
}