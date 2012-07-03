package org.certification.common.database;

import java.util.List;
import org.certification.common.data.Excursion;
import org.certification.common.exceptions.RecordNotFoundException;

/**
 * The interface for the excursion database.
 * 
 * @author BJ Peter DeLaCruz
 */
public interface ExcursionDatabase {

  /**
   * Creates an excursion.
   * 
   * @param excursion The new excursion.
   * @return True if the given excursion was successfully added to the database, false otherwise.
   */
  public boolean createExcursion(Excursion excursion);

  /**
   * Updates an existing excursion.
   * 
   * @param recordNumber The record number of the excursion to update.
   * @param excursion The new excursion.
   * @return True if the excursion was successfully updated in the database, false otherwise.
   * @throws RecordNotFoundException If the given excursion cannot be found.
   */
  public boolean updateExcursion(long recordNumber, Excursion excursion)
      throws RecordNotFoundException;

  /**
   * Deletes an existing excursion.
   * 
   * @param recordNumber The record number of the excursion to delete.
   * @return True if the excursion was successfully deleted from the database, false otherwise.
   * @throws RecordNotFoundException If the given excursion cannot be found.
   */
  public boolean deleteExcursion(long recordNumber) throws RecordNotFoundException;

  /**
   * Reads an excursion from the database and then returns it.
   * 
   * @param recordNumber The record number of the excursion to read from the database.
   * @return The excursion at the given record number.
   * @throws RecordNotFoundException If the given excursion cannot be found.
   */
  public Excursion getExcursion(long recordNumber) throws RecordNotFoundException;

  /**
   * Returns a list of all excursions in the database.
   * 
   * @return The list of excursions.
   */
  public List<Excursion> getAllExcursions();

}
