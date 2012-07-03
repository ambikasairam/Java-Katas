package org.certification.common.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.utils.Validator;

/**
 * An excursion for a tour bus company.
 * 
 * @author BJ Peter DeLaCruz
 */
public class Excursion implements Serializable {

  private static final long serialVersionUID = 6419719493263183433L;

  private final int busNumber;
  private final String companyName;
  private final Date departure;
  private final Date arrival;
  private final String origin;
  private final String destination;
  private final double price;
  private final byte capacity;

  /**
   * Creates a new Excursion.
   * 
   * @param busNumber The bus number.
   * @param companyName The name of the company.
   * @param departure The departure date and time.
   * @param arrival The arrival date and time.
   * @param origin The place of origin.
   * @param destination The destination.
   * @param price The cost for this excursion.
   * @param capacity The number of people signed up for this excursion.
   */
  public Excursion(int busNumber, String companyName, Date departure, Date arrival, String origin,
      String destination, double price, byte capacity) {
    Validator.checkIfNonNegative(busNumber);
    Validator.checkIfNotEmptyString(companyName);
    Validator.checkNull(destination);
    Validator.checkNull(arrival);
    Validator.checkIfNotEmptyString(origin);
    Validator.checkIfNotEmptyString(destination);
    Validator.checkIfNonNegative(price);
    Validator.checkIfNonNegative(capacity);

    this.busNumber = busNumber;
    this.companyName = companyName;
    this.departure = new Date(departure.getTime());
    this.arrival = new Date(arrival.getTime());
    this.origin = origin;
    this.destination = destination;
    this.price = price;
    this.capacity = capacity;
  }

  /** @return The bus number. */
  public int getBusNumber() {
    return this.busNumber;
  }

  /** @return The name of the company. */
  public String getCompanyName() {
    return this.companyName;
  }

  /** @return The departure date and time. */
  public Date getDeparture() {
    return new Date(this.departure.getTime());
  }

  /** @return The arrival date and time. */
  public Date getArrival() {
    return new Date(this.arrival.getTime());
  }

  /** @return The origin. */
  public String getOrigin() {
    return this.origin;
  }

  /** @return The destination. */
  public String getDestination() {
    return this.destination;
  }

  /** @return The cost for this excursion. */
  public double getPrice() {
    return this.price;
  }

  /** @return The number of people signed up for this excursion. */
  public byte getCapacity() {
    return this.capacity;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Excursion)) {
      return false;
    }
    Excursion excursion = (Excursion) object;
    return this.busNumber == excursion.busNumber && this.companyName.equals(excursion.companyName)
        && this.departure.equals(excursion.departure) && this.arrival.equals(excursion.arrival)
        && this.origin.equals(excursion.origin) && this.destination.equals(excursion.destination)
        && Double.compare(this.price, excursion.price) == 0 && this.capacity == excursion.capacity;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(this.busNumber, this.companyName, this.departure, this.arrival,
        this.origin, this.destination, this.price, this.capacity);
  }

}
