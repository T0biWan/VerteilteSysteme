package task3.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
   private String message;
   private Date timestamp;
   private String date;
   private String time;

   public Message(String message) {
      this.message = message;
      this.timestamp = new Date();
      this.date = new SimpleDateFormat("dd.MM.yyyy").format(timestamp);
      this.time = new SimpleDateFormat("hh:mm:ss").format(timestamp);
   }

   public long getMillisecondsSinceUnixTimestamp() {
      return timestamp.getTime();
   }

   public long getSecondsSinceUnixTimestamp() {
      return timestamp.getTime()/1000;
   }

   public String toString() {
      return "[" + time + "] " + message;
   }
}
