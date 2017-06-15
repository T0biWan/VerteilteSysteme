package task4.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {
   private static String IP = "127.0.0.1";
   private static int port = 8090;
   private static boolean isRunning = true;
   private static Scanner scanner = new Scanner(System.in);
   private static String prompt = "$> ";
   private static Socket socket;
   private static PrintWriter outputStream;
   private static BufferedReader inputStream;

   public static void main(String[] args) throws IOException {
      setIpAndPort(args);
      try {
         socket = new Socket(IP, port);
         outputStream = new PrintWriter(socket.getOutputStream(), true);
         inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

         commands();
         System.out.println("-------------------------");
         if (inputStream.ready()) System.out.println(receive());
         while (isRunning) {
            if (inputStream.ready()) System.out.println(receive());
            processUserInput(getUserInput());
         }

         inputStream.close();
         outputStream.close();
         socket.close();
         scanner.close();
      } catch (UnknownHostException e) {
         System.err.println("Don't know about host " + IP);
         System.exit(1);
      } catch (IOException e) {
         System.err.println("Couldn't get I/O for the connection to " + IP);
         System.exit(1);
      }
   }

   private static void setIpAndPort(String[] args) {
      if (args.length == 2) {
         IP = args[0];
         port = Integer.parseInt(args[1]);
      }
   }

   private static String getUserInput() {
      System.out.print(prompt);
      return scanner.nextLine();
   }

   private static void processUserInput(String userInput) throws IOException {
      List<String> tokenisedInput = tokenise(userInput);
      String command = tokenisedInput.get(0);
      if (command.equals("logout")) logout();
      else if (command.equals("login")) login(tokenisedInput.get(1));
      else if (command.equals("time")) time();
      else if (command.equals("who")) who();
      else if (command.equals("commands")) commands();
      else if (command.equals("chat")) chat(tokenisedInput.get(1) + " " + tokenisedInput.get(2));
      else if (command.equals("note")) note(userInput);
      else if (command.equals("notes")) notes();
      else echo(userInput);
      System.out.println();
   }

   private static void echo(String userInput) throws IOException {
      send(userInput);
      System.out.println(receive());
   }

   private static void send(String message) {
      outputStream.println(message);
   }

   private static String receive() throws IOException {
      return inputStream.readLine();
   }

   private static void logout() throws IOException {
      send("logout");
      System.out.println(receive());
      System.out.println("Bye");
      isRunning = false;
   }

   private static void login(String argument) throws IOException {
      send("login " + argument);
      System.out.println(receive());
   }

   private static List<String> tokenise(String string) {
      return new ArrayList<String>(Arrays.asList(string.split(" ")));
   }

   private static void time() throws IOException {
      send("time");
      System.out.println(receive());
   }

   private static void who() throws IOException {
      send("who");
      System.out.println(receive());
   }

   private static void chat(String userInput) throws IOException {
      send("chat "+userInput);
      System.out.println(receive());
   }

   private static void note(String text) throws IOException {
      send("note " + text);
      System.out.println(receive());
   }
   private static void notes() throws IOException {
      send("notes");
      System.out.println(receive());
   }

   private static void commands() throws IOException {
      String commands = "Available commands:\n" +
              "login <username>\n" +
              "logout\n" +
              "time\n" +
              "who\n" +
              "commands\n" +
              "chat <username> <message>\n" +
              "note <text>\n" +
              "notes";
      System.out.println(commands);
   }
}