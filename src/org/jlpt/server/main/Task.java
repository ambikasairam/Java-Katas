package org.jlpt.server.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.AddRemoveRequest;
import org.jlpt.common.db.Commands;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.EntryAlreadyExistsException;
import org.jlpt.common.db.EntryDoesNotExistException;
import org.jlpt.common.db.FindRequest;
import org.jlpt.common.db.UpdateRequest;
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
    try (ObjectOutputStream ostream = new ObjectOutputStream(this.socket.getOutputStream())) {
      ostream.flush();
      try (ObjectInputStream istream = new ObjectInputStream(this.socket.getInputStream())) {
        while (true) {
          Object request = istream.readObject();
          if (request == Commands.QUIT) {
            break;
          }
          if (request instanceof AddRemoveRequest) {
            AddRemoveRequest addRemoveRequest = (AddRemoveRequest) request;
            String msg = "";
            switch (addRemoveRequest.getCommand()) {
            case ADD:
              this.databaseManager.addEntry(addRemoveRequest.getEntry());

              msg = "Successfully added entry to database. New entry: ";
              msg += addRemoveRequest.getEntry() + ".";
              LOGGER.log(Level.INFO, msg);
              break;
            case REMOVE:
              this.databaseManager.removeEntry(addRemoveRequest.getEntry());

              msg = "Successfully removed entry from database. Old entry: ";
              msg += addRemoveRequest.getEntry() + ".";
              LOGGER.log(Level.INFO, msg);
              break;
            default:
              msg = "Invalid command: " + addRemoveRequest.getCommand();
              throw new IllegalArgumentException(msg);
            }
          }
          else if (request instanceof UpdateRequest) {
            UpdateRequest updateRequest = (UpdateRequest) request;
            this.databaseManager.updateEntry(updateRequest.getNewEntry(),
                updateRequest.getOldEntry());

            String msg = "Successfully updated entry in database. Old entry: ";
            msg += updateRequest.getOldEntry() + ". ";
            msg += "New entry: " + updateRequest.getNewEntry() + ".";
            LOGGER.log(Level.INFO, msg);
          }
          else if (request instanceof FindRequest) {
            FindRequest findRequest = (FindRequest) request;
            List<JapaneseEntry> results = this.databaseManager.find(findRequest.getRegex());
            ostream.writeObject(results);

            String msg = "Processed search using regular expression: " + findRequest.getRegex();
            msg += ". Found ";
            if (results.size() == 1) {
              msg += "1 entry.";
            }
            else {
              msg += results.size() + " entries.";
            }
            LOGGER.log(Level.INFO, msg);
          }
          else if (request == Commands.GET) {
            List<JapaneseEntry> entries = this.databaseManager.getEntries();
            ostream.writeObject(entries);

            String msg = "Retrieved list of entries. Total: ";
            if (entries.size() == 1) {
              msg += "1.";
            }
            else {
              msg += entries.size() + ".";
            }
            LOGGER.log(Level.INFO, msg);
          }
          else if (request == Commands.SAVE) {
            ostream.writeObject(this.databaseManager.save());

            LOGGER.log(Level.INFO, "Successfully saved entries to database.");
          }
        }
      }
    }
    catch (IOException | EntryAlreadyExistsException | EntryDoesNotExistException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }

}
