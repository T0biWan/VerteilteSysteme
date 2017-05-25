package task3.exceptions;

/**
 * Created by Tobi on 25.05.2017.
 */
public class MessageTooLongException extends Exception {
   public MessageTooLongException() {}

   public MessageTooLongException(String msg) {
      super(msg);
   }
}
