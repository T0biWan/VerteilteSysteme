package task3.exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {}

    public WrongPasswordException(String msg) {
        super(msg);
    }
}
