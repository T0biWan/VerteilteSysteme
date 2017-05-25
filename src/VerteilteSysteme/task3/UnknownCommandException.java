package task3;

public class UnknownCommandException extends Exception {
   public UnknownCommandException() {
   }

   public UnknownCommandException(String msg) {
      super(msg);
   }

}