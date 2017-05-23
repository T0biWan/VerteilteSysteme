package task3;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Pinboard extends UnicastRemoteObject implements PinboardInterface {

   protected Pinboard() throws RemoteException {
      super();
   }

   long messageLifeTime = 60;
   private HashSet<String> passwordList = new HashSet<String>();
   String clientHost;
   private static final long               serialVersionUID  = 1L;
   private              int                maxNumMessages    = 20;
   private              int                maxLengthMessages = 160;
   static               String             nameOfService     = "Pinboard";
   private              ArrayList<Message> pinnwandArray     = new ArrayList<Message>();
   private              ArrayList<Message> removeList        = new ArrayList<Message>();

   @Override public boolean login(String password) throws RemoteException {
      System.out.println("Password: " + password);

      if (password.equals("guest")) {
         try {
            clientHost = getClientHost();
            System.out.println("Getting client host..." + clientHost);
            passwordList.add(clientHost);
            System.out.println(passwordList.toString());
         } catch (ServerNotActiveException e) {
            System.out.println("Host Error");
         }
         return true;
      } else
         return false;
   }

   @Override public int getMessageCount() throws RemoteException {
      if (loginTest()) {
         return pinnwandArray.size();
      }
      System.out.println("Failed to get message...");
      return 0;
   }

   @Override public String getMessage(String index) throws RemoteException {
      if (loginTest()) {
         check();
         if (isInteger(index)) {
            ArrayList<String> messages = getMessages();
            int indexI = Integer.parseInt(index);
            if (indexI > pinnwandArray.size() - 1 || indexI < 0) {
               return "No message at this index";
            } else {
               return "Input has to be a number!";
            }
         }
      }
      System.out.println("Host Error");
      return null;
   }

   @Override public boolean putMessage(String msg) throws RemoteException {
      if (loginTest()) {
         check();
         if (msg.length() <= maxLengthMessages && pinnwandArray.size() <= maxNumMessages) {
            Message stringMessage = new Message(msg);
            pinnwandArray.add(stringMessage);
            return true;
         } else
            System.out.println("Message too long");
         return false;
      }
      System.out.println("Host Error");
      return false;
   }

   @Override public boolean loginTest() throws RemoteException {
      String clientHostTest;
      try {
         clientHostTest = getClientHost();
         System.out.println("Success");
      } catch (ServerNotActiveException e) {
         System.out.println("Host Error");
         return false;
      }
      if (passwordList.contains(clientHostTest)) {
         System.out.println(clientHostTest);
         return true;
      } else
         return false;
   }

   @Override public ArrayList<String> getMessages() throws RemoteException {
      Date actualDate = new Date();
      long actual = actualDate.getTime();

      if (loginTest()) {
         check();
         ArrayList<String> ArrayNew = new ArrayList<String>();
         if (pinnwandArray.size() == 0) {
            System.out.println("...");
            return ArrayNew;
         } else {
            for (Message msg : pinnwandArray) {
               long diff = ((actual - msg.getTimeM()) / 1000);
               if (diff <= messageLifeTime) {
                  System.out.println(actual + "actual");
                  System.out.println(diff + "diff");
                  System.out.println(messageLifeTime + ": Lifetime of Message");
                  ArrayNew.add(msg.toString());
               }
            }
            System.out.println("Successfully returned Array");
            return ArrayNew;
         }
      }
      System.out.println("Host Error");
      return null;
   }

   public static boolean isInteger(String string) {
      try {
         Integer.valueOf(string);
         return true;
      } catch (NumberFormatException e) {
         return false;
      }
   }

   public void check() {
      Date actualDate = new Date();
      long actual = actualDate.getTime();

      if (pinnwandArray.size() == 0) {} else {
         for (Message msg : pinnwandArray) {
            long diff = ((actual - msg.getTimeM()) / 1000);
            if (diff > messageLifeTime) {
               removeList.add(msg);
            }
         }
      }
      for (Message msg : removeList) {
         pinnwandArray.remove(msg);
      }

   }

}
