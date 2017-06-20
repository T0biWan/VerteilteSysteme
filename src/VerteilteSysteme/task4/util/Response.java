package task4.util;

import com.google.gson.annotations.SerializedName;

public class Response {
   public Response(int status, int sequenceNumber, String[] data) {
      this.res = new res(status, sequenceNumber, data);
   }

   private res res;

   private class res {
      private final int status;

      @SerializedName("seq")
      private final int sequenceNumber;

      private final String[] data;

      private res (int status, int sequenceNumber, String[] data) {
         this.status = status;
         this.sequenceNumber = sequenceNumber;
         this.data = data;
      }
   }

   public int getStatus() {
      return this.res.status;
   }

   public int getSequenceNumber() {
      return this.res.sequenceNumber;
   }

   public String[] getData() {
      return this.res.data;
   }
}
