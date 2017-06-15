package task4.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
   private String message;
   private Date timestamp;
   private String time;
   private String user;

   public Note(String user, String message) {
      this.message = message;
      this.timestamp = new Date();
      this.time = new SimpleDateFormat("hh:mm:ss").format(timestamp);
      this.user = user;
   }

   public long getTimestampInMilliseconds() {
      return timestamp.getTime();
   }

   public String toString() {
      return "[" + time + "] " + user + ":\t " + message;
   }
}
