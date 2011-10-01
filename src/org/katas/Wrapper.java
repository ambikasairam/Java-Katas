package org.katas;

import java.util.Random;

/**
 * Java Kata: String Wrapper.
 * 
 * @author BJ Peter DeLaCruz
 * @version 1.0
 */
public class Wrapper {

  /**
   * Given a string, this method will return the same string but with line breaks inserted at the
   * right places so that no line is longer than the column number.
   * 
   * @param str String to modify.
   * @param numColumns Maximum number of characters on a line.
   * @return String whose lines do not have more than the maximum number of characters specified.
   */
  public char[] getWrappedString(String str, int numColumns) {
    char[] string = str.toCharArray();
    for (int counter = 0, position = 0; position < str.length(); counter++, position++) {
      if (counter == numColumns) {
        while (string[position] != ' ') {
          position--;
        }
        string[position] = '\n';
        counter = 0;
      }
    }
    return string;
  }

  /**
   * Task 1.
   * 
   * @param str String to manipulate.
   * @return Manipulated string.
   */
  public String task1(String str) {
    StringBuffer buffer = new StringBuffer();
    char[] charArray = str.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      if (charArray[i] > 96 && charArray[i] < 123) {
        charArray[i] -= 32;
      }
      else if (charArray[i] > 64 && charArray[i] < 91) {
        charArray[i] += 32;
      }
    }
    buffer.append(charArray);
    return buffer.reverse().toString();
  }

  /**
   * Task 2: Returns a 2D array of ints.
   * 
   * @return A 2D array of ints.
   */
  public int[][] generate2dArray() {
    int[][] twoDArray = new int[7][7];
    Random random = new Random();
    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 7; j++) {
        twoDArray[i][i] = random.nextInt(11);
      }
    }
    return twoDArray;
  }

  /**
   * Task 2: Calculates the minimum, maximum, and average of all numbers in 2D array.
   * 
   * @param twoDArray A 2D array of ints.
   */
  public void calculateMinMaxAve(int[][] twoDArray) {
    int min = twoDArray[0][0];
    int max = min;
    int sum = 0;
    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 7; j++) {
        if (min < twoDArray[i][i]) {
          min = twoDArray[i][i];
        }
        if (max > twoDArray[i][i]) {
          max = twoDArray[i][i];
        }
        sum += twoDArray[i][i];
      }
    }
    System.out.println("Min: " + min + "\nMax: " + max + "\nAve: " + (sum / 49));
  }

  /**
   * Tests the getWrappedString method above.
   * 
   * @param args None.
   */
  public static void main(String[] args) {
    StringBuffer buffer = new StringBuffer();
    String testString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ";
    buffer.append(testString);
    testString = "Aenean in nulla eros. Proin eget eros vitae turpis sodales tincidunt. ";
    buffer.append(testString);
    testString = "Aliquam at orci ut diam placerat laoreet sit amet et nunc. ";
    buffer.append(testString);
    testString = "Suspendisse nisl neque, fermentum et rhoncus at, posuere eget massa.";
    buffer.append(testString);

    Wrapper wrapper = new Wrapper();
    char[] result = wrapper.getWrappedString(buffer.toString(), 20);
    System.out.println(result);
  }
}
