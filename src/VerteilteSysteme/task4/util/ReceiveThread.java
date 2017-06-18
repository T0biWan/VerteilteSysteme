package task4.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveThread implements Runnable {
   private boolean isRunning = true;
   private Socket socket;
   private BufferedReader inputStream;
   Response res;
   private Gson gson = new Gson();

   public ReceiveThread(Socket socket) {
      this.socket = socket;
   }

   public void run() {
      try {
         this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         while (isRunning) {
            processResponse();
         }
         inputStream.close();
         socket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void processResponse() throws IOException {
//      if (inputStream.ready()) {
      String input;
      while ((input = inputStream.readLine()) != null) {
//         String input = inputStream.readLine();
//         res = gson.fromJson(input, Response.class);
//         System.out.println(res.getData());
         System.out.println(input);
      }
   }
}
