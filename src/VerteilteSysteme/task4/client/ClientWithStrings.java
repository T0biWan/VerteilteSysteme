package task4.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientWithStrings {
    public static void main(String[] args) throws IOException {
        String IP = "127.0.0.1";
        int port = 8090;

        try {
            Socket socket = new Socket(IP, port);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(input.readLine());

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + IP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + IP);
            System.exit(1);
        }
    }
}