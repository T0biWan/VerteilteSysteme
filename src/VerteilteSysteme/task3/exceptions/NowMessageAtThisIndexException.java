package task3.exceptions;

/**
 * Created by Tobi on 25.05.2017.
 */
public class NowMessageAtThisIndexException extends Exception {
   public NowMessageAtThisIndexException() {}

   public NowMessageAtThisIndexException(String msg) {
      super(msg);
   }
}
