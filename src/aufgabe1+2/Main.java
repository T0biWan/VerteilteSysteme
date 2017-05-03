import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Main {
    public static void main(String args[]) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 11111);
            PrintStream ps = new PrintStream(socket.getOutputStream(), true);
            BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Write
            ps.println("Hallo Welt!");
            // Read
            while (buff.ready()) {
                System.out.println(buff.readLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}