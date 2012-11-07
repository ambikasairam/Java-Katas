package org.utils;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * A custom table header that prevents one or more columns from being moved.
 * 
 * @author Ganesh Kumar
 * @author BJ Peter DeLaCruz
 */
public class CustomTableHeader extends JTableHeader {
  private static final long serialVersionUID = 1L;
  private int columnIndex = 1;

  /**
   * Creates a new CustomTableHeader object with a given TableColumnModel.
   * 
   * @param model The TableColumnModel used in the table.
   */
  public CustomTableHeader(TableColumnModel model) {
    super(model);
  }

  /**
   * Sets the index of the column that should not be moved to that in the view.
   * 
   * @param viewIndex The index of the column that should not be moved.
   */
  public void setColumnIndex(int viewIndex) {
    columnIndex = viewIndex;
  }

  /**
   * Prevents a column at the given index from being moved. If the column to the left of it is
   * dragged to the right, don't allow it. Likewise, if the column to the right of it is dragged to
   * the left, don't allow it either.
   * 
   * @return The table column that is being dragged by the mouse.
   */
  @Override
  public TableColumn getDraggedColumn() {
    TableColumn draggedColumn = super.getDraggedColumn();
    if (draggedColumn != null) {
      int modelIndex = draggedColumn.getModelIndex();
      int index = getTable().convertColumnIndexToView(modelIndex);

      if (index == columnIndex) {
        super.setDraggedColumn(null);
      }
      else if (super.draggedDistance > 0 && index + 1 == columnIndex) {
        super.setDraggedColumn(null);
      }
      else if (super.draggedDistance < 0 && index - 1 == columnIndex) {
        super.setDraggedColumn(null);
      }
    }
    return draggedColumn;
  }
}