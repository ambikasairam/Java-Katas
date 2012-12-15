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
package org.katas.transaction;

/**
 * Contains account information needed to process a transaction in the TransactionProcessing class.
 * 
 * @author BJ Peter DeLaCruz
 */
final class Account {

  private final int accountNo;
  private final String accountDescr;

  /**
   * Creates a new Account object that contains an account number and description of the account.
   * 
   * @param accountNo Account number.
   * @param accountDescr Description of the account.
   */
  public Account(int accountNo, String accountDescr) {
    if (accountNo < 0) {
      throw new IllegalArgumentException("Invalid account number: " + accountNo);
    }
    if (accountDescr == null || "".equals(accountDescr)) {
      throw new IllegalArgumentException("Account description is null or empty string.");
    }
    this.accountNo = accountNo;
    this.accountDescr = accountDescr;
  }

  /**
   * Returns the account number.
   * 
   * @return The account number.
   */
  public int getAccountNo() {
    return this.accountNo;
  }

  /**
   * Returns the account description.
   * 
   * @return The account description.
   */
  public String getAccountDescr() {
    return this.accountDescr;
  }

  /**
   * Returns the account number and the description of this account in a readable format.
   * 
   * @return The account number and the description of this account in a readable format.
   */
  @Override
  public String toString() {
    return "AccountInfo=[accountNo=" + this.accountNo + ",accountDescr=" + this.accountDescr + "]";
  }
}
