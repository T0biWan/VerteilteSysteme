package task3.exceptions;

public class UnknownCommandException extends Exception {
   public UnknownCommandException() {
   }

   public UnknownCommandException(String msg) {
      super(msg);
   }

}