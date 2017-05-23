package task1And2.client;

import task1And2.support.Support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommandLineInterfaceWithStrings {
    private static String prompt = "$>\t";
    private static String space = "\t";
    private static Support s = new Support();
    private static String defaultIP = "127.0.0.1";
    private static int defaultPort = 7;
    private static String IP;
    private static int port;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static BufferedReader stdIn;
    private static String userInput;
    private static String[] arguments;

    public static void main(String[] args) throws IOException {
        setIpAndPort(args);

        try {
            socket = new Socket(IP, port);
            System.out.println("Connected to ServerWithStrings on IP: " + IP + " and Port: " + port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Welcome! Type \"h\" or \"help\" for instructions.");
            System.out.print(prompt);

            while ((userInput = stdIn.readLine()) != null) { // todo falschen input abfangen
                arguments = s.splitInputArguments(userInput);
                String command = arguments[0];
                if (command.equals("h") || command.equals("help")) {
                    String helpMessage = space + "h | help\t\t\t\t\tInstructions\n"
                            + space + "e | end\t\t\t\t\t\tEnd Application\n"
                            + space + "f | fibonacci\t<Number>\tGet Fibonacci-Number\n"
                            + space + "r | reverse\t\t<Strihng>\tGet Reversed String";
                    System.out.println(helpMessage);
                    System.out.println();
                }
                if (command.equals("f") || command.equals("fibonacci")) {
                    fibonacci();
                }
                if (command.equals("r") || command.equals("reverse")) {
                    reverse();
                }
                if (command.equals("e") || command.equals("end")) {
                    System.exit(0);
                }
                System.out.print(prompt);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + IP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + IP);
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private static void fibonacci() throws IOException {
        out.println("fibonacci " + arguments[1]);
        System.out.println(space + "Fibonacci of " + arguments[1] + " is " + in.readLine());
        System.out.println();
    }

    private static void reverse() throws IOException {
        String string = "";
        for (int i = 1; i < arguments.length; i++) {
            string += arguments[i] + " ";
        }
        out.println("reverse " + string);
        System.out.println(space + in.readLine());
        System.out.println();
    }

}
