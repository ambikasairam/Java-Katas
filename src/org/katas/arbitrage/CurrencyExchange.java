package org.katas.arbitrage;

import java.util.Objects;

/**
 * This class is used by the Arbitrage class to store a currency exchange sequence and the profit
 * one makes by using it.
 * 
 * @author BJ Peter DeLaCruz
 */
public class CurrencyExchange implements Comparable<CurrencyExchange> {

  private final String exchangeSequence;
  private final Double profit;

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
    return exchangeSequence;
  }

  /**
   * Gets the profit made by the exchange sequence.
   * 
   * @return The profit made by the exchange sequence.
   */
  public double getProfit() {
    return profit;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return exchangeSequence + ": " + profit;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof CurrencyExchange)) {
      return false;
    }
    CurrencyExchange exchange = (CurrencyExchange) object;
    return exchangeSequence.equals(exchange.exchangeSequence) && profit.equals(exchange.profit);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(exchangeSequence, profit);
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(CurrencyExchange exchange) {
    return profit.compareTo(exchange.profit);
  }

}
