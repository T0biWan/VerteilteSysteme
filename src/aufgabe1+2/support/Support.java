package support;

/**
 * Created by Tobias on 03.05.2017.
 */
public class Support {
    public String[] splitInputArguments(String inputLine) throws Exception {
        String[] arguments;
        if (inputLine.contains(" ")) {
            arguments = inputLine.split(" ");
            arguments[0] = arguments[0].toLowerCase();
            return arguments;
        } else {
            throw new Exception();
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
