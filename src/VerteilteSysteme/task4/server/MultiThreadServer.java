package task4.server;

import task4.exceptions.NotEnoughInputTokensException;
import task4.util.ClientDataModel;
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

import com.google.gson.Gson;
import task4.util.Request;
import task4.util.Response;

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
   private Request req;
   private Response res;
   private Gson gson = new Gson();
   private boolean clientLoggedIn = false;

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
         if (reachedMaximumOfConnectedClients()) toManyClients();
         else {
            verifyLogin();
            if (clientLoggedIn) processRequest();
            else clientIsNotLoggedIn();
         }
      } catch (SocketException e) {
         System.out.println(e);
      } catch (IOException e) {
         System.out.println(e);
      }
   }

   private void clientIsNotLoggedIn() {
      int status = 401;
      int sequenceNumber = 0;
      String message = "You are not logged in";
      Response response = new Response(status, sequenceNumber, new String[]{message});
      send(gson.toJson(response));
   }

   private void toManyClients() throws IOException {
      res = new Response(503, 0, new String[0]);
      send(gson.toJson(res));
      this.output.close();
      this.clientSocket.close();
      Thread.interrupted();
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

   private void verifyLogin() throws IOException {
      welcome();
      String inputLine;
      while ((inputLine = input.readLine()) != null) {
         req = gson.fromJson(inputLine, Request.class);
         int sequenceNumber = req.getSequenceNumber();
         String command = req.getCommand();
         String username = req.getParameters()[0];

         if (command.equals("login")) {
            if (!alreadyLoggedIn(username)) {
               this.client = new ClientDataModel(username, this.output, this.input);
               clients.add(this.client);
               System.out.println("New client " + this.client.toString() + " connected [" + clients.size() + "/" + maxConnectedClients + "]");
               send(gson.toJson(new Response(200, sequenceNumber, new String[]{"Hi " + this.client.toString() + "! You are now succesfully loged in"})));
               clientLoggedIn = true;
               break;
            } else send(gson.toJson(new Response(409, sequenceNumber, new String[]{"Client " + this.client.toString() + " is already logged in"})));
         } else send(gson.toJson(new Response(400, sequenceNumber, new String[]{"Wrong command, please try again"})));
      }
   }

   private void welcome() {
      int status = 200;
      int sequenceNumber = 0;
      String message = "Welcome, please login:";
      Response response = new Response(status, sequenceNumber, new String[]{message});
      send(gson.toJson(response));
   }

   private void processRequest() throws IOException {
      String inputLine;
      while ((inputLine = input.readLine()) != null) {
         try {
            req = gson.fromJson(inputLine, Request.class);
            String command = req.getCommand();
            if (command.equals("time")) time(req);
            else if (command.equals("who")) who(req);
            else if (command.equals("chat")) chat(req);
            else if (command.equals("note")) note(req);
            else if (command.equals("notes")) notes(req);
            else if (command.equals("notify")) notify(req);
            else if (command.equals("ls")) ls(req);
            else if (command.equals("logout")) {
               logoutClient(req);
               break;
            } else wrongCommand(req);
         } catch (NotEnoughInputTokensException e) {
            e.printStackTrace();
         }
      }
   }

   private void ls(Request req) {
      System.out.println(this.client + ".ls()");
      int status = 200;
      int sequenceNumber = req.getSequenceNumber();
      String path = (req.getParameters().length > 0) ? req.getParameters()[0] : "";

      String message = path;
      Response response = new Response(status, sequenceNumber, new String[]{message});
      send(gson.toJson(response));
   }

   private void wrongCommand(Request req) {
      System.out.println(this.client + ".wrongCommand()");
      int status = 501;
      int sequenceNumber = req.getSequenceNumber();
      String message = "Wrong command";
      Response response = new Response(status, sequenceNumber, new String[]{message});
      send(gson.toJson(response));
   }

   private boolean minimumArguments(int expected, int actual) {
      return actual >= expected;
   }

   private String[] removeCommand(List<String> input) {
      input.remove(0);
      String[] returnArr = new String[input.size()];
      for (int i = 0; i < input.size(); i++) returnArr[i] = input.get(i);
      return returnArr;
   }

   private boolean alreadyLoggedIn(String username) {
      for (ClientDataModel client : clients) {
         if (client.toString().equals(username)) return true;
      }
      return false;
   }

   private static boolean reachedMaximumOfConnectedClients() {
      return !(clients.size() <= maxConnectedClients);
   }

   private String[] removeFirstParameter(String[] input) {
      String[] returnArr = new String[input.length];
      for (int i = 1; i < input.length; i++) returnArr[i] = input[i];
      return returnArr;
   }

   private String concatParameters(String[] parameters) {
      String returnString = "";
      for (int i = 0; i < parameters.length; i++) returnString += parameters[i] + " ";
      return returnString;
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

   private void logoutClient(Request request) throws IOException {
      System.out.println(this.client + ".logout()");
      send(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{"Successfully logged out"})));
      this.output.close();
      this.clientSocket.close();
      clients.remove(this.client);
      System.out.println("Client: " + this.client + " disconnected [" + clients.size() + "/" + maxConnectedClients + "]");
   }

   private void time(Request request) {
      System.out.println(this.client + ".time()");
      Date date = new Date();
      send(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{String.format("Current Time: %tc", date)})));
   }

   private void who(Request request) {
      System.out.println(this.client + ".who()");
      String currentClients = "";
      int i = 0;
      for (ClientDataModel client : clients) {
         currentClients += client;
         i++;
         if (i < clients.size()) currentClients += ", ";
      }
      send(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{currentClients})));
   }

   private void chat(Request request) throws NotEnoughInputTokensException {
      if (!minimumArguments(2, request.getParameters().length)) throw new NotEnoughInputTokensException();
      System.out.println(this.client + ".chat()");
      String serverNotification = "User is offline";
      String username = request.getParameters()[0];
      String message = concatParameters(removeFirstParameter(request.getParameters()));
      for (ClientDataModel client : clients) {
         if (client.getUsername().equals(username)) {
            client.getOutput().println(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{client.getUsername() + ":\t" + message})));
            serverNotification = "Successfully send message";
            break;
         }
      }
      send(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{serverNotification})));
   }

   private void note(Request request) throws NotEnoughInputTokensException {
      if (!minimumArguments(1, request.getParameters().length)) throw new NotEnoughInputTokensException();
      System.out.println(this.client + ".note()");
      deleteToOldMessages();
      notes.add(new Note(this.client.toString(), concatParameters(request.getParameters())));
      System.out.println("Created note");
      send(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{"Successfully added note"})));
   }

   private void notes(Request request) {
      System.out.println(this.client + ".notes()");
      deleteToOldMessages();
      String placedNotes = "";
      int i = 0;
      for (Note note : notes) {
         placedNotes += note.toString();
         i++;
         if (i < notes.size()) placedNotes += ", ";
      }
      if (placedNotes.equals("")) placedNotes = "No placed notes";
      send(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{placedNotes})));
   }

   private void notify(Request request) throws NotEnoughInputTokensException {
      if (!minimumArguments(1, request.getParameters().length)) throw new NotEnoughInputTokensException();
      System.out.println(this.client + ".notify()");
      for (ClientDataModel client : clients) {
         if (client != this.client) client.getOutput().println(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{new Note(this.client.toString(), concatParameters(request.getParameters())).toString()})));
      }
      send(gson.toJson(new Response(200, request.getSequenceNumber(), new String[]{"Send notification"})));
   }
}