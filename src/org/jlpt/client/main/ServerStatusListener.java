package org.jlpt.client.main;

/**
 * A listener for receiving server status events.
 * 
 * @author BJ Peter DeLaCruz
 */
public interface ServerStatusListener {

  /**
   * Called when the status of the server has changed.
   * 
   * @param status The status of the server, either <code>ONLINE</code> or <code>OFFLINE</code>.
   */
  public void serverStatusChanged(ServerStatus status);

}
