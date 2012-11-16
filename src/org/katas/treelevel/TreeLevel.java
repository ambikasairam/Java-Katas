package org.katas.treelevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import org.katas.common.Kata;
import org.katas.common.KataUtils;

/**
 * This program builds one or more trees and then prints the contents of each in level order.
 * 
 * @author BJ Peter DeLaCruz
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/122_Tree_Level.pdf">Tree Level</a>
 */
public class TreeLevel extends Kata {

  /**
   * Creates a new TreeLevel object.
   */
  public TreeLevel() {
    // Empty constructor.
  }

  /**
   * Processes each line in a file until a "()" is reached, at which point a tree is constructed and
   * whose contents are printed in level order; repeats until the end of the file is reached.
   */
  @Override
  public void processLines() {
    while (!this.getLines().isEmpty()) {
      StringBuffer buffer = new StringBuffer();
      String line = this.getLines().remove(0) + " ";
      buffer.append(line);
      while (!line.contains("()")) {
        line = this.getLines().remove(0) + " ";
        buffer.append(line);
      }

      List<Node> nodes = this.createNodesList(buffer.toString());
      Node tree = this.buildTree(nodes);
      if (tree != null) {
        this.printLevelOrder(tree);
      }
    }
  }

  /**
   * Given a line containing pairs of values and paths, builds a list of nodes, each with a value
   * and path.
   * 
   * @param line The line read in from a file containing pairs of values and paths.
   * @return The list of nodes.
   */
  private List<Node> createNodesList(String line) {
    StringTokenizer tokenizer = new StringTokenizer(line, "(), ");
    List<String> strings = new ArrayList<String>();
    while (tokenizer.hasMoreTokens()) {
      strings.add(tokenizer.nextToken());
    }

    for (int index = 0; index < strings.size(); index += 2) {
      try {
        Integer.parseInt(strings.get(index));
      }
      catch (NumberFormatException e) {
        strings.add(index - 1, "-");
        break;
      }
    }

    List<Node> nodes = new ArrayList<Node>();
    for (int index = 0; index < strings.size(); index += 2) {
      int value = Integer.parseInt(strings.get(index));
      String position = strings.get(index + 1);
      nodes.add(new Node(value, position));
    }

    Collections.sort(nodes);
    return nodes;
  }

  /**
   * Given a list of nodes, builds a tree using the paths found in the nodes.
   * 
   * @param nodes The list of nodes with which to build a tree.
   * @return The root of the tree.
   */
  private Node buildTree(List<Node> nodes) {
    List<Node> tree = new ArrayList<Node>();
    tree.add(nodes.remove(0));
    if (!"-".equals(tree.get(0).getPosition())) {
      System.err.println("not complete");
      return null;
    }

    while (!nodes.isEmpty()) {
      Node node = nodes.remove(0);
      Node parent = tree.get(0);
      char[] path = node.getPosition().toUpperCase().toCharArray();
      boolean isInserted = false;
      for (char direction : path) {
        if (direction == 'L' && parent.getLeft() == null) {
          parent.setLeft(node);
          isInserted = true;
        }
        else if (direction == 'R' && parent.getRight() == null) {
          parent.setRight(node);
          isInserted = true;
        }
        else if (direction == 'L') {
          parent = parent.getLeft();
        }
        else if (direction == 'R') {
          parent = parent.getRight();
        }
        else {
          throw new IllegalArgumentException("Invalid character found: " + direction);
        }
      }
      if (!isInserted) {
        throw new IllegalArgumentException("Node not inserted into tree: " + node);
      }
    }

    return tree.get(0);
  }

  /**
   * Prints a tree in level order using a queue.
   * 
   * @param node The root of the tree.
   */
  private void printLevelOrder(Node node) {
    List<Node> queue = new ArrayList<Node>();
    queue.add(node);
    Node currentNode = null;

    while (true) {
      currentNode = queue.get(0);
      if (currentNode.getLeft() != null) {
        queue.add(currentNode.getLeft());
      }
      if (currentNode.getRight() != null) {
        queue.add(currentNode.getRight());
      }
      if (queue.size() > 1) {
        System.out.print(queue.remove(0).getValue() + " ");
      }
      else {
        break;
      }
    }
    System.out.println(queue.remove(0).getValue());
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing nodes with which to build one or more trees.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    TreeLevel treeLevel = new TreeLevel();
    treeLevel.setLines(KataUtils.readLines(args[0]));

    if (treeLevel.getLines() != null) {
      treeLevel.processLines();
    }
  }
}
