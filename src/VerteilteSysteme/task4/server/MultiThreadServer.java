package task4.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer implements Runnable {
    private Socket clientSocket;
    private PrintWriter output;
    private static int connectedClients = 0;
    private static int maxConnectedClients = 5;

    MultiThreadServer(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            this.output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        final int port = 8090;
        final String IP = "127.0.0.1";
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is running on " + IP + " and listens on Port: " + port);

        while (true) {
            if (!reachedMaximumOfConnectedClients()) {
                connectedClients++;
                Socket socket = serverSocket.accept();
                System.out.println("New client connected [" + connectedClients + "/" + maxConnectedClients + "]");
                new Thread(new MultiThreadServer(socket)).start();
            } else {
                // todo: Wie Client Rückmeldung geben?
                System.out.println("To many clients");
            }
        }
    }

    public void run() {
        try {

            send("Welcome");

            this.output.close();
            this.clientSocket.close();
            connectedClients --;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void send(String message) {
        this.output.println(message);
    }

    private static boolean reachedMaximumOfConnectedClients() {
        return !(connectedClients <= maxConnectedClients);
    }
}