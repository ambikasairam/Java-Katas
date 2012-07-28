package org.jlpt.common.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A status bar that displays help messages or the current status of the application.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class StatusBar extends JPanel {

  private final JPanel innerPanel = new JPanel(new BorderLayout());

  /**
   * Creates a new StatusBar.
   */
  public StatusBar() {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(10, 23));
    setBackground(SystemColor.control);
    this.innerPanel.setOpaque(false);
    add(this.innerPanel, BorderLayout.CENTER);
  }

  /**
   * Adds a component to the status bar.
   *
   * @param component The component to add to the status bar.
   */
  public void addComponent(JComponent component, String position) {
    this.innerPanel.add(component, position);
  }

  /** {@inheritDoc} */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int y = 0;
    g.setColor(new Color(0, 0, 0));
    g.drawLine(0, y, getWidth(), y);
  }

}
