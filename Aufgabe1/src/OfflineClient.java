import exception.InputException;

import java.util.Scanner;

public class OfflineClient {
    private Scanner scanner = new Scanner(System.in);

    private int takeInput () {
        return scanner.nextInt();
    }

    private void checkInput(int n) throws InputException {
        if (n < 0 || n > 20) throw new InputException();
    }

    public int inputInt() {
        System.out.print("Input an Integer between [0, 20]: ");
        int input = takeInput();
        try {
            checkInput(input);
        } catch (InputException e) {
            e.printStackTrace();
        }
        return input;
    }
}
