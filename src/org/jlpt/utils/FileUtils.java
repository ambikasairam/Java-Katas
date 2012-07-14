package org.jlpt.utils;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;
import org.jlpt.model.JapaneseEntry;
import org.jlpt.table.JlptMenu;
import org.jlpt.table.JlptTableModel;

/**
 * Utility class that contains methods for reading from and writing to a file. Contains a
 * <code>main</code> method that tests the <code>readFile</code> methods and displays the results in
 * a JFrame.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class FileUtils {

  /** Do not instantiate this class. */
  private FileUtils() {
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
    Validator.checkNotEmptyString(filename);

    FileInputStream fstream = new FileInputStream(filename);
    InputStreamReader in = new InputStreamReader(fstream, "UTF8");
    BufferedReader reader = new BufferedReader(in);

    List<String> lines = new ArrayList<>();
    String currentLine = "";
    while ((currentLine = reader.readLine()) != null) {
      lines.add(currentLine);
    }

    reader.close();

    return lines;
  }

  /**
   * Writes the given list of strings (lines) to the given file.
   * @param lines The lines to write to the file.
   * @param file The file to write to.
   * @throws IOException If there are problems writing to the file.
   */
  public static void writeToFile(List<String> lines, File file) throws IOException {
    Validator.checkNotEmpty(lines);

    FileOutputStream fstream = new FileOutputStream(file);
    OutputStreamWriter out = new OutputStreamWriter(fstream, "UTF8");
    BufferedWriter writer = new BufferedWriter(out);

    for (String line : lines) {
      writer.write(line);
      writer.newLine();
    }

    writer.close();
  }

  /**
   * Tests the <code>readFile</code> methods.
   * 
   * @param args The name of the file.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    try {
      List<String> lines = readFile(args[0]);
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
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      JFrame frame = new JFrame();
      frame.setTitle("JLPT Study v0.1");
      JlptTableModel model = new JlptTableModel(entries);
      final JTable table = new JTable(model);
      table.setShowVerticalLines(false);
      table.addMouseListener(new MouseInputAdapter() {

        @Override
        public void mouseClicked(MouseEvent event) {
          if (SwingUtilities.isRightMouseButton(event)) {
            JlptMenu menu = new JlptMenu(table);
            menu.show(table, event.getX(), event.getY());
          }
        }

      });
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
