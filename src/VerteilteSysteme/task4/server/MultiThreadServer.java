package task4.server;

import task4.util.Note;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MultiThreadServer implements Runnable {
   private static int port = 8090;
   private static String IP = "127.0.0.1";
   private Socket clientSocket;
   private PrintWriter output;
   private BufferedReader input;
   private static int maxConnectedClients = 5;
   private static int noteLifeSpanInMilliseconds = 600000;
   private static List<ClientDataModel> clients = new ArrayList<>();
   private static List<Note> notes = new ArrayList<>();
   private ClientDataModel client;

   MultiThreadServer(Socket clientSocket) {
      try {
         this.clientSocket = clientSocket;
         this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         this.output = new PrintWriter(clientSocket.getOutputStream(), true);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void main(String args[]) throws Exception {
      setIpAndPort(args);
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("Server is running on " + IP + " and listens on Port: " + port);

      while (true) {
         Socket socket = serverSocket.accept();
         new Thread(new MultiThreadServer(socket)).start();
      }
   }

   public void run() {
      try {
         if (reachedMaximumOfConnectedClients()) {
            send("-100");
            this.output.close();
            this.clientSocket.close();
            return;
         }
         verifyLogin();
         processRequest();
      } catch (SocketException e) {
         System.out.println(e);
      } catch (IOException e) {
         System.out.println(e);
      }
   }

   private static void setIpAndPort(String[] args) {
      if (args.length == 2) {
         IP = args[0];
         port = Integer.parseInt(args[1]);
      }
   }

   private void send(String message) {
      this.output.println(message);
   }

   private String receive() throws IOException {
      return input.readLine();
   }

   private void tooManyClients() {
      Thread.interrupted();
   }

   private void welcome() {
      send("Welcome");
   }

   private void verifyLogin() throws IOException {
      send("Welcome, please login:");
      String inputLine;
      while ((inputLine = input.readLine()) != null) {
         List<String> tokenisedInput = tokenise(inputLine);
         if (tokenisedInput.size() == 2) {
            String command = tokenisedInput.get(0);
            String username = tokenisedInput.get(1);
            if (command.equals("login")) {
               if (!alreadyLogedIn(username)) {
                  this.client = new ClientDataModel(username, this.output, this.input);
//                  this.client = username;
                  clients.add(this.client);
//                  connectedClients++;
                  System.out.println("New client " + this.client + " connected [" + clients.size() + "/" + maxConnectedClients + "]");
                  send("Hi " + this.client + "! You are now succesfully loged in");
                  break;
               } else send("Client is already logged in");
            } else {
               send("Wrong command, please try again");
            }
         } else {
            send("Wrong command, please try again");
         }
      }
   }

   private void processRequest() throws IOException {
      String inputLine;
      while ((inputLine = input.readLine()) != null) {
         List<String> tokenisedInput = tokenise(inputLine);
         String command = tokenisedInput.get(0);
         if (command.equals("time")) time();
         else if (command.equals("who")) who();
         else if (command.equals("chat")) chat(tokenisedInput.get(1) + " " + tokenisedInput.get(2));
         else if (command.equals("note")) note(inputLine);
         else if (command.equals("notes")) notes();
         else if (command.equals("logout")) {
            logoutClient();
            break;
         } else send("echo: " + inputLine);
      }
   }

   private boolean alreadyLogedIn(String client) {
      return clients.contains(client);
   }

   private static boolean reachedMaximumOfConnectedClients() {
      return !(clients.size() <= maxConnectedClients);
   }

   private void logoutClient() throws IOException {
      send("Successfully logged out");
      this.output.close();
      this.clientSocket.close();
      clients.remove(this.client);
      System.out.println("Client: " + this.client + " disconnected [" + clients.size() + "/" + maxConnectedClients + "]");
   }

   private void time() {
      System.out.println(this.client + ": time()");
      Date date = new Date();
      send(String.format("Current Time: %tc", date));
   }

   private void chat(String commandAndArguments) {
      System.out.println(this.client + ": chat()");
      List<String> tokenisedInput = tokenise(commandAndArguments);
      if (tokenisedInput.size() == 2) {
         String username = tokenisedInput.get(0);
         String message = tokenisedInput.get(1);
         clients.stream().filter(client -> client.getUsername().equals(username)).forEach(client -> {
            client.getOutput().println(message);
         });
         send("Successfully send message");
      }
   }

   private void note(String note) {
      System.out.println(this.client + ": note()");
      deleteToOldMessages();
      notes.add(new Note(note.replace("note ", "")));
      System.out.println("Created note");
      send("Successfully added node");
   }

   private void notes() {
      System.out.println(this.client + ": notes()");
      deleteToOldMessages();
      String placedNotes = "";
      int i = 0;
      for (Note note : notes) {
         placedNotes += note.toString();
         i++;
         if (i < notes.size()) placedNotes += ", ";
      }
      send(placedNotes);
   }

   private void who() {
      System.out.println(this.client + ": who()");
      String currentClients = "";
      int i = 0;
      for (ClientDataModel client : clients) {
         currentClients += client;
         i++;
         if (i < clients.size()) currentClients += ", ";
      }
      send(currentClients);
   }

   private static List<String> tokenise(String string) {
      return new ArrayList<>(Arrays.asList(string.split(" ")));
   }

   public void deleteToOldMessages() {
      long timestamp = new Date().getTime();
      if (notes.size() > 0) {
         for (Note note : notes) {
            long lifetimeInSeconds = ((timestamp - note.getTimestampInMilliseconds()) / 1000);
            if (lifetimeInSeconds > noteLifeSpanInMilliseconds) {
               System.out.println("Delete Note: " + note);
               notes.remove(note);
            }
         }
      }
   }
}