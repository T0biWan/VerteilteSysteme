package task3.util;

import task3.exceptions.NowMessageAtThisIndexException;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Pinboard extends UnicastRemoteObject implements PinboardInterface {
    String clientHost;
    static String nameOfService = "Pinboard";
    private static final long serialVersionUID = 1L;
    private HashSet<String> clients = new HashSet<>();
    private static final String defaultPassword = "guest";
    private ArrayList<Message> pinboard = new ArrayList<>();
    private long maxMessageLifeTimeInSeconds = 600;
    private int maxAmountOfMessages = 20;
    private int maxLengthOfMessage = 160;

    protected Pinboard() throws RemoteException {
        super();
    }

    @Override
    public boolean login(String password) throws RemoteException {
        System.out.println("Login... entered password:\t" + password);
        if (validPassword(password)) {
            try {
                clientHost = getClientHost();
                System.out.println("Getting client host... " + clientHost);
                clients.add(clientHost);
            } catch (ServerNotActiveException e) {
                System.out.println("Server Error");
            }
            System.out.println("Success");
            return true;
        }
        return false;
    }

    private boolean validPassword(String password) {
        System.out.println("Password is valid");
        return defaultPassword.equals(password);
    }

    @Override
    public int getMessageCount() throws RemoteException {
        System.out.println("Count Messages");
        if (loginTest()) {
            return pinboard.size();
        }
        System.out.println("User is not authenticated");
        return 0;
    }

    @Override
    public String getMessage(String index) throws RemoteException {
        if (loginTest()) {
            deleteToOldMessages();
            if (isValidInteger(index)) {
                int messageIndex = Integer.parseInt(index);
                ArrayList<String> messages = getMessages();
                try {
                    if (messageIndex > pinboard.size() -1 || messageIndex < 0) throw new NowMessageAtThisIndexException();
                    else return messages.get(messageIndex);
                } catch (NowMessageAtThisIndexException e) {
                    return "No message at this index";
                }
            }
        }
        return "Server Error";
    }

    @Override
    public boolean loginTest() throws RemoteException {
        String clientHost;
        try {
            clientHost = getClientHost();
            System.out.println("Success");
        } catch (ServerNotActiveException e) {
            System.out.println("Server Error");
            return false;
        }
        if (clients.contains(clientHost)) {
            System.out.println(clientHost);
            return true;
        }
        return false;
    }

// todo Exceptions werfen bei sicherheitsprüfung
// todo Sysos überarbeiten

    @Override
    public boolean putMessage(String msg) throws RemoteException {
        if (loginTest()) {
            deleteToOldMessages();
            if (msg.length() <= maxLengthOfMessage && pinboard.size() <= maxAmountOfMessages) {
                Message stringMessage = new Message(msg);
                pinboard.add(stringMessage);
                return true;
            } else
                System.out.println("Message too long");
            return false;
        }
        System.out.println("Host Error");
        return false;
    }

    @Override
    public ArrayList<String> getMessages() throws RemoteException {
        long nowInMilliseconds = new Date().getTime();

        if (loginTest()) {
            deleteToOldMessages();
            ArrayList<String> ArrayNew = new ArrayList<String>();
            if (pinboard.size() == 0) {
                System.out.println("...");
                return ArrayNew;
            } else {
                for (Message message : pinboard) {
                    long lifetimeInSeconds = ((nowInMilliseconds - message.getMillisecondsSinceUnixTimestamp()) / 1000);
                    if (lifetimeInSeconds <= maxMessageLifeTimeInSeconds) {
                        System.out.println("[" + (lifetimeInSeconds - maxMessageLifeTimeInSeconds) +"s remaining]" + message.toString());
                        System.out.println("Remaining for "+ (maxMessageLifeTimeInSeconds -lifetimeInSeconds) + "s " + message.toString());
                        ArrayNew.add(message.toString());
                    }
                }
                System.out.println("Successfully returned Array");
                return ArrayNew;
            }
        }
        System.out.println("Host Error");
        return null;
    }

    public static boolean isValidInteger(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void deleteToOldMessages() {
        long nowInMilliseconds = new Date().getTime();

        if (pinboard.size() > 0) {
            for (Message message : pinboard) {
                long lifetimeInSeconds = ((nowInMilliseconds - message.getMillisecondsSinceUnixTimestamp()) / 1000);
                if (lifetimeInSeconds > maxMessageLifeTimeInSeconds) {
                    pinboard.remove(message);
                }
            }
        }
    }

}
