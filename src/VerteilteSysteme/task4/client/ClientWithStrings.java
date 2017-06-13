package task4.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientWithStrings {
    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 8090;

        try {
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(in.readLine());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}