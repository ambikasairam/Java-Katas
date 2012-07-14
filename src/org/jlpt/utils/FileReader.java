package org.jlpt.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import org.jlpt.model.JapaneseEntry;
import org.jlpt.table.JlptTableModel;
import org.utils.Validator;

/**
 * Utility class that contains methods for reading in from a file. Contains a <code>main</code>
 * method that tests the methods and displays the results in a JFrame.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class FileReader {

  /** Do not instantiate this class. */
  private FileReader() {
    // Empty constructor.
  }

  /**
   * Reads in the file at the given URL.
   * 
   * @param url The URL of the file.
   * @throws IOException If there are problems reading in the file.
   * @return The list of strings (lines) read in from the file.
   */
  public static List<String> readFile(URL url) throws IOException {
    Validator.checkNull(url);
    return readFile(url.getFile());
  }

  /**
   * Reads in the file at the given URL.
   * 
   * @param filename The name of the file.
   * @throws IOException If there are problems reading in the file.
   * @return The list of strings (lines) read in from the file.
   */
  public static List<String> readFile(String filename) throws IOException {
    Validator.checkIfNotEmptyString(filename);

    FileInputStream fstream = new FileInputStream(filename);
    InputStreamReader in = new InputStreamReader(fstream, "UTF8");
    BufferedReader reader = new BufferedReader(in);

    List<String> lines = new ArrayList<>();
    String currentLine = "";
    while ((currentLine = reader.readLine()) != null) {
      lines.add(currentLine);
    }

    reader.close();
    in.close();
    fstream.close();

    return lines;
  }

  /**
   * Tests the <code>readFile</code> methods.
   * 
   * @param args None.
   */
  public static void main(String... args) {
    try {
      String filename = "C:\\Users\\BJ Peter DeLaCruz\\workspace\\Katas\\";
      List<String> lines = readFile(filename + "src\\org\\jlpt\\utils\\japanese_words.csv");
      List<JapaneseEntry> entries = new ArrayList<>();
      for (String line : lines) {
        String[] words = line.split(";");
        if (words.length != 3) {
          // Ignore entries that do not have exactly three fields.
          continue;
        }
        JapaneseEntry entry = new JapaneseEntry(words[0], words[1], words[2]);
        entries.add(entry);
      }
      JFrame frame = new JFrame();
      frame.setTitle("JLPT Study");
      JTable table = new JTable(new JlptTableModel(entries));
      table.setShowVerticalLines(false);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      JScrollPane pane =
          new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(pane);
      frame.pack();
      frame.setVisible(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
