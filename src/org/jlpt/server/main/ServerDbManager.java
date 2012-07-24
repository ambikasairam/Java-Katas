package org.jlpt.server.main;

import org.jlpt.common.db.DbManager;
import org.jlpt.common.utils.Validator;

/**
 * A server-side proxy that processes requests from the client.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ServerDbManager {

  private final DbManager databaseManager;

  /**
   * Creates a new ServerDbManager.
   * 
   * @param databaseManager The database manager.
   */
  public ServerDbManager(DbManager databaseManager) {
    Validator.checkNull(databaseManager);

    this.databaseManager = databaseManager;
  }

  /** @return The database manager. */
  public DbManager getDatabaseManager() {
    return this.databaseManager;
  }

}
