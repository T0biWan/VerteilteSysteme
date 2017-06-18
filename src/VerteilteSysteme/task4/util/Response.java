package task4.util;

public class Response {
   // { res: { status: Integer, seq: Integer, data: [ String ] } }
   private final int status;
   private final int sequenceNumber;
   private final String[] data;

   public Response(int status, int sequenceNumber, String[] data) {
      this.status = status;
      this.sequenceNumber = sequenceNumber;
      this.data = data;
   }

   public int getStatus() {
      return status;
   }

   public int getSequenceNumber() {
      return sequenceNumber;
   }

   public String[] getData() {
      return data;
   }
}
