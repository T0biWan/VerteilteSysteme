package task1And2.client;

import task1And2.support.Support;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandLineInterface {
    private static String prompt = "$>\t";
    private static String space = "\t";
    private static Support s = new Support();
    private static String defaultIP = "127.0.0.1";
    private static int defaultPort = 7;
    private static String IP;
    private static int port;
    private static boolean clientIsRunning = true;
    private static Map<String, Integer> codes = new HashMap<>();
    private static int minimum = 0;
    private static int maximum = 20;

    public static void main(String[] args) throws IOException {
        setCodes();
        setIpAndPort(args);
        System.out.println("Connected to ServerWithStrings on IP: " + IP + " and Port: " + port);
        System.out.println("Welcome! Type " + codes.get("help") + " for instructions.");
        while (clientIsRunning) {
            try (
                    Socket socket = new Socket(IP, port);
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
            ) {
                Scanner scanner = new Scanner(System.in);
                System.out.print(prompt);
                int userInput = scanner.nextInt();
                if (userInput == codes.get("help"))  help();
                else if (userInput == codes.get("end"))  end();
                else if (numberIsWithinBoundaries(userInput, minimum, maximum)) fibonacci(userInput, output, input);
                else wrongInput();

            } catch (RuntimeException e) {
                wrongInput();
            }
        }
    }

    private static void end() {
        System.out.println(space + "Bye!");
        clientIsRunning = false;
    }

    private static void help() {
        String helpMessage = space + codes.get("help") + "\t\t\t\t\t| Instructions\n"
                + space + codes.get("end") + "\t\t\t\t\t| End Application\n"
                + space + "Positive Integer\t| Get Fibonacci-Number";
        System.out.println(helpMessage);
        System.out.println();
    }

    private static void fibonacci(int number, DataOutputStream out, DataInputStream in) throws IOException {
        out.writeInt(number);
        System.out.println(space + "Fibonacci of " + number + " is " + in.readInt());
        System.out.println();
    }

    private static void wrongInput() {
        System.out.println(space + "Wrong Input");
        System.out.println();
    }

    private static void setIpAndPort(String[] args) {
        if (s.argsIsLessThen(2, args)) {
            IP = defaultIP;
            port = defaultPort;
        } else {
            IP = args[0];
            port = Integer.parseInt(args[1]);
        }
    }

    private static void setCodes() {
        codes.put("help", -11);
        codes.put("end", -12);
    }

    private static boolean numberIsWithinBoundaries(int number, int minimum, int maximum) {
        return !(number < minimum || number > maximum);
    }
}