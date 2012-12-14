package org.katas.transaction;

import org.katas.common.KataUtils;

/**
 * Contains transaction information needed to balance a list of transactions in the
 * TransactionProcessing class.
 * 
 * @author BJ Peter DeLaCruz
 */
final class Transaction {

  private final int accountNo;
  private final int transactionNo;
  private final int balance;

  /**
   * Creates a new Transaction object that contains the account number associated with this
   * transaction, a transaction number, and the balance associated with this transaction.
   * 
   * @param accountNo Account number.
   * @param transactionNo Transaction number.
   * @param balance Positive or negative balance.
   */
  public Transaction(int accountNo, int transactionNo, int balance) {
    if (accountNo < 0) {
      throw new IllegalArgumentException("Invalid account number: " + accountNo);
    }
    if (transactionNo < 0) {
      throw new IllegalArgumentException("Invalid transaction number: " + transactionNo);
    }
    this.accountNo = accountNo;
    this.transactionNo = transactionNo;
    this.balance = balance;
  }

  /**
   * Returns the account number associated with this transaction.
   * 
   * @return The account number associated with this transaction.
   */
  public int getAccountNo() {
    return this.accountNo;
  }

  /**
   * Returns the transaction number.
   * 
   * @return The transaction number.
   */
  public int getTransactionNo() {
    return this.transactionNo;
  }

  /**
   * Returns the balance associated with this transaction.
   * 
   * @return The balance associated with this transaction.
   */
  public int getBalance() {
    return this.balance;
  }

  /**
   * Returns the account number associated with this transaction, the transaction number, and the
   * balance for this transaction in a readable format.
   * 
   * @return Information about this transaction.
   */
  @Override
  public String toString() {
    return "Transaction=[accountNo=" + this.accountNo + ",transactionNo=" + this.transactionNo
        + ",balance=" + KataUtils.getBalanceAsString(this.balance) + "]";
  }
}
