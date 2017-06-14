package task4.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
   private String message;
   private Date timestamp;
   private String time;

   public Note(String message) {
      this.message = message;
      this.timestamp = new Date();
      this.time = new SimpleDateFormat("hh:mm:ss").format(timestamp);
   }

   public long getTimestampInMilliseconds() {
      return timestamp.getTime();
   }

   public String toString() {
      return "[" + time + "] " + message;
   }
}
