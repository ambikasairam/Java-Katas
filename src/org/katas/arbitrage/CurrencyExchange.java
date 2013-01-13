/*******************************************************************************
 * Copyright (C) 2012 BJ Peter DeLaCruz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
