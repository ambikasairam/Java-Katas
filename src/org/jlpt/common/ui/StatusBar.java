package org.jlpt.common.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;
import javax.swing.JPanel;

/**
 * A status bar that displays help messages or the current status of the application.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class StatusBar extends JPanel {

  /**
   * Creates a new StatusBar.
   */
  public StatusBar() {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(10, 23));
    setBackground(SystemColor.control);
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
