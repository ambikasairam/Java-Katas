package org.jlpt.server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.DbManagerImpl;
import org.jlpt.common.utils.Validator;

/**
 * A server-side proxy that processes requests from the client.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ServerDbManager {

  private static final Logger LOGGER = Logger.getGlobal();
  private static final int NUM_THREADS = 25;

  private final ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
  private final DbManager databaseManager;

  private ServerSocket serverSocket;
  private final int port;

  /**
   * Creates a new ServerDbManager.
   * 
   * @param databaseManager The database manager.
   * @param port The port number used to connect to the server.
   * @throws IOException If there are problems trying to create the server socket.
   */
  public ServerDbManager(DbManager databaseManager, int port) throws IOException {
    Validator.checkNull(databaseManager);
    Validator.checkNotNegative(port);

    this.databaseManager = databaseManager;
    this.port = port;
    this.serverSocket = new ServerSocket(port);
  }

  /** @return The database manager. */
  public DbManager getDatabaseManager() {
    return this.databaseManager;
  }

  /**
   * Creates a new server socket.
   * 
   * @throws IOException If there are problems trying to create the server socket.
   */
  public void createSocket() throws IOException {
    this.serverSocket = new ServerSocket(this.port);
  }

  /**
   * Creates a new server socket and binds it to the given port.
   * 
   * @param port The port number to use for the server.
   * @throws IOException If there are problems trying to create the server socket.
   */
  public void createSocket(int port) throws IOException {
    Validator.checkNotNegative(port);

    this.serverSocket = new ServerSocket(port);
  }

  /**
   * Closes the socket.
   * 
   * @throws IOException If there are problems trying to close the server socket.
   */
  public void closeSocket() throws IOException {
    this.serverSocket.close();
  }

  /**
   * Starts a thread that will listen for connections from the client. When a request comes in, the
   * thread will hand it over to another thread that will process the request.
   */
  private void start() {
    this.threadPool.execute(new Runnable() {

      @Override
      public void run() {
        while (!threadPool.isShutdown()) {
          try {
            Socket socket = serverSocket.accept();
            threadPool.execute(new Task(databaseManager, socket));
          }
          catch (SocketException e) {
            if (e.getMessage().contains("socket closed")) {
              LOGGER.log(Level.INFO, e.getMessage());
            }
            else {
              LOGGER.log(Level.SEVERE, e.getMessage());
            }
            break;
          }
          catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            break;
          }
        }
      }

    });
  }

  /**
   * Shuts down the thread pool.
   * 
   * @throws IOException If there are problems shutting down the server socket.
   */
  public void shutdown() throws IOException {
    this.serverSocket.close();
    this.threadPool.shutdown();
  }

  /**
   * Test program.
   * @param args None.
   * @throws Exception If problems are encountered.
   */
  public static void main(String... args) throws Exception {
    LOGGER.setLevel(Level.INFO);
    String location = "C:\\Users\\BJ Peter DeLaCruz\\Desktop\\JLPT Study\\japanese_words.db";
    ServerDbManager manager = new ServerDbManager(new DbManagerImpl(location), 7777);
    manager.start();
    Thread.sleep(15000); // Run server for 15 seconds.
    manager.shutdown();
  }

}
