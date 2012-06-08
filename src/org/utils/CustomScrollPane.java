package org.utils;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * A JScrollPane that will forward a mouse wheel scroll event to the parent scroll pane, if one
 * exists, when it reaches either the top or the bottom.
 * 
 * Code taken from <a href="http://stackoverflow.com/a/1379695/739379" target="_blank">this
 * answer</a> on Stack Overflow.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
@SuppressWarnings("serial")
public class CustomScrollPane extends JScrollPane {

  /**
   * Creates a custom scroll pane.
   */
  public CustomScrollPane() {
    super();
    addMouseWheelListener(new MouseWheelListenerImpl());
  }

  /**
   * Creates a custom scroll pane with a JTable inside it.
   * 
   * @param table The table to add to this scroll pane.
   */
  public CustomScrollPane(JTable table) {
    super(table);
    addMouseWheelListener(new MouseWheelListenerImpl());
  }

  /**
   * A mouse wheel listener that will forward a mouse wheel scroll event to the parent scroll pane,
   * if one exists, when this scroll pane reaches either the top or the bottom.
   */
  class MouseWheelListenerImpl implements MouseWheelListener {

    private JScrollBar bar;
    private int previousValue = 0;

    /**
     * Returns the parent scroll pane, or null if none exists.
     * 
     * @return The parent scroll pane, or null if none exists.
     */
    private JPanel getParentPanel() {
      Component parent = getParent();
      while (!(parent instanceof JPanel) && parent != null) {
        parent = parent.getParent();
      }
      if (parent == null) {
        return null;
      }
      return (JPanel) parent;
    }

    /** Creates a new mouse wheel listener. */
    public MouseWheelListenerImpl() {
      bar = CustomScrollPane.this.getVerticalScrollBar();
    }

    /**
     * Forwards a mouse wheel event to the parent scroll pane if one exists.
     * 
     * @param event The event to forward to the parent scroll pane.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
      JPanel parent = getParentPanel();
      if (parent == null) {
        CustomScrollPane.this.removeMouseWheelListener(this);
        return;
      }

      // User reaches the top.
      if (event.getWheelRotation() < 0 && bar.getValue() == 0 && previousValue == 0) {
        parent.dispatchEvent(cloneEvent(event, parent));
      }
      // User reaches the bottom.
      else if (bar.getValue() == getMax() && previousValue == getMax()) {
        parent.dispatchEvent(cloneEvent(event, parent));
      }
      previousValue = bar.getValue();
    }

    /**
     * Returns the maximum for the scroll bar.
     * 
     * @return The maximum for the scroll bar.
     */
    private int getMax() {
      return bar.getMaximum() - bar.getVisibleAmount();
    }

    /**
     * Returns a new copy of the mouse wheel event.
     * 
     * @param event The event to copy.
     * @param parent The source of the event, i.e. the component that is getting the forwarded
     * event.
     * @return A copy of the given mouse wheel event.
     */
    private MouseWheelEvent cloneEvent(MouseWheelEvent event, JPanel parent) {
      return new MouseWheelEvent(parent, event.getID(), event.getWhen(), event.getModifiers(), 1,
          1, event.getClickCount(), false, event.getScrollType(), event.getScrollAmount(),
          event.getWheelRotation());
    }
  }
}
