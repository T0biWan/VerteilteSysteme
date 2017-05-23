package task3;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

   private String msg;
   private String date;
   private long   TimeM;

   public Message(String msg) {
      this.msg = msg;
      this.date = timeStamp(new Date());
      this.TimeM = timeStampWithoutDate();
   }

   public long getTimeM() {
      return TimeM;
   }

   public String timeStamp(Date date) {
      DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
      String s = formatter.format(date);
      return s;
   }

   public long timeStampWithoutDate() {
      long timeM = System.currentTimeMillis();
      return timeM;
   }

   public String toString() {
      return "[" + date + "] " + msg;
   }

}
