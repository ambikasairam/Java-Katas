package org.katas.minesweeper;

import com.bpd.utils.math.Point;

/**
 * A class representing a square in the Minesweeper game.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 * 
 * @param <X> Row number.
 * @param <Y> Column number.
 */
final class MinesweeperSquare<X extends Number, Y extends Number> extends Point<X, Y> {

  private final boolean isMineLocatedHere;
  private int numSurroundingMines;

  /**
   * Constructs a new MinesweeperSquare.
   * 
   * @param x The row number.
   * @param y The column number.
   * @param isMineLocatedHere True if a mine is located in this square, false otherwise.
   */
  public MinesweeperSquare(X x, Y y, boolean isMineLocatedHere) {
    super(x, y);
    this.isMineLocatedHere = isMineLocatedHere;
    if (this.isMineLocatedHere) {
      this.numSurroundingMines = Integer.MIN_VALUE;
    }
    else {
      this.numSurroundingMines = 0;
    }
  }

  /** @return True if a mine is located in this square, false otherwise. */
  public boolean isMineLocatedHere() {
    return this.isMineLocatedHere;
  }

  /**
   * @return The number of surrounding mines if this square does not contain a mine, or MIN_VALUE if
   * it does.
   */
  public int getNumSurroundingMines() {
    return this.numSurroundingMines;
  }

  /**
   * Increments the number of squares that the current square is next to that contain mines.
   */
  public void incrementNumSurroundingMines() {
    this.numSurroundingMines++;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    String msg = "MinesweeperSquare[Row: " + this.getX() + " - Column: " + this.getY() + " ";
    msg += this.isMineLocatedHere;
    msg += ((this.isMineLocatedHere) ? "]" : " - " + this.numSurroundingMines + "]");
    return msg;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    @SuppressWarnings("unchecked")
    MinesweeperSquare<Number, Number> square = (MinesweeperSquare<Number, Number>) o;
    if (this.getX().equals(square.getX()) && this.getY().equals(square.getY())
        && this.isMineLocatedHere == square.isMineLocatedHere
        && this.numSurroundingMines == square.numSurroundingMines) {
      return true;
    }
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int hashCode = this.getX().hashCode() + this.getY().hashCode();
    hashCode += ((this.isMineLocatedHere) ? 1 : 0) + this.numSurroundingMines;
    return hashCode;
  }

}
