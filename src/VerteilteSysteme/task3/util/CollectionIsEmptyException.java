package task3.util;

/**
 * Created by Tobi on 26.05.2017.
 */
public class CollectionIsEmptyException extends Throwable {
   public CollectionIsEmptyException() {}

   public CollectionIsEmptyException(String msg) {
      super(msg);
   }
}
