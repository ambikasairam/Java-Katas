package org.katas.currency;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A <code>Comparator</code> used to sort a list of <code>CurrencyExchange</code> objects.
 * 
 * @author BJ Peter DeLaCruz
 */
public class CurrencyExchangeComparator implements Comparator<CurrencyExchange>, Serializable {

  /**
   * Used for object serialization.
   */
  private static final long serialVersionUID = 1L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(CurrencyExchange arg0, CurrencyExchange arg1) {
    Double profit1 = arg0.getProfit();
    Double profit2 = arg1.getProfit();
    if (profit1.compareTo(profit2) == 0) {
      return arg0.getExchangeSequence().compareTo(arg1.getExchangeSequence());
    }
    return profit1.compareTo(profit2);
  }

}
