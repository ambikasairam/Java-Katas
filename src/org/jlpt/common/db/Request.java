package org.jlpt.common.db;

import java.io.Serializable;

/**
 * A request that the client sends to the server.
 * 
 * @author BJ Peter DeLaCruz
 */
public interface Request extends Serializable {

  /** @return The command for the server to process. */
  public Commands getCommand();

}
