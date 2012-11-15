package org.katas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.katas.common.Kata;
import org.katas.common.KataUtils;

/**
 * This class creates a card game in which the computer takes a card from the top of a pile, flips
 * it over, and then puts it under another pile. See {@link #playGame()} for rules on how this game
 * is played.
 * 
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/170_Clock_Patience.pdf">Clock
 * Patience</a>
 * 
 * @author BJ Peter DeLaCruz
 */
public class ClockPatience extends Kata {

  private List<PlayingCard> playingCards;
  private final Map<Integer, List<PlayingCard>> clock;
  private boolean enableShuffling;
  private static final int NUM_DECKS = 13;
  private static final int NUM_TIMES_SHUFFLE = 3;

  /**
   * Creates a new ClockPatience object. Sets up the piles and turns off shuffling, which can be
   * re-enabled via the <code style="font-weight: bold">-s</code> command-line argument.
   */
  public ClockPatience() {
    this.clock = new HashMap<Integer, List<PlayingCard>>();
    for (int index = 1; index <= NUM_DECKS; index++) {
      this.clock.put(index, new ArrayList<PlayingCard>());
    }
    this.enableShuffling = false;
  }

  /**
   * Turns on shuffling; shuffles the cards before dealing them. See {@link #dealCards()}.
   */
  public void enableShuffling() {
    this.enableShuffling = true;
  }

  /**
   * Creates a set of 52 playing cards, deals them, and then plays the game. See {@link #playGame()}
   * .
   */
  @Override
  public void processLines() {
    this.playingCards = KataUtils.createPlayingCards(this.getLines());

    dealCards();

    playGame();

    if (areAllCardsFlippedOver()) {
      System.out.println("* You win! *");
    }
  }

  /**
   * Deals the cards by placing each card in a pile until there are thirteen piles and four cards
   * per pile. The user can choose to have the computer shuffle the cards first before dealing them
   * if he or she chooses via the <code style="font-weight: bold">-s</code> command-line argument.
   */
  private void dealCards() {
    if (this.enableShuffling) {
      for (int index = 1; index <= NUM_TIMES_SHUFFLE; index++) {
        Collections.shuffle(this.playingCards);
      }
    }
    else {
      Collections.reverse(this.playingCards);
    }

    int index = 1;
    for (PlayingCard card : this.playingCards) {
      this.clock.get(index).add(card);
      if (++index > NUM_DECKS) {
        index = 1;
      }
    }

    for (index = 1; index <= NUM_DECKS; index++) {
      Collections.reverse(this.clock.get(index));
    }
  }

  /**
   * Takes the top card from a pile, flips it over (i.e. the card is now facing up), and places it
   * at the bottom of the pile corresponding to the value of the card. Then the top card from the
   * pile under which the previous card was placed is taken, flipped over, and placed at the bottom
   * of another pile corresponding to the value of that card. This process continues until all cards
   * in the pile under which the card that was just played are flipped over.
   */
  private void playGame() {
    int count = 1;
    PlayingCard currentCard = this.clock.get(NUM_DECKS).remove(0);
    currentCard.flipCardOver();
    int position = getClockPosition(currentCard);
    this.clock.get(position).add(currentCard);

    while (this.clock.get(position).get(0).isCardFacingDown()) {
      currentCard = this.clock.get(position).remove(0);
      currentCard.flipCardOver();
      position = getClockPosition(currentCard);
      this.clock.get(position).add(currentCard);
      count++;
    }

    printClock();

    System.out.println("\n" + count + "," + currentCard.getCard());
  }

  /**
   * Prints all of the playing cards in each of the thirteen piles.
   */
  private void printClock() {
    for (int index = 1; index <= NUM_DECKS; index++) {
      List<PlayingCard> cards = this.clock.get(index);
      System.out.print(index + ":\t");
      for (int pos = 0; pos < cards.size() - 1; pos++) {
        System.out.print(cards.get(pos) + " ");
      }
      System.out.println(cards.get(cards.size() - 1));
    }
  }

  /**
   * Returns a number from 1 to 13 given a playing card (Ace = 1, T = 10, Jack = 11, Queen = 12,
   * King = 13).
   * 
   * @param card The playing card.
   * @return A number from 1 to 13.
   */
  private int getClockPosition(PlayingCard card) {
    char value = card.getCardValue();
    if (value >= '1' && value <= '9') {
      return Integer.parseInt(value + "");
    }
    switch (value) {
    case 'A':
      return 1;
    case 'T':
      return 10;
    case 'J':
      return 11;
    case 'Q':
      return 12;
    case 'K':
      return 13;
    default:
      throw new IllegalArgumentException("Invalid character: " + value);
    }
  }

  /**
   * Returns true if all cards are flipped over (i.e. all cards on the top of each pile are facing
   * up), false otherwise.
   * 
   * @return True if all cards are flipped over, false otherwise.
   */
  private boolean areAllCardsFlippedOver() {
    for (int index = 1; index <= NUM_DECKS; index++) {
      if (this.clock.get(index).get(0).isCardFacingDown()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Prints the Help menu for this program.
   */
  private static void printUsage() {
    System.err.println("Usage: [filename] {-s}\n");
    System.err.println("       filename : name of file");
    System.err.println("       -s       : shuffle cards (optional)");
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing a list of playing cards (required) and an argument to
   * tell the computer to shuffle the cards first before dealing them (optional).
   */
  public static void main(String... args) {
    if (args.length > 2) {
      printUsage();
      return;
    }

    ClockPatience clockPatience = new ClockPatience();

    if (args.length == 2 && ("-s".equals(args[1]) || "-S".equals(args[1]))) {
      clockPatience.enableShuffling();
    }
    else if (args.length == 2) {
      printUsage();
      return;
    }

    clockPatience.setLines(KataUtils.readLines(args[0]));

    if (clockPatience.getLines() != null) {
      clockPatience.processLines();
    }
  }
}
