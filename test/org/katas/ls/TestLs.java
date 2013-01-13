package org.katas.ls;

import static org.junit.Assert.assertTrue;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests for the {@link Ls} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestLs {

  /**
   * Tests the methods in the {@link Ls} class.
   * 
   * @throws Exception If a problem occurred while reading in the list of filenames from a file.
   */
  @Test
  public void testLsClass() throws Exception {
    List<String> filenames =
        Files.readAllLines(Paths.get(getClass().getResource("test.txt").toURI()),
            Charset.defaultCharset());

    List<String> list = new ArrayList<>();
    list.addAll(filenames.subList(1, filenames.indexOf("12")));
    String table = "------------------------------------------------------------\n";
    table += "12345678.123         2short4me          \n";
    table += "mid_size_name        much_longer_name   \n";
    table += "shorter              size-1             \n";
    table += "size2                size3              \n";
    table += "tiny                 very_long_file_name\n";
    table += "------------------------------------------------------------\n";
    assertTrue(Ls.getFilesTable(list).equals(table));
    assertTrue(Ls.getNumberOfColumns(19) == 2);

    list.clear();
    list.addAll(filenames.subList(filenames.indexOf("12") + 1, filenames.indexOf("19")));
    table = "------------------------------------------------------------\n";
    table += "Alfalfa        Buckwheat      Butch          Cotton       \n";
    table += "Darla          Froggy         Joe            Mrs_Crabapple\n";
    table += "P.D.           Porky          Stimey         Weaser       \n";
    table += "------------------------------------------------------------\n";
    assertTrue(Ls.getFilesTable(list).equals(table));
    assertTrue(Ls.getNumberOfColumns(13) == 4);

    list.clear();
    list.addAll(filenames.subList(filenames.indexOf("19") + 1, filenames.size() - 1));
    table = "------------------------------------------------------------\n";
    table += "Alice       Bobby       Buffy       Carol       Chris     \n";
    table += "Cindy       Danny       Greg        Jan         Jody      \n";
    table += "Keith       Lori        Marsha      Mike        Mr._French\n";
    table += "Peter       Shirley     Sissy       \n";
    table += "------------------------------------------------------------\n";
    assertTrue(Ls.getFilesTable(list).equals(table));
    assertTrue(Ls.getNumberOfColumns(10) == 5);
  }
}
