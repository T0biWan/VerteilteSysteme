package task3.util;

import task3.exceptions.MessageTooLongException;
import task3.exceptions.NowMessageAtThisIndexException;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Pinboard extends UnicastRemoteObject implements PinboardInterface {
   String clientHost;
   static String nameOfService = "Pinboard";
   private static final long serialVersionUID = 1L;
   private HashSet<String> clients = new HashSet<>();
   private static final String defaultPassword = "guest";
   private ArrayList<Message> pinboard = new ArrayList<>();
   private long maxMessageLifeTimeInSeconds = 600;
   private int maxAmountOfMessages = 20;
   private int maxLengthOfMessage = 160;

   public Pinboard() throws RemoteException {
      super();
   }

   @Override
   public boolean login(String password) throws RemoteException {
      System.out.println("Login... entered password:\t" + password);
      if (validPassword(password)) {
         try {
            clientHost = getClientHost();
            System.out.println("Getting client host... " + clientHost);
            clients.add(clientHost);
         } catch (ServerNotActiveException e) {
            System.out.println("Server Error");
         }
         System.out.println("Success");
         return true;
      }
      return false;
   }

   private boolean validPassword(String password) {
      System.out.println("Password is valid");
      return defaultPassword.equals(password);
   }

   @Override
   public int getMessageCount() throws RemoteException {
      System.out.println("Count Messages");
      if (loginTest()) {
         return pinboard.size();
      }
      System.out.println("User is not authenticated");
      return 0;
   }

   @Override
   public String getMessage(String index) throws RemoteException {
      if (loginTest()) {
         deleteToOldMessages();
         if (isValidInteger(index)) {
            int messageIndex = Integer.parseInt(index);
            ArrayList<String> messages = getMessages();
            try {
               if (messageIndex > pinboard.size() - 1 || messageIndex < 0) throw new NowMessageAtThisIndexException();
               else return messages.get(messageIndex);
            } catch (NowMessageAtThisIndexException e) {
               return "No message at this index";
            }
         }
      }
      return "Server Error";
   }

   @Override
   public boolean loginTest() throws RemoteException {
      String clientHost;
      try {
         clientHost = getClientHost();
         System.out.println("Success");
      } catch (ServerNotActiveException e) {
         System.out.println("Server Error");
         return false;
      }
      if (clients.contains(clientHost)) {
         System.out.println(clientHost);
         return true;
      }
      return false;
   }

// todo Exceptions werfen bei sicherheitsprüfung
// todo Sysos überarbeiten

   @Override
   public boolean putMessage(String message) throws RemoteException {
      if (loginTest()) {
         deleteToOldMessages();
         try {
            if (message.length() > maxLengthOfMessage) throw new MessageTooLongException();
            if (pinboard.size() >= maxAmountOfMessages) throw new CollectionIsFullException();
            else {
               pinboard.add(new Message(message));
               return true;
            }
         } catch (MessageTooLongException e) {
            System.out.println("Message is too long");
            return false;
         } catch (CollectionIsFullException e) {
            System.out.println("Pinboard reached maximum amount of messages");
            return false;
         }
      }
      System.out.println("Server Error");
      return false;
   }

   @Override
   public ArrayList<String> getMessages() throws RemoteException {
      ArrayList<String> messages = new ArrayList<>();

      if (loginTest()) {
         deleteToOldMessages();
         try {
            if (pinboard.size() == 0) throw new CollectionIsEmptyException();
            else {
               for (Message message : pinboard) {
                  messages.add(message.toString());
               }
            }
            System.out.println("Successfully returned Array");
            return messages;
         } catch (CollectionIsEmptyException e) {
            System.out.println("Pinboard is empty");
            return messages;
         }
      }
      System.out.println("Server Error");
      return messages;
   }

   public static boolean isValidInteger(String number) {
      try {
         Integer.parseInt(number);
      } catch (NumberFormatException e) {
         return false;
      }
      return true;
   }

   public void deleteToOldMessages() {
      long nowInMilliseconds = new Date().getTime();

      if (pinboard.size() > 0) {
         for (Message message : pinboard) {
            long lifetimeInSeconds = ((nowInMilliseconds - message.getMillisecondsSinceUnixTimestamp()) / 1000);
            if (lifetimeInSeconds > maxMessageLifeTimeInSeconds) {
               pinboard.remove(message);
            }
         }
      }
   }

}
