package org.jlpt.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
   * @param path The path representing the file to write to.
   * @throws IOException If there are problems writing to the file.
   */
  public static void writeToFile(List<String> lines, Path path) throws IOException {
    Validator.checkNotEmpty(lines);
    Validator.checkNull(path);

    writeToFile(lines, path.toFile());
  }

  /**
   * Writes the given list of strings (lines) to the given file.
   * @param lines The lines to write to the file.
   * @param file The file to write to.
   * @throws IOException If there are problems writing to the file.
   */
  public static void writeToFile(List<String> lines, File file) throws IOException {
    Validator.checkNotEmpty(lines);
    Validator.checkNull(file);

    FileOutputStream fstream = new FileOutputStream(file);
    OutputStreamWriter out = new OutputStreamWriter(fstream, "UTF8");
    BufferedWriter writer = new BufferedWriter(out);

    for (String line : lines) {
      writer.write(line);
      writer.newLine();
    }

    writer.close();
  }

}
