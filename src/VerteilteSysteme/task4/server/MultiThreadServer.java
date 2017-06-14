package task4.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MultiThreadServer implements Runnable {
   private Socket clientSocket;
   private PrintWriter output;
   private BufferedReader input;
   private static int connectedClients = 0;
   private static int maxConnectedClients = 5;
   private static ArrayList<String> clients = new ArrayList<>();

   MultiThreadServer(Socket clientSocket) {
      try {
         this.clientSocket = clientSocket;
         BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
            Socket socket = serverSocket.accept();
            connectedClients++;
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
         welcome();
         processRequest();
      } catch (IOException e) {
         System.out.println(e);
      }
   }

   private void send(String message) {
      this.output.println(message);
   }

   private String receive() throws IOException {
      return input.readLine();
   }

   private void welcome() {
      send("Welcome");
   }

   private void processRequest() throws IOException {
      String inputLine;
      while ((inputLine = input.readLine()) != null) {
         if (inputLine.equals("time")) time();
         else send(inputLine);
      }
   }

   private static boolean reachedMaximumOfConnectedClients() {
      return !(connectedClients <= maxConnectedClients);
   }

   private void loginClient() {
      send("Please enter your username");
      try {
         System.out.println(receive());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void logoutClient() throws IOException {
      this.output.close();
      this.clientSocket.close();
      connectedClients--;
      System.out.println("Client logged out [" + connectedClients + "/" + maxConnectedClients + "]");
   }

   private void time() {
      Date date = new Date();
      send(String.format("Current Time: %tc", date));
   }
}