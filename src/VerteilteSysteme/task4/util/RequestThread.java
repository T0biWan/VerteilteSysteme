package task4.util;

import com.google.gson.Gson;
import task4.exceptions.NotEnoughInputTokensException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RequestThread implements Runnable {
   private Socket socket;
   private PrintWriter outputStream;
   private Scanner scanner = new Scanner(System.in);
   private boolean isRunning = true;
   private String prompt = "$> ";
   private Integer sequenceNumber;
   private Random random = new Random();
   private Request req;
   private Gson gson = new Gson();

   public RequestThread(Socket socket) {
      this.socket = socket;
   }

   public void run() {
      try {
         this.outputStream = new PrintWriter(socket.getOutputStream(), true);
         while (isRunning) {
            processUserInput(getUserInput());
         }
         scanner.close();
         outputStream.close();
         socket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   private String getUserInput() {
      System.out.print(prompt);
      return scanner.nextLine();
   }

   private void processUserInput(String userInput) throws IOException {
      // Todo Sicherheitsabfrage, userinput darf nciht null sein, genauso wenig wie command und arguments
      List<String> input = tokenize(userInput);
      String command = input.get(0);
      try {
         if (command.equals("logout")) logout(input);
         else if (command.equals("login")) login(input);
         else if (command.equals("time")) time(input);
         else if (command.equals("who")) who(input);
         else if (command.equals("commands")) commands();
         else if (command.equals("chat")) chat(input);
         else if (command.equals("note")) note(input);
         else if (command.equals("notes")) notes(input);
         else if (command.equals("echo")) echo(input);
         else if (command.equals("notify")) notify(input);
         else if (command.equals("ls")) ls(input);
         else wrongCommand();
         System.out.println();
      } catch (NotEnoughInputTokensException e) {
         System.out.println("Missing Parameters");
      }
   }

   private void ls(List<String> input) throws NotEnoughInputTokensException {
      if (!minimumArguments(1, input.size())) throw new NotEnoughInputTokensException();
      int sequenceNumber = getSequenceNumber();
      String command = input.get(0);
      String path = (input.size() > 1) ? input.get(1) : ".";
      String message = filesUnderPath(Paths.get(path));
      Request request = new Request(sequenceNumber, command, new String[]{message});
      send(gson.toJson(request));
   }

   private String filesUnderPath(Path path) {
      String files = "";
      for (File file: new File(path.toString()).listFiles()) {
         files += file.toString() + "\n";
      }
      return files;
   }

   private List<String> tokenize(String string) {
      return new ArrayList<String>(Arrays.asList(string.split(" ")));
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

   private int getSequenceNumber() {
      if (sequenceNumber == null) sequenceNumber = random.nextInt(10000);
      return sequenceNumber++;
   }

   private void logout(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(1, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), new String[]{});
      send(gson.toJson(req));
      System.out.println("Bye");
      isRunning = false;
   }

   private void login(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(2, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), new String[]{input.get(1)});
      send(gson.toJson(req));
   }

   private void time(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(1, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), new String[]{});
      send(gson.toJson(req));
   }

   private void who(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(1, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), new String[]{});
      send(gson.toJson(req));
   }

   private void chat(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(3, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), removeCommand(input));
      send(gson.toJson(req));
   }

   private void note(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(2, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), removeCommand(input));
      send(gson.toJson(req));
   }

   private void notes(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(1, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), new String[]{});
      send(gson.toJson(req));
   }

   private void echo(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(1, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), removeCommand(input));
      send(gson.toJson(req));
   }

   private void notify(List<String> input) throws IOException, NotEnoughInputTokensException {
      if (!minimumArguments(1, input.size())) throw new NotEnoughInputTokensException();
      req = new Request(getSequenceNumber(), input.get(0), removeCommand(input));
      send(gson.toJson(req));
   }

   private void commands() throws IOException, NotEnoughInputTokensException {
      String commands = "Available commands:\n" +
              "login <username>\n" +
              "logout\n" +
              "time\n" +
              "who\n" +
              "commands\n" +
              "chat <username> <message>\n" +
              "note <text>\n" +
              "notes\n" +
              "ls [<path>]\n" +
              "notify <message>";
      System.out.println(commands);
   }

   private void wrongCommand() {
      System.out.println("Wrong command");
   }

   private void send(String message) {
      this.outputStream.println(message);
   }


}