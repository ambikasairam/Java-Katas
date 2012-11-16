package org.katas.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import org.katas.common.Kata;
import org.katas.common.KataUtils;

/**
 * Processes a file containing a list of transactions and finds those that are not balanced.
 * 
 * @author BJ Peter DeLaCruz
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/Transaction_Processing.pdf">Transaction
 * Processing</a>
 */
public class TransactionProcessing extends Kata {

  private static final int NUM_SPACES = 50;
  private final Map<Integer, List<Transaction>> transactions;
  private final Map<Integer, Account> accounts;

  /**
   * Creates a new TransactionProcessing object and initializes the hash maps that will contain
   * transactions and account information.
   */
  public TransactionProcessing() {
    this.transactions = new HashMap<Integer, List<Transaction>>();
    this.accounts = new HashMap<Integer, Account>();
  }

  /**
   * Inputs all accounts and transactions, and process all transactions by summing all of the items
   * in each transaction. If the sum is not equal to zero, i.e. a transaction is not balanced,
   * information about that transaction will be printed to the screen.
   */
  @Override
  public void processLines() {
    inputAccounts();

    this.getLines().remove(0);

    inputTransactions();

    processTransactions();
  }

  /**
   * Inputs all account information read in from a file into a HashMap.
   */
  private void inputAccounts() {
    while (!this.getLines().get(0).contains("000")) {
      String info = this.getLines().remove(0);
      int accountNo = Integer.parseInt(info.substring(0, 3));
      String accountDescr = info.substring(3);
      Account account = new Account(accountNo, accountDescr);
      this.accounts.put(accountNo, account);
    }
  }

  /**
   * Inserts all transactions read in from a file into a HashMap.
   */
  private void inputTransactions() {
    while (!this.getLines().isEmpty()) {
      String line = this.getLines().remove(0);
      StringTokenizer tokenizer = new StringTokenizer(line, " ");
      if (tokenizer.countTokens() != 2) {
        System.err.println("Line in incorrect format: " + line);
        continue;
      }
      String info = tokenizer.nextToken();
      if ("000000".equals(info)) {
        break;
      }
      int accountNo = Integer.parseInt(info.substring(3, 6));
      int transactionNo = Integer.parseInt(info.substring(0, 3));
      int balance = Integer.parseInt(tokenizer.nextToken());
      Transaction transaction = new Transaction(accountNo, transactionNo, balance);
      if (this.transactions.get(Integer.parseInt(info.substring(0, 3))) == null) {
        this.transactions.put(Integer.parseInt(info.substring(0, 3)), new ArrayList<Transaction>());
      }
      this.transactions.get(Integer.parseInt(info.substring(0, 3))).add(transaction);
    }
  }

  /**
   * Sums all of the transactions and prints information about those that are out of balance.
   */
  private void processTransactions() {
    for (Entry<Integer, List<Transaction>> e : this.transactions.entrySet()) {
      int sum = 0;
      for (Transaction t : e.getValue()) {
        sum += t.getBalance();
      }
      if (sum != 0) {
        System.out.println(printResults(e, Math.abs(sum)));
      }
    }
  }

  /**
   * Prints information about the transactions that are out of balance.
   * 
   * @param entry Contains the transaction ID and a list of transactions.
   * @param sum A value greater or less than, but not equal to, zero.
   * @return A string displaying a list of transactions that are out of balance.
   */
  private String printResults(Entry<Integer, List<Transaction>> entry, int sum) {
    if (sum == 0) {
      return "Transaction " + entry.getKey() + " is not out of balance.";
    }

    StringBuffer buffer = new StringBuffer();

    String temp = "*** Transaction " + entry.getKey() + " is out of balance ***\n";
    buffer.append(temp);

    for (Transaction t : entry.getValue()) {
      temp = t.getAccountNo() + " " + this.accounts.get(t.getAccountNo()).getAccountDescr();
      buffer.append(temp);
      int index = temp.length() + KataUtils.getBalanceAsString(t.getBalance()).length();
      for (; index < NUM_SPACES; index++) {
        temp = " ";
        buffer.append(temp);
      }
      buffer.append(KataUtils.getBalanceAsString(t.getBalance()));
      temp = "\n";
      buffer.append(temp);
    }

    temp = "999 Out of Balance";
    buffer.append(temp);
    String balance = KataUtils.getBalanceAsString(sum);
    for (int index = temp.length() + balance.length(); index < NUM_SPACES; index++) {
      temp = " ";
      buffer.append(temp);
    }
    buffer.append(balance);
    temp = "\n";
    buffer.append(temp);

    return buffer.toString();
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing account information and transactions.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    TransactionProcessing transactionProcessing = new TransactionProcessing();
    transactionProcessing.setLines(KataUtils.readLines(args[0]));

    if (transactionProcessing.getLines() != null) {
      transactionProcessing.processLines();
    }
  }
}
