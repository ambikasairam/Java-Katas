package org.katas.treelevel;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class is used to compare two nodes together.
 * 
 * @author BJ Peter DeLaCruz
 */
public class NodeComparator implements Comparator<Node>, Serializable {

  /**
   * Used for object serialization.
   */
  private static final long serialVersionUID = 1L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(Node arg0, Node arg1) {
    return arg0.getPosition().toUpperCase().compareTo(arg1.getPosition().toUpperCase());
  }

}
