package org.jlpt.client.main;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.EntryAlreadyExistsException;
import org.jlpt.common.db.EntryDoesNotExistException;
import org.jlpt.common.db.InvalidRegExPatternException;
import org.jlpt.common.db.StaleEntryException;
import org.jlpt.common.utils.Validator;

/**
 * A client-side proxy that sends requests to a server using a socket.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ClientDbManager implements DbManager {

  private final Socket socket;

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

    this.socket = new Socket(hostname, port);
  }

  /** {@inheritDoc} */
  @Override
  public void addEntry(JapaneseEntry entry) throws EntryAlreadyExistsException {
    // TODO: Add code here.
  }

  /** {@inheritDoc} */
  @Override
  public void removeEntry(JapaneseEntry entry) throws EntryDoesNotExistException {
    // TODO: Add code here.
  }

  /** {@inheritDoc} */
  @Override
  public void updateEntry(JapaneseEntry newEntry, JapaneseEntry oldEntry)
      throws EntryDoesNotExistException, StaleEntryException {
    // TODO: Add code here.
  }

  /** {@inheritDoc} */
  @Override
  public List<JapaneseEntry> find(String regexPattern) throws InvalidRegExPatternException {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public List<JapaneseEntry> getEntries() {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void save() throws IOException {
    // TODO: Add code here.
  }

  /**
   * Closes the connection.
   * 
   * @throws IOException If there are problems trying to close the socket.
   */
  public void close() throws IOException {
    this.socket.close();
  }

}
