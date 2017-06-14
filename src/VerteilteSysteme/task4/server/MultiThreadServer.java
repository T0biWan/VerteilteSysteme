package task4.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MultiThreadServer implements Runnable {
   private Socket clientSocket;
   private PrintWriter output;
   private BufferedReader input;
   private static int connectedClients = 0;
   private static int maxConnectedClients = 5;
   private static List<String> clients = new ArrayList<>();
   private String client;

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
         verifyLogin();
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

   private void verifyLogin() throws IOException {
      send("Welcome, please login:");
      String inputLine;
      while ((inputLine = input.readLine()) != null) {
         if (!alreadyLogedIn(inputLine)) {
            this.client = inputLine;
            clients.add(this.client);
            send("Hi "+this.client+"! You are now succesfully loged in");
            break;
         }
         else send("Wrong login, please try again");
      }
   }

   private void processRequest() throws IOException {
      String inputLine;
      while ((inputLine = input.readLine()) != null) {
         if (inputLine.equals("time")) time();
         else if (inputLine.equals("logout")) {
            logoutClient();
            break;
         }
         else send(inputLine);
      }
   }

   private boolean alreadyLogedIn(String client) {
      return clients.contains(client);
   }

   private static boolean reachedMaximumOfConnectedClients() {
      return !(connectedClients <= maxConnectedClients);
   }

   private void logoutClient() throws IOException {
      send("Successfully logged out");
      this.output.close();
      this.clientSocket.close();
      connectedClients--;
      clients.remove(this.client);
      System.out.println("Client: "+this.client+" logged out [" + connectedClients + "/" + maxConnectedClients + "]");
   }

   private void time() {
      Date date = new Date();
      send(String.format("Current Time: %tc", date));
   }
}