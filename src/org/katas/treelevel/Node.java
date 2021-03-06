/*******************************************************************************
 * Copyright (C) 2012 BJ Peter DeLaCruz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.katas.treelevel;

import java.util.Locale;
import java.util.Objects;

/**
 * This class is used to create a Node object that will be put into a tree.
 * 
 * @author BJ Peter DeLaCruz
 */
final class Node implements Comparable<Node> {

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

  /** {@inheritDoc} */
  @Override
  public String toString() {
    String info = "Node=[value=" + this.value + ",position=" + this.position;
    return info + ",left=" + this.left + ",right=" + this.right + "]";
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Node)) {
      return false;
    }
    Node node = (Node) object;
    return value == node.value && Objects.equals(position, node.position)
        && Objects.equals(left, node.left) && Objects.equals(right, node.right);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(value, position, left, right);
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(Node node) {
    return position.toUpperCase(Locale.US).compareTo(node.position.toUpperCase(Locale.US));
  }

}
