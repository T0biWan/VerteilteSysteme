package task4.util;

import com.google.gson.annotations.SerializedName;

public class Request {
   public class req {

   }
   @SerializedName("seq")
   private final int sequenceNumber;

   @SerializedName("cmd")
   private final String command;

   @SerializedName("params")
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
