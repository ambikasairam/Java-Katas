package org.katas;

/**
 * This class is used by the Arbitrage class to store a currency exchange sequence and the profit
 * one makes by using it.
 * 
 * @author BJ Peter DeLaCruz
 */
public class CurrencyExchange {

  private final String exchangeSequence;
  private final double profit;

  /**
   * Creates a new CurrencyExchange object; holds the exchange sequence and the profit made by it.
   * 
   * @param exchangeSequence The exchange sequence.
   * @param profit The profit made by the exchange sequence.
   */
  public CurrencyExchange(String exchangeSequence, double profit) {
    this.exchangeSequence = exchangeSequence;
    this.profit = profit;
  }

  /**
   * Gets the exchange sequence.
   * 
   * @return The exchange sequence.
   */
  public String getExchangeSequence() {
    return this.exchangeSequence;
  }

  /**
   * Gets the profit made by the exchange sequence.
   * 
   * @return The profit made by the exchange sequence.
   */
  public double getProfit() {
    return this.profit;
  }

  /**
   * Prints the exchange sequence and the profit made by it.
   * 
   * @return A string displaying the exchange sequence and profit.
   */
  public String toString() {
    return this.exchangeSequence + ": " + this.profit;
  }
}
