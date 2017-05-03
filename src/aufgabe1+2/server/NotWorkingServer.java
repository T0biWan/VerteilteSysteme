package server;// server.NotWorkingServer.java

import tasks.Fibonacci;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class NotWorkingServer {
    private static final int port = 11111;

    public static void main(String[] args) {
        NotWorkingServer server = new NotWorkingServer();

        //while(true) { // Keeps server.NotWorkingServer running
            try {
                server.echo();
            } catch (IOException e) {
                e.printStackTrace();
           // }
        }
    }

    private void echo() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket client = listen(serverSocket);
        System.out.println("listens");

        String message = readMessage(client);
        writeMessage(client, message);
    }

    private void fibonacci() throws IOException {
        ServerSocket serverSocket = new java.net.ServerSocket(port);
        System.out.println("server.NotWorkingServer is running and listens on Port: " + port);
        Socket client = listen(serverSocket);
        Fibonacci fibonacci = new Fibonacci();

        String message = readMessage(client);
        int fibo = fibonacci.getFibonacci(Integer.parseInt(message));
        writeMessage(client, Integer.toString(fibo));
    }

    private Socket listen(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept(); // blockiert, bis sich ein client.NotWorkingClient angemeldet hat
        return socket;
    }

    private String readMessage(Socket socket) throws IOException { // TODO Buffered Reader ändern...
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = bufferedReader.readLine();

        return message;
    }

    private void writeMessage(Socket socket, String message) throws IOException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

}