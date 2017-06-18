package task4.util;

import java.util.Arrays;

public class Request {
   // { req: { seq: Integer, cmd: String, params: [ String ] } }
   private final int sequenceNumber;
   private final String command;
   private final String[] parameters;

   public int getSequenceNumber() {
      return sequenceNumber;
   }

   public String getCommand() {
      return command;
   }

   public String[] getParameters() {
      return parameters;
   }

   public Request(int sequenceNumber, String command, String[] parameters) {
      this.sequenceNumber = sequenceNumber;
      this.command = command;
      this.parameters = parameters;
   }
}
