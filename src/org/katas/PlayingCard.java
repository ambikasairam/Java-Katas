package org.katas;

import java.util.Locale;

/**
 * This class is used to create a new playing card, which can be used in katas that involve playing
 * cards.
 * 
 * @author BJ Peter DeLaCruz
 */
public class PlayingCard {

  private final String value;
  private boolean isFacingDown;

  /**
   * Creates a new playing card, which is either facing down (true) or up (false) initially.
   * 
   * @param value The value for the new playing card.
   * @param isFacingDown True if the playing card should be facing down initially, false otherwise.
   */
  public PlayingCard(String value, boolean isFacingDown) {
    if (value == null || "".equals(value)) {
      throw new IllegalArgumentException("Card value is null or empty string.");
    }
    if (value.length() != 2) {
      throw new IllegalArgumentException("Card value must be of length 2.");
    }
    if (!isCardValueValid(value)) {
      throw new IllegalArgumentException("Invalid card value: " + value);
    }
    this.value = value.toUpperCase(Locale.US);
    this.isFacingDown = isFacingDown;
  }

  /**
   * Determines whether a playing card's value and suit are valid.
   * 
   * @param value Includes the playing card's value (must be A = Ace; T = 10; 1, 2, 3, 4, 5, 6, 7,
   * 8, or 9) and suit (must be C = Club, D = Diamond, H = Heart, or S = Spade).
   * @return True if a playing card's value and suit are valid, false otherwise.
   */
  private boolean isCardValueValid(String value) {
    char[] values = value.toUpperCase(Locale.US).toCharArray();
    if (values[0] < '1'
        || (values[0] > '9' && values[0] != 'A' && values[0] != 'T' && values[0] != 'J'
            && values[0] != 'Q' && values[0] != 'K')) {
      return false;
    }
    if (values[1] != 'C' && values[1] != 'D' && values[1] != 'H' && values[1] != 'S') {
      return false;
    }
    return true;
  }

  /**
   * Gets a playing card's value and suit.
   * 
   * @return The playing card's value and suit.
   */
  public String getCard() {
    return this.value;
  }

  /**
   * Gets a playing card's value (1, 2, 3, 4, 5, 6, 7, 8, 9, T [10], or A [Ace]).
   * 
   * @return A playing card's value.
   */
  public char getCardValue() {
    return this.value.toCharArray()[0];
  }

  /**
   * Gets a playing card's suit (C = Club, D = Diamond, H = Heart, S = Spade).
   * 
   * @return One of four suits.
   */
  public char getCardSuit() {
    return this.value.toCharArray()[1];
  }

  /**
   * Flips a playing card over; true if it is facing down, false otherwise.
   */
  public void flipCardOver() {
    this.isFacingDown = !this.isFacingDown;
  }

  /**
   * Returns true if this playing card is facing down, false otherwise.
   * 
   * @return True if this playing card is facing down, false otherwise.
   */
  public boolean isCardFacingDown() {
    return this.isFacingDown;
  }

  /**
   * Prints the value of a playing card and whether it is facing down or up.
   * 
   * @return The value of a playing card and whether it is facing down or up.
   */
  @Override
  public String toString() {
    return "PlayingCard=[value=" + this.value + ",isFacingDown=" + this.isFacingDown + "]";
  }
}
