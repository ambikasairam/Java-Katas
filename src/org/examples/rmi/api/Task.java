package org.examples.rmi.api;

import java.io.Serializable;

/**
 * The interface for tasks for the compute engine.
 * 
 * @author BJ Peter DeLaCruz
 * 
 * @param <T> The type of the results of this task.
 */
public interface Task<T> extends Serializable {

  /**
   * The task to execute.
   * 
   * @return The results of this task.
   */
  T execute();

}
