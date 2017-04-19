import exception.InputException;

import java.util.Scanner;

public class OfflineClient {
    private Scanner scanner = new Scanner(System.in);

    private int takeInput () {
        int input = scanner.nextInt();
        scanner.close();
        return input;
    }

    private void checkInput(int n) throws InputException {
        if (inputIsWithinBoundaries(n)) throw new InputException();
    }

    private boolean inputIsWithinBoundaries(int n) {
        return !(n < 0 || n > 20);
    }

    public int inputInt() {
        System.out.print("Input an Integer between [0, 20]: ");
        int input = takeInput();
        if(inputIsWithinBoundaries(input)) return input;
        return -1;
    }
}
