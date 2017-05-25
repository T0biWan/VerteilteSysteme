package task3.util;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PinboardInterface extends Remote {

   public boolean loginTest() throws RemoteException;

   public boolean login(String password) throws RemoteException;

   public int getMessageCount() throws RemoteException;

   public ArrayList<String> getMessages() throws RemoteException;

   public String getMessage(String index) throws RemoteException;

   public boolean putMessage(String msg) throws RemoteException;

}
