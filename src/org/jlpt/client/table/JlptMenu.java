package org.jlpt.client.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.utils.FileUtils;
import org.jlpt.common.utils.Validator;

/**
 * A popup menu that is displayed when the user right-clicks on an entry in the table.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JlptMenu extends JPopupMenu {

  private final JTable table;

  /**
   * Creates a new JlptMenu instance.
   * 
   * @param table The table that contains the entries of Japanese words and English meanings.
   */
  public JlptMenu(JTable table) {
    super();
    Validator.checkNull(table);

    this.table = table;

    JMenuItem exportCsvMenuItem = new JMenuItem("Export to CSV");
    exportCsvMenuItem.addActionListener(new ExportAction());
    add(exportCsvMenuItem);

    // TODO: Add a Copy Entry to Clipboard menu item later.

  }

  /**
   * An action that exports the table model to a CSV file on the user's computer.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class ExportAction implements ActionListener {

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent event) {
      List<JapaneseEntry> entries = ((JlptTableModel) table.getModel()).getEntries();
      List<String> lines = new ArrayList<>();
      for (JapaneseEntry entry : entries) {
        lines.add(entry.getJword() + "," + entry.getReading() + "," + entry.getEnglishMeaning());
      }
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter =
          new FileNameExtensionFilter("Comma-separated file (*.csv)", "csv");
      chooser.setFileFilter(filter);
      int returnValue = chooser.showSaveDialog(JlptMenu.this);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        try {
          FileUtils.writeToFile(lines, chooser.getSelectedFile());
        }
        catch (IOException e) {
          // TODO: Replace with logger.
          System.err.println("There was a problem writing to the file.");
        }
      }
    }

  }

}
