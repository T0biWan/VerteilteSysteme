package task1And2.offline;

import task1And2.exceptions.InputException;

import java.util.Scanner;

public class OfflineClient {
    public final int minimum = 0;
    public final int maximum = 20;
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
        return !(n < minimum || n > maximum);
    }

    public int inputInt() {
        System.out.print("Input an Integer between [" + minimum + ", " + maximum + "]: ");
        int input = takeInput();
        if(inputIsWithinBoundaries(input)) return input;
        return -1;
    }
}
