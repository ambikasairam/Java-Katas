package org.jlpt.server.main;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlpt.common.db.AddRemoveRequest;
import org.jlpt.common.db.Commands;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.utils.Validator;

/**
 * A Runnable that listens for requests from a client and processes them. One Runnable per client.
 * 
 * @author BJ Peter DeLaCruz
 */
public class Task implements Runnable {

  private static final Logger LOGGER = Logger.getGlobal();

  private final DbManager databaseManager;
  private final Socket socket;

  /**
   * Creates a new Task.
   * 
   * @param databaseManager The database manager.
   * @param socket The socket to which a client is connected.
   */
  public Task(DbManager databaseManager, Socket socket) {
    Validator.checkNull(databaseManager);
    Validator.checkNull(socket);

    this.databaseManager = databaseManager;
    this.socket = socket;
  }

  /** {@inheritDoc} */
  @Override
  public void run() {
    try (// ObjectOutputStream ostream = new ObjectOutputStream(this.socket.getOutputStream());
        ObjectInputStream istream = new ObjectInputStream(this.socket.getInputStream())) {
      while (true) {
        Object request = istream.readObject();
        if (request == Commands.QUIT) {
          break;
        }
        if (request instanceof AddRemoveRequest) {
          AddRemoveRequest addRemoveRequest = (AddRemoveRequest) request;
          switch (addRemoveRequest.getCommand()) {
          case ADD:
            this.databaseManager.addEntry(addRemoveRequest.getEntry());
            LOGGER.log(Level.INFO, "Successfully added entry to database.");
            break;
          case REMOVE:
            this.databaseManager.removeEntry(addRemoveRequest.getEntry());
            LOGGER.log(Level.INFO, "Successfully removed entry from database.");
            break;
          default:
            throw new IllegalArgumentException("Invalid command: " + addRemoveRequest.getCommand());
          }
        }
      }
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }

}
