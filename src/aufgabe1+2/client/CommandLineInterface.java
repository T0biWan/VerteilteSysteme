package client;

import support.Support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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

//    private static Socket socket;
//    private static PrintWriter out;
//    private static BufferedReader in;
//    private static BufferedReader stdIn;
//    private static String[] arguments;

    public static void main(String[] args) throws IOException {
        setCodes();
        setIpAndPort(args);
        System.out.println("Connected to ServerWithStrings on IP: " + IP + " and Port: " + port);
        System.out.println("Welcome! Type " + codes.get("help") + " for instructions.");
        while (clientIsRunning) {
            try (
                    Socket socket = new Socket(IP, port);
//                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            ) {
                Scanner scanner = new Scanner(System.in);
                System.out.print(prompt);
                int userInput = scanner.nextInt();
                if (userInput == codes.get("help"))  help();
                if (userInput == codes.get("end"))  end();

//            scanner.close(); // Schmeisst mit Exceptions um sich!
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        try {
//            socket = new Socket(IP, port);
//            System.out.println("Connected to ServerWithStrings on IP: " + IP + " and Port: " + port);
//
//            out = new PrintWriter(socket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            stdIn = new BufferedReader(new InputStreamReader(System.in));
//
//            System.out.println("Welcome! Type \"h\" or \"help\" for instructions.");
//            System.out.print(prompt);
//
//            while ((userInput = stdIn.readLine()) != null) {
//                arguments = s.splitInputArguments(userInput);
//                String command = arguments[0];
//                if (command.equals("h") || command.equals("help")) {
//                    String helpMessage = space + "h | help\t\t\t\t\tInstructions\n"
//                            + space + "e | end\t\t\t\t\t\tEnd Application\n"
//                            + space + "f | fibonacci\t<Number>\tGet Fibonacci-Number\n"
//                            + space + "r | reverse\t\t<Strihng>\tGet Reversed String";
//                    System.out.println(helpMessage);
//                    System.out.println();
//                }
//                if (command.equals("f") || command.equals("fibonacci")) {
//                    fibonacci();
//                }
//                if (command.equals("r") || command.equals("reverse")) {
//                    reverse();
//                }
//                if (command.equals("e") || command.equals("end")) {
//                    System.exit(0);
//                } else {
//                    out.println(command);
//                    System.out.println(space + "Fibonacci of " + command + " is " + in.readLine());
//                    System.out.println();
//                }
//                System.out.print(prompt);
//            }
//        } catch (UnknownHostException e) {
//            System.err.println("Don't know about host " + IP);
//            System.exit(1);
//        } catch (IOException e) {
//            System.err.println("Couldn't get I/O for the connection to " + IP);
//            System.exit(1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

//    private static void fibonacci() throws IOException {
//        out.println("fibonacci " + arguments[1]);
//        System.out.println(space + "Fibonacci of " + arguments[1] + " is " + in.readLine());
//        System.out.println();
//    }

}