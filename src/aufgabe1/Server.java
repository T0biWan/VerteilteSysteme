// Server.java

import com.sun.org.apache.xpath.internal.SourceTree;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class Server {
    private static final int port = 11111;

    public static void main(String[] args) {
        Server server = new Server();

        while(true) { // Keeps Server running
            try {
                server.fibonacci();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void echo() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket client = listen(serverSocket);
        String message = readMessage(client);
        writeMessae(client, message);
    }

    private void fibonacci() throws IOException {
        ServerSocket serverSocket = new java.net.ServerSocket(port);
        System.out.println("Server is running and listens on Port: " + port);
        Socket client = listen(serverSocket);
        Fibonacci fibonacci = new Fibonacci();

        String message = readMessage(client);
        int fibo = fibonacci.getFibonacci(Integer.parseInt(message));
        writeMessae(client, Integer.toString(fibo));
    }

    private Socket listen(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept(); // blockiert, bis sich ein Client angemeldet hat
        return socket;
    }

    private String readMessage(Socket socket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        char[] buffer = new char[200];
        int anzahlZeichen = bufferedReader.read(buffer, 0, 200);
        String nachricht = new String(buffer, 0, anzahlZeichen);

        return nachricht;
    }

    private void writeMessae(Socket socket, String message) throws IOException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

}