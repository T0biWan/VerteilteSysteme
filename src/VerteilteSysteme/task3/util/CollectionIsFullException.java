package task3.util;

/**
 * Created by Tobi on 26.05.2017.
 */
public class CollectionIsFullException extends Exception {
   public CollectionIsFullException() {}

   public CollectionIsFullException(String msg) {
      super(msg);
   }
}
