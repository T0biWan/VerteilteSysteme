package task3.client;

import task3.util.PinboardInterface;
import task3.exceptions.MessageTooLongException;
import task3.exceptions.UnknownCommandException;
import task3.exceptions.WrongPasswordException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
   private static boolean isRunning = true;
   private static Scanner scanner = new Scanner(System.in);
   private static Map<String, String> codes = new HashMap<>();
   private static PinboardInterface pinboard;

   public static void main(String[] args) throws RemoteException, NotBoundException {
      setCodes();
      Registry registry = LocateRegistry.getRegistry();
      pinboard = (PinboardInterface) registry.lookup("Pinboard");
      boolean validPassword = authentification();

      if (validPassword) {
         System.out.println(getCommands() + "\n");
         while (isRunning) processUserInput(getUserInput());
      }
   }

   private static boolean authentification() throws RemoteException {
      while (true) {
         String password = getPassword();
         try {
            if (!passwordIsValid(password)) throw new WrongPasswordException();
            else {
               System.out.println(codes.get("validPassword") + "\n");
               break;
            }
         } catch (WrongPasswordException e) {
            System.out.println(codes.get("wrongPassword"));
            System.out.println();
         }
      }
      return true;
   }

   private static void processUserInput(String input) throws RemoteException {
      try {
         if (input.equals(codes.get("countMessages"))) countMessages();
         else if (input.equals(codes.get("getMessage"))) getMessage();
         else if (input.equals(codes.get("getAllMessages"))) getAllMessages();
         else if (input.equals(codes.get("pinNewMessage"))) pinNewMessage();
         else if (input.equals(codes.get("help"))) help();
         else if (input.equals(codes.get("end"))) end();
         else throw new UnknownCommandException();
      } catch (UnknownCommandException e) {
         unknownCommand();
      }
      System.out.println();
   }

   private static void unknownCommand() {
      System.out.println(codes.get("unknownCommand"));
   }

   private static void end() {
      System.out.println(codes.get("goodBye"));
      isRunning = false;
   }

   private static void pinNewMessage() throws RemoteException {
      System.out.print(codes.get("askForMessage"));
      String message = scanner.nextLine();
      try {
         if (message.length() > 160) throw new MessageTooLongException();
      } catch (MessageTooLongException e) {
         System.out.println(codes.get("messageTooLong"));
      }
      pinboard.putMessage(message);
   }

   private static void getAllMessages() throws RemoteException {
      System.out.println(arrayToString(pinboard.getMessages()));
   }

   private static void help() throws RemoteException {
      System.out.println(getCommands());
   }

   private static void countMessages() throws RemoteException {
      System.out.println(pinboard.getMessageCount());
   }

   private static void getMessage() throws RemoteException {
      System.out.print(codes.get("askForMessageIndex"));
      String index = scanner.nextLine();
      System.out.println(pinboard.getMessage(index).toString() + "\n");
   }

   private static String getCommands() {
      String spacer = "\t- ";
      String commands = codes.get("countMessages") + spacer + "Amount of stored messages\n"
              + codes.get("getMessage") + spacer + "Get message\n"
              + codes.get("getAllMessages") + spacer + "Get all messages\n" +
              codes.get("pinNewMessage") + spacer + "Pin a new message\n" +
              codes.get("help") + spacer + "List commands\n" +
              codes.get("end") + spacer + "Log out";
      return commands;
   }

   private static boolean passwordIsValid(String password) throws RemoteException {
      return pinboard.login(password);
   }

   private static String getPassword() {
      System.out.print(codes.get("getPassword"));
      return scanner.nextLine();
   }

   private static String getUserInput() {
      System.out.print(codes.get("prompt"));
      return scanner.nextLine();
   }

   private static void setCodes() {
      codes.put("getPassword", "Enter your Password: ");
      codes.put("wrongPassword", "Wrong password!");
      codes.put("validPassword", "Successfully logged in");
      codes.put("countMessages", "cm");
      codes.put("getMessage", "gm");
      codes.put("getAllMessages", "gam");
      codes.put("pinNewMessage", "pin");
      codes.put("end", "end");
      codes.put("prompt", "$> ");
      codes.put("separator", "-----------------------");
      codes.put("unknownCommand", "Unknown command");
      codes.put("help", "help");
      codes.put("goodBye", "Bye");
      codes.put("askForMessage", "Message: ");
      codes.put("askForMessageIndex", "Index: ");
      codes.put("messageTooLong", "No message longer than 160 characters");
   }

   public static String arrayToString(ArrayList<String> array) {
      if (array.size() == 0 || array == null)
         return "[]";

      StringBuilder sb = new StringBuilder();
      for (int index = 0; index < array.size(); index++) {
         sb.append("[ " + array.get(index) + " ]" + "\n");
      }
      return sb.toString();
   }

}
