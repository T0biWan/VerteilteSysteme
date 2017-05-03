package client;

import support.Support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommandLineInterface {
    private static String prompt = "$>\t";
    private static String space = "\t";
    private static Support s = new Support();
    private static String IP;
    private static int port;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static BufferedReader stdIn;
    private static String userInput;
    private static String[] arguments;

    public static void main(String[] args) throws IOException {
        if (s.argsIsLessThen(2, args)) { // todo auslagenr in Methode, dazu müssen variablen static sein und außerhalb der main liegen
            IP = "127.0.0.1";
            port = 7;
        } else {
            IP = args[0];
            port = Integer.parseInt(args[1]);
        }

        try {
            socket = new Socket(IP, port);
            System.out.println("Connected to Server on IP: " + IP + " and Port: " + port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.print(prompt);

            while ((userInput = stdIn.readLine()) != null) {
                arguments = s.splitInputArguments(userInput);
                String command = arguments[0];
                if (command.equals("f") || command.equals("fibonacci")) {
                    fibonacci();
                }
                if (command.equals("h") || command.equals("help")) {

                }
                if (command.equals("e") || command.equals("end")) {

                }
                if (command.equals("h") || command.equals("help")) {

                }
                if (command.equals("r") || command.equals("reverse")) {

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

    private static void fibonacci() throws IOException {
        out.println("fibonacci "+ arguments[1]);
        System.out.println(space + "Fibonacci of " + arguments[1] + " is " + in.readLine());
        System.out.println();
    }

}
