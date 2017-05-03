package client;// client.Client.java

import exceptions.InputException;

import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class Client {
    private static final String IP = "127.0.0.1"; // localhost
    private static final int port = 11111;
    private static final int minimum = 0;
    private static final int maximum = 20;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Client client = new Client();

        try {
            client.fibonacci();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void echo() throws IOException {
        Socket serverSocket = new Socket(IP, port); // Connect with server.Server

        String message = "ph'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn,";
        writeStringToServer(serverSocket, message);

        String answer = readFromServer(serverSocket);
        System.out.println(answer);
    }

    private void fibonacci() throws IOException {
        Socket socket = new Socket(IP, port); // Connect with server.Server
        System.out.println("Connected to server.Server on IP: " + IP + " and Port: " + port);
        int input = inputInt();
        writeIntToServer(socket, input);
        String answer = readFromServer(socket);
        System.out.println("tasks.Fibonacci of " + input + " is " + answer);
    }

    private void writeIntToServer(Socket socket, int message) throws IOException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    private void writeStringToServer(Socket socket, String message) throws IOException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    private String readFromServer(Socket socket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //String message = bufferedReader.readLine();

        char[] buffer = new char[200];
        int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert bis Nachricht empfangen
        String message = new String(buffer, 0, anzahlZeichen);
        return message;
    }

    private int takeInput () {
        int input = scanner.nextInt();
        scanner.close();
        return input;
    }

    private void checkInput(int n) throws InputException {
        if (inputIsWithinBoundaries(n)) throw new InputException();
    }

    private boolean inputIsWithinBoundaries(int n) {
        return !(n < minimum || n > maximum);
    }

    public int inputInt() {
        System.out.print("Input an Integer between [" + minimum + ", " + maximum + "]: ");
        int input = takeInput();
        if(inputIsWithinBoundaries(input)) return input;
        return -1;
    }
}