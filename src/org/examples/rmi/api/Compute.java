package org.examples.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface for the compute engine.
 * 
 * @author BJ Peter DeLaCruz
 */
public interface Compute extends Remote {

  /**
   * Executes the given task.
   * 
   * @param <T> The data type of the result of this task.
   * @param t The task to execute.
   * @return The results of the given task.
   * @throws RemoteException If a communication or protocol error has occurred.
   */
  <T> T executeTask(Task<T> t) throws RemoteException;
}
