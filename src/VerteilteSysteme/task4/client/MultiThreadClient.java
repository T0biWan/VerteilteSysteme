package task4.client;

import task4.util.ReceiveThread;
import task4.util.RequestThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MultiThreadClient {
   private static String IP = "127.0.0.1";
   private static int port = 8090;
   private static boolean isRunning = true;
   private static Scanner scanner = new Scanner(System.in);
   private static String prompt = "$> ";
   private static Socket socket;
   private static PrintWriter outputStream;
   private static BufferedReader inputStream;
   private int sequenceNumber;

   public static void main(String[] args) throws IOException {
      setIpAndPort(args);
      socket = new Socket(IP, port);
      outputStream = new PrintWriter(socket.getOutputStream(), true);
      Thread receiveThread = new Thread(new ReceiveThread(socket));
      Thread requestThread = new Thread(new RequestThread(socket));
      commands();
      System.out.println("-------------------------");
      receiveThread.start();
      requestThread.start();
   }

   private static void setIpAndPort(String[] args) {
      if (args.length == 2) {
         IP = args[0];
         port = Integer.parseInt(args[1]);
      }
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
              "notes\n" +
              "notify <message>";
      System.out.println(commands);
   }

}
