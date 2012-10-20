package org.katas;

import java.util.ArrayList;
import java.util.List;

import org.katas.common.Kata;
import org.katas.common.KataEnums;
import org.katas.common.KataUtils;

/**
 * This program sorts one or more stacks of pancakes that were read in from a file from smallest to
 * biggest; the biggest pancake will be at the bottom of the stack and the smallest one will be at
 * the top.
 * 
 * @author BJ Peter DeLaCruz
 */
public class PancakeStacks extends Kata {

  private final List<List<Integer>> pancakeStacks;
  private final List<Integer> positions;
  private int headIndex;

  /**
   * Creates a new PancakeStacks object and initializes the lists used to store the stacks and
   * positions under which to flip a subset of pancakes.
   */
  public PancakeStacks() {
    this.pancakeStacks = new ArrayList<List<Integer>>();
    this.positions = new ArrayList<Integer>();
  }

  /**
   * Reads in one or more stacks of pancakes and then sorts each stack.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void processLines() {
    for (String s : this.getLines()) {
      List<Integer> list = (List<Integer>) KataUtils.createList(s, " ", KataEnums.INTEGER);
      this.pancakeStacks.add(list);
    }

    flipPancakes();
  }

  /**
   * Sorts each of the stacks of pancakes found in the input file, and then displays each original
   * stack of pancakes and also the number of flips it took to sort the pancakes from smallest to
   * biggest.
   */
  private void flipPancakes() {
    for (List<Integer> stack : this.pancakeStacks) {
      System.out.println("Stack:     " + KataUtils.printArrayContents(stack));
      int size = stack.size();
      while (!this.isSorted(stack)) {
        flip(stack.subList(0, size--));
      }
      this.positions.add(0);
      System.out.println("Positions: " + KataUtils.printArrayContents(this.positions) + "\n");
      this.positions.clear();
      this.headIndex = 1;
    }
  }

  /**
   * Given the entire stack or a subset of pancakes, finds the biggest pancake, and then flips that
   * pancake along with the other ones on top of it.
   * 
   * @param stack The stack of pancakes to flip.
   */
  private void flip(List<Integer> stack) {
    if (stack == null || stack.isEmpty()) {
      throw new IllegalArgumentException("Stack is null or empty.");
    }

    int maxPos = 0;
    for (int index = 0; index < stack.size() - 1; index++) {
      if (stack.get(maxPos) < stack.get(index + 1)) {
        maxPos = index + 1;
      }
    }

    // The pancake with the biggest diameter is at the beginning of the list, so just flip the
    // pancakes from the bottom so that it will be at the bottom. Otherwise, flip the pancakes at
    // the position found, and then flip them again from the bottom so that the biggest pancake will
    // be at the bottom.
    if (maxPos == 0) {
      flip(stack, stack.size() - 1);
      this.positions.add(this.headIndex);
    }
    else if (maxPos != stack.size() - 1) {
      flip(stack, maxPos);
      this.positions.add(stack.size() - maxPos);
      flip(stack, stack.size() - 1);
      this.positions.add(this.headIndex);
    }
    this.headIndex++;
  }

  /**
   * Flips a subset of pancakes from under the pancake at position <code>length</code>.
   * 
   * @param stack The stack of pancakes.
   * @param length The number of pancakes to flip.
   */
  private void flip(List<Integer> stack, int length) {
    if (stack == null || stack.isEmpty()) {
      throw new IllegalArgumentException("Stack is null or empty.");
    }
    if (length < 0) {
      throw new IllegalArgumentException("Length is less than 0.");
    }
    else if (length > stack.size()) {
      throw new IllegalArgumentException("Length cannot be greater than " + stack.size() + ".");
    }
    double numSwaps = Math.ceil(length / 2.0);
    for (int head = 0, tail = length; head < numSwaps; head++, tail--) {
      int temp = stack.get(tail);
      stack.set(tail, stack.get(head));
      stack.set(head, temp);
    }
  }

  /**
   * Returns <code>true</code> if a stack of pancakes is sorted from smallest to largest, beginning
   * from the top of the stack; <code>false</code> otherwise.
   * 
   * @param stack The stack of pancakes.
   * @return True if the pancakes are sorted from smallest to biggest, false otherwise.
   */
  private boolean isSorted(List<Integer> stack) {
    if (stack == null || stack.isEmpty()) {
      throw new IllegalArgumentException("Stack is null or empty.");
    }
    for (int index = 0; index < stack.size() - 1; index++) {
      if (stack.get(index) > stack.get(index + 1)) {
        return false;
      }
    }
    return true;
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing one or more stacks of pancakes to sort.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    PancakeStacks pancakeStacks = new PancakeStacks();
    pancakeStacks.setLines(KataUtils.readLines(args[0]));

    if (pancakeStacks.getLines() != null) {
      pancakeStacks.processLines();
    }
  }
}
