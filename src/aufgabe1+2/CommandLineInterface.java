import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class CommandLineInterface {
    private static String IP = "127.0.0.1"; // localhost
    private static int port = 11111;
    private static boolean isRunning = true;
    private static String prompt = "(;,,;) $>\t";
    private static String space = "\t\t\t";
    private static Scanner scanner = new Scanner(System.in);
    private static String userInput = "";

    public static void main(String[] args){
        if (args[0].length() > 0) IP = args[0].substring(0, args[0].length()-1);
        if (args[1].length() > 1) port = Integer.parseInt(args[1]);
        try {
            Socket socket = new Socket(IP, port);
            System.out.println("Connected to Server on IP: " + IP + " and Port: " + port);
            while(isRunning) {
                System.out.print(prompt);
                userInput = scanner.nextLine();
                if (userInput.equals("help") || userInput.equals("h")) {
                    System.out.println(space + "CTHULHU!");
                    System.out.println();
                }

                if (userInput.startsWith("fibonacci") || userInput.startsWith("f")) {
                    int number = Integer.parseInt(getUserInputArgument(userInput)); // TODO prüfen ob es wirklich zahl ist
                    writeIntToServer(socket, number);
                    String answer = readFromServer(socket);
                    System.out.println(space + "Fibonacci of " + number + " is " + answer);
                    System.out.println();
                }

                if (userInput.equals("reverse")) {

                }

                if (userInput.equals("x")) {
                    //String input = getUserInputArgument(userInput);
                    writeStringToServer(socket, userInput);
                    String answer = readFromServer(socket);
                    System.out.println(space + answer);
                    System.out.println();
                }

                if (userInput.equals("cthulhu")) {

                }

                if (userInput.equals("end")) {
                    isRunning = false;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeIntToServer(Socket socket, int message) throws IOException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    private static void writeStringToServer(Socket socket, String message) throws IOException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    private static String readFromServer(Socket socket) throws IOException { // TODO BufferedReader verändern
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //String message = bufferedReader.readLine();

        char[] buffer = new char[200];
        int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert bis Nachricht empfangen
        String message = new String(buffer, 0, anzahlZeichen);
        return message;
    }

    private static String getUserInputArgument(String input) { // TODO prüfen ob überhaupt argument vorhandne ist
        return input.split(" ")[1];
    }
}
