package org.jlpt.client.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
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

  private final Socket socket;
  private final ObjectInputStream istream;
  private final ObjectOutputStream ostream;

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
    this.istream = new ObjectInputStream(this.socket.getInputStream());
    this.ostream = new ObjectOutputStream(this.socket.getOutputStream());
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
  public void save() throws IOException {
    this.ostream.writeObject(Commands.SAVE);
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
   * @throws IOException If there are problems trying to close the socket.
   */
  public void close() throws IOException {
    this.socket.close();
  }

  /**
   * Test program.
   * @param args None.
   * @throws Exception If problems are encountered.
   */
  public static void main(String... args) throws Exception {
    ClientDbManager manager = new ClientDbManager("localhost", 7777);
    JapaneseEntry entry = new JapaneseEntry("Hello", "World", "Foobar");
    manager.addEntry(entry);
    Thread.sleep(1000);

    entry = new JapaneseEntry("Hi", "Bye", "Hello World");
    manager.addEntry(entry);
    Thread.sleep(1000);

    manager.quit();
    manager.close();
    Thread.sleep(1000);
    System.out.println("Done.");
  }

}
