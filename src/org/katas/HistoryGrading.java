package org.katas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class contains methods to read in and grade students' responses as per the instructions
 * found <a href="http://uva.onlinejudge.org/external/1/111.pdf" target="_blank">here</a>.
 * 
 * @author BJ Peter DeLaCruz
 * @version 1.0
 */
public class HistoryGrading {

  /** Line containing a student's responses. */
  private String line;
  /** Total number of historical events. */
  private int length;
  /** List of historical events. */
  private final List<String> historicalEvents = new ArrayList<String>();
  /** List of one student's responses. */
  private final List<String> studentResponses = new ArrayList<String>();
  /** List of all students' responses. */
  private final List<String> allResponses = new ArrayList<String>();
  /** List of all students' grades. */
  private final List<Integer> grades = new ArrayList<Integer>();

  /**
   * Given a filename, gets the total number of historical events, the historical events in the
   * correct order, and the students' responses from the file. Grade a student's responses before
   * reading in the next student's responses. The number of responses by the student must match the
   * total number of historical events. If a problem is encountered, return immediately.
   * 
   * @param filename Name of the file containing the students' responses to grade.
   * @return True if no problems are encountered, false otherwise.
   */
  public boolean getInput(String filename) {
    if ("".equals(filename) || filename == null) {
      return false;
    }

    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));

      if (!this.getTotalHistoricalEvents(reader) || !this.getHistoricalEvents(reader)) {
        return false;
      }

      if (!this.getStudentResponses(reader)) {
        return false;
      }
    }
    catch (IOException e) {
      System.out.println("Problem reading in file: " + e);
      return false;
    }
    return true;
  }

  /**
   * Gets the line containing the total number of historical events.
   * 
   * @param reader Used to read in a line.
   * @return True on success, false otherwise.
   * @throws IOException If there are problems reading in the file.
   */
  private boolean getTotalHistoricalEvents(BufferedReader reader) throws IOException {
    if (reader == null) {
      return false;
    }

    if ((this.line = reader.readLine()) == null) {
      System.err.println("Empty file.");
      return false;
    }
    else {
      try {
        this.length = Integer.parseInt(this.line);
      }
      catch (NumberFormatException e) {
        System.err.println("First line is not a number.");
        return false;
      }
    }

    return true;
  }

  /**
   * Gets the line containing the historical events in the correct order.
   * 
   * @param reader Used to read in a line.
   * @return True on success, false otherwise.
   * @throws IOException If there are problems reading in the file.
   */
  private boolean getHistoricalEvents(BufferedReader reader) throws IOException {
    if (reader == null) {
      return false;
    }

    if ((this.line = reader.readLine()) == null) {
      System.err.println("Missing line containing the correct order of historical events.");
      return false;
    }
    else {
      StringTokenizer tokenizer = new StringTokenizer(this.line, " ");
      if (tokenizer.countTokens() != this.length) {
        System.err.print("Expected " + this.length + " historical events. ");
        System.err.println("Found " + tokenizer.countTokens() + ".");
        return false;
      }
      while (tokenizer.hasMoreTokens()) {
        this.historicalEvents.add(tokenizer.nextToken());
      }
    }

    return true;
  }

  /**
   * Gets the line containing a student's responses.
   * 
   * @param reader Used to read in a line.
   * @return True on success, false otherwise.
   * @throws IOException If there are problems reading in the file.
   */
  private boolean getStudentResponses(BufferedReader reader) throws IOException {
    if (reader == null) {
      return false;
    }
    this.line = reader.readLine();
    if (this.line == null) {
      System.err.println("Need at least one student's responses.");
      return false;
    }
    while (this.line != null) {
      StringTokenizer tokenizer = new StringTokenizer(this.line, " ");
      if (tokenizer.countTokens() != this.length) {
        System.err.print("Expected " + this.length + " responses by the student. ");
        System.err.println("Found " + tokenizer.countTokens() + ".");
        return false;
      }
      while (tokenizer.countTokens() > 0) {
        this.studentResponses.add(tokenizer.nextToken());
      }
      this.grades.add(this.gradeResponses());
      this.allResponses.addAll(this.studentResponses);
      this.allResponses.add("\n");
      this.studentResponses.clear();
      // System.out.println(this.line + "\n");
      this.line = reader.readLine();
    }
    return true;
  }

  /**
   * Iterates through the <code>historicalEvents</code> and <code>studentResponses</code> lists
   * twice to find the length of the longest sequence of events that are in the correct order; the
   * length that is returned is the student's grade.
   * 
   * @return The length of the longest sequence of events that are in the correct order, i.e. the
   * student's grade.
   */
  private Integer gradeResponses() {
    int gradeFirstPass = checkResponses(false);
    int gradeSecondPass = checkResponses(true);
    return (gradeFirstPass >= gradeSecondPass) ? gradeFirstPass : gradeSecondPass;
  }

  /**
   * Iterates through the <code>historicalEvents</code> and <code>studentResponses</code> lists to
   * find the length of the sequence of events that are in the correct order; the length that is
   * returned is the student's grade.
   * 
   * @param useHistoricalEvents True to remove an event from the <code>historicalEvents</code> list
   * first or false to remove the event from the <code>studentResponses</code> list first; the event
   * that is removed is then removed from the other list as well.
   * @return Student's grade.
   */
  private int checkResponses(boolean useHistoricalEvents) {
    List<String> tempHistoricalEvents = new ArrayList<String>();
    tempHistoricalEvents.addAll(this.historicalEvents);
    List<String> tempStudentResponses = new ArrayList<String>();
    tempStudentResponses.addAll(this.studentResponses);
    int grade = 0;

    String firstStudentResponse = tempStudentResponses.get(0);
    while (!firstStudentResponse.equals(tempHistoricalEvents.get(0))) {
      String temp = tempHistoricalEvents.get(0);
      tempStudentResponses.remove(temp);
      tempHistoricalEvents.remove(temp);
      // System.out.println("** Removed " + temp + ". **");
    }

    for (int index = 0; index < tempStudentResponses.size(); index++) {
      if (tempStudentResponses.get(index).equals(tempHistoricalEvents.get(index))) {
        grade++;
      }
      else {
        String historicalEvent = tempHistoricalEvents.get(index);
        String studentResponse = tempStudentResponses.get(index);
        String temp = (useHistoricalEvents) ? historicalEvent : studentResponse;
        tempHistoricalEvents.remove(temp);
        tempStudentResponses.remove(temp);
        // System.out.println("Removed " + temp + ".");
        index--;
      }
    }

    return grade;
  }

  /**
   * Prints the results of grading all students' responses to the screen.
   */
  public void printResults() {
    System.out.println("Historical Events:");
    for (String event : this.historicalEvents) {
      System.out.print(event + " ");
    }
    System.out.println("\n\nStudents' Responses:");
    for (String response : this.allResponses) {
      System.out.print(response);
      if (!"\n".equals(response)) {
        System.out.print(" ");
      }
    }
    System.out.println("\nGrades:");
    for (Integer grade : this.grades) {
      System.out.println(grade);
    }
  }

  /**
   * This program will grade students' responses as per the instructions found <a
   * href="http://uva.onlinejudge.org/external/1/111.pdf" target="_blank">here</a>.
   * 
   * @param args The filename.
   */
  public static void main(String[] args) {
    HistoryGrading grading = new HistoryGrading();
    if (grading.getInput(args[0])) {
      grading.printResults();
    }
  }
}
