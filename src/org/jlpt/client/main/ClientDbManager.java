package org.jlpt.client.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.AddRemoveRequest;
import org.jlpt.common.db.Commands;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.EntryAlreadyExistsException;
import org.jlpt.common.db.EntryDoesNotExistException;
import org.jlpt.common.db.FindRequest;
import org.jlpt.common.db.StaleEntryException;
import org.jlpt.common.db.UpdateRequest;
import org.jlpt.common.utils.Validator;

/**
 * A client-side proxy that sends requests to a server using a socket.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ClientDbManager implements DbManager {

  private static final Logger LOGGER = Logger.getGlobal();

  private final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);

  private final String hostname;
  private final int port;
  private final Socket socket;
  private final ObjectInputStream istream;
  private final ObjectOutputStream ostream;
  private ServerStatusListener listener;

  /**
   * Creates a new ClientDbManager.
   * 
   * @param hostname The hostname of the server.
   * @param port The server port.
   * @throws IOException If there are problems connecting to the server.
   */
  public ClientDbManager(String hostname, int port) throws IOException {
    Validator.checkNotEmptyString(hostname);
    Validator.checkNotNegative(port);

    this.hostname = hostname;
    this.port = port;
    this.socket = new Socket(hostname, port);
    this.ostream = new ObjectOutputStream(this.socket.getOutputStream());
    this.ostream.flush();
    this.istream = new ObjectInputStream(this.socket.getInputStream());
  }

  /** {@inheritDoc} */
  @Override
  public void addEntries() throws IOException {
    this.ostream.writeObject(Commands.INIT);
  }

  /** {@inheritDoc} */
  @Override
  public void addEntry(JapaneseEntry entry) throws EntryAlreadyExistsException, IOException {
    Validator.checkNull(entry);

    AddRemoveRequest request = new AddRemoveRequest(Commands.ADD, entry);
    this.ostream.writeObject(request);
  }

  /** {@inheritDoc} */
  @Override
  public void removeEntry(JapaneseEntry entry) throws EntryDoesNotExistException, IOException {
    Validator.checkNull(entry);

    AddRemoveRequest request = new AddRemoveRequest(Commands.REMOVE, entry);
    this.ostream.writeObject(request);
  }

  /** {@inheritDoc} */
  @Override
  public void updateEntry(JapaneseEntry newEntry, JapaneseEntry oldEntry)
      throws EntryDoesNotExistException, StaleEntryException, IOException {
    Validator.checkNull(newEntry);
    Validator.checkNull(oldEntry);

    UpdateRequest request = new UpdateRequest(newEntry, oldEntry);
    this.ostream.writeObject(request);
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public List<JapaneseEntry> find(String regexPattern) throws Exception {
    Validator.checkNotEmptyString(regexPattern);

    FindRequest request = new FindRequest(regexPattern);
    this.ostream.writeObject(request);
    return (List<JapaneseEntry>) this.istream.readObject();
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public List<JapaneseEntry> getEntries() throws Exception {
    this.ostream.writeObject(Commands.GET);
    return (List<JapaneseEntry>) this.istream.readObject();
  }

  /** {@inheritDoc} */
  @Override
  public boolean save() throws Exception {
    this.ostream.writeObject(Commands.SAVE);
    return (Boolean) this.istream.readObject();
  }

  /**
   * Tells the server that this client is going to quit.
   * 
   * @throws IOException If there are problems trying to send the QUIT command to the server.
   */
  public void quit() throws IOException {
    this.ostream.writeObject(Commands.QUIT);
  }

  /**
   * Closes the connection.
   * 
   * @return True if the socket has been closed, false otherwise.
   * @throws IOException If there are problems trying to close the socket.
   */
  public boolean close() throws IOException {
    this.socket.close();
    return this.socket.isClosed();
  }

  /**
   * Sets the listener for receiving server status events.
   * @param listener The listener to set.
   */
  public void setServerStatusListener(ServerStatusListener listener) {
    Validator.checkNull(listener);
    this.listener = listener;
  }

  /** Starts the task that will check the status of the server periodically. */
  public void startCheckServerStatusTask() {
    this.threadPool.scheduleAtFixedRate(new CheckServerStatusTask(), 0L, 10L, TimeUnit.SECONDS);
  }

  /**
   * A task that will check the status of the server by trying to open a connection to it.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class CheckServerStatusTask implements Runnable {

    /** {@inheritDoc} */
    @Override
    public void run() {
      try {
        new Socket(hostname, port);
        listener.serverStatusChanged(ServerStatus.ONLINE);
      }
      catch (IOException e) {
        LOGGER.log(Level.SEVERE, e.getMessage());
        listener.serverStatusChanged(ServerStatus.OFFLINE);
      }
    }

  }

}
