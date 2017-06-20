package task4.util;

import com.google.gson.annotations.SerializedName;

public class Request {

   public Request(int sequenceNumber, String command, String[] parameters) {
      this.req = new req(sequenceNumber, command, parameters);
   }

   private req req;

   private class req {
      @SerializedName("seq")
      private final int sequenceNumber;

      @SerializedName("cmd")
      private final String command;

      @SerializedName("params")
      private final String[] parameters;

      private req(int sequenceNumber, String command, String[] parameters) {
         this.sequenceNumber = sequenceNumber;
         this.command = command;
         this.parameters = parameters;
      }
   }

   public int getSequenceNumber() {
      return this.req.sequenceNumber;
   }

   public String getCommand() {
      return this.req.command;
   }

   public String[] getParameters() {
      return this.req.parameters;
   }


}
