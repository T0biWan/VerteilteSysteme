package task1And2.support;

/**
 * Created by Tobias on 03.05.2017.
 */
public class Support {
    public String[] splitInputArguments(String inputLine) {
        String[] arguments;
        if (inputLine.contains(" ")) {
            arguments = inputLine.split(" ");
            arguments[0] = arguments[0].toLowerCase();
            return arguments;
        } else {
            arguments = new String[1];
            arguments[0] = inputLine;
            return arguments;
        }
    }

    public boolean argsIsLessThen(int numberOfArguments, String[] args) {
        return args.length < numberOfArguments;
    }

    public void shutDownIfNotEnoughArguments(String[] args) {
        if (argsIsLessThen(1, args)) {
            System.err.println("Missing Argument(s) in String[] args");
            System.exit(1);
        }
    }

}
