package org.katas;

/**
 * This class is used to create a Node object that will be put into a tree ADT.
 * 
 * @author BJ Peter DeLaCruz
 */
public class Node {

  private final int value;
  private final String position;
  private Node left;
  private Node right;

  /**
   * Creates a Node object.
   * 
   * @param value The value for the node.
   * @param position The position in the tree at which this node is located.
   */
  public Node(int value, String position) {
    this.value = value;
    this.position = position;
    this.left = null;
    this.right = null;
  }

  /**
   * Gets the value of a node.
   * 
   * @return The value of this node.
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Gets the position of a node.
   * 
   * @return The position of this node.
   */
  public String getPosition() {
    return this.position;
  }

  /**
   * Sets the left child of a node.
   * 
   * @param left The left child of this node.
   */
  public void setLeft(Node left) {
    this.left = left;
  }

  /**
   * Gets the left child of a node.
   * 
   * @return The left child of this node.
   */
  public Node getLeft() {
    return this.left;
  }

  /**
   * Sets the right child of a node.
   * 
   * @param right The right child of this node.
   */
  public void setRight(Node right) {
    this.right = right;
  }

  /**
   * Gets the right child of a node.
   * 
   * @return right The right child of this node.
   */
  public Node getRight() {
    return this.right;
  }

  /**
   * Prints the contents of this node.
   * 
   * @return A string representing the contents of this node.
   */
  @Override
  public String toString() {
    String info = "Node=[value=" + this.value + ",position=" + this.position;
    return info + ",left=" + this.left + ",right=" + this.right + "]";
  }
}
