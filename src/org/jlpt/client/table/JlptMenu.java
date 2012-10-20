package org.jlpt.client.table;

import java.awt.Point;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import org.jlpt.common.utils.Validator;

/**
 * A popup menu that is displayed when the user right-clicks on an entry in the table.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JlptMenu extends JPopupMenu {

  private Point point;

  /**
   * Creates a new JlptMenu instance.
   * 
   * @param table The table that contains the entries of Japanese words and English meanings.
   */
  public JlptMenu(JTable table) {
    super();
    Validator.checkNull(table);

    add(new ExportAction(table, this));
    add(new JSeparator());

    // TODO: Add a Copy Entry to Clipboard menu item later.

  }

  /**
   * Sets the point at which the mouse right-clicked.
   * 
   * @param point The point.
   */
  public void setPoint(Point point) {
    this.point = new Point(point.x, point.y);
  }

  /** @return The point at which the mouse right-clicked. */
  public Point getPoint() {
    return this.point;
  }

}
