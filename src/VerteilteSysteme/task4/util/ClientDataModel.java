package task4.util;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ClientDataModel {
   private String username;
   private PrintWriter output;
   private BufferedReader input;

   public ClientDataModel(String username, PrintWriter output, BufferedReader input) {
      this.username = username;
      this.output = output;
      this.input = input;
   }

   @Override
   public String toString() {
      return username;
   }

   public String getUsername() {
      return username;
   }

   public PrintWriter getOutput() {
      return output;
   }
}
