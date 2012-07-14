package org.jlpt.client.table;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.utils.FileUtils;
import org.utils.Validator;

/**
 * An action that will export the contents of a table to a CSV file.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class ExportAction extends AbstractAction {

  private final JTable table;
  private final JComponent component;

  /**
   * Creates a new action that will export the contents of the table to a CSV file.
   * @param table The table whose contents are to be exported.
   * @param component The menu to which this action belongs.
   */
  public ExportAction(JTable table, JComponent component) {
    super("Export to CSV");
    Validator.checkNull(table);
    Validator.checkNull(component);

    this.table = table;
    this.component = component;
  }

  /** {@inheritDoc} */
  @Override
  public void actionPerformed(ActionEvent event) {
    List<JapaneseEntry> entries = ((JlptTableModel) this.table.getModel()).getEntries();
    List<String> lines = new ArrayList<>();
    for (JapaneseEntry entry : entries) {
      lines.add(entry.getJword() + "," + entry.getReading() + "," + entry.getEnglishMeaning());
    }
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter =
        new FileNameExtensionFilter("Comma-separated file (*.csv)", "csv");
    chooser.setFileFilter(filter);
    int returnValue = chooser.showSaveDialog(this.component);
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
