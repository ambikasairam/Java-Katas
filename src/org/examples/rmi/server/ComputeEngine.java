package org.examples.rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.examples.rmi.api.Compute;
import org.examples.rmi.api.Task;

/**
 * A compute engine for executing tasks from a client.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ComputeEngine implements Compute {

  /** {@inheritDoc} */
  @Override
  public <T> T executeTask(Task<T> task) throws RemoteException {
    if (task == null) {
      throw new IllegalArgumentException("task is null");
    }
    return task.execute();
  }

  /**
   * Sets up the ComputeEngine.
   * 
   * @param args None.
   */
  public static void main(String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      String name = "Compute";
      Compute engine = new ComputeEngine();
      Compute stub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(name, stub);
      System.out.println("ComputeEngine bound.");
    }
    catch (Exception e) {
      System.err.println("ComputeEngine exception: ");
      e.printStackTrace();
    }
  }

}
