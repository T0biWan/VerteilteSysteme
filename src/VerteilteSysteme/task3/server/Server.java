package task3.server;

import task3.util.Pinboard;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
   public static void main(String[] args) throws RemoteException {
      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
      Pinboard pinboard = new Pinboard();
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind("Pinboard", pinboard);

      System.out.println("Pinboard-Server is running and listens on Port: " + Registry.REGISTRY_PORT);
   }

}
