package org.katas;

/**
 * A class that represents an employee at a company.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class Employee {

  private final String firstName;
  private final String lastName;
  private final String middleInitial;
  private final int income;

  /**
   * Creates a new Employee.
   * 
   * @param firstName The first name.
   * @param lastName The last name.
   * @param middleInitial The middle initial.
   * @param income The yearly income.
   */
  public Employee(String firstName, String lastName, String middleInitial, int income) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.middleInitial = middleInitial;
    this.income = income;
  }

  /** @return The first name. */
  public String getFirstName() {
    return this.firstName;
  }

  /** @return The last name. */
  public String getLastName() {
    return this.lastName;
  }

  /** @return The middle initial. */
  public String getMiddleInitial() {
    return this.middleInitial;
  }

  /** @return The annual income. */
  public int getIncome() {
    return this.income;
  }

}
