package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        final int port = 7;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean argsIsLessThen(int numberOfArguments, String[] args) {
        return args.length < numberOfArguments;
    }

    private void shutDownIfNotEnoughArguments(String[] args) {
        if (argsIsLessThen(1, args)) {
            System.err.println("Missing Argument(s) in String[] args");
            System.exit(1);
        }
    }
}
