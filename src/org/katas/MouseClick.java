package org.katas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import org.katas.common.Kata;
import org.katas.common.KataUtils;

/**
 * This program processes a file containing coordinates for icons, regions, and mouse clicks, and
 * outputs all regions that were clicked on and all icons that were nearest to a mouse click.
 * 
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/142_Mouse_Clicks.pdf">Mouse Clicks</a>
 * 
 * @author BJ Peter DeLaCruz
 */
public class MouseClick extends Kata {

  private final List<Integer[][]> icons;
  private final List<Integer[][]> regions;
  private final List<Integer[][]> mouseClicks;

  /**
   * Creates a new MouseClick object.
   */
  public MouseClick() {
    this.icons = new ArrayList<Integer[][]>();
    this.regions = new ArrayList<Integer[][]>();
    this.mouseClicks = new ArrayList<Integer[][]>();
  }

  /**
   * Puts all icons, regions, and mouse clicks into three different lists and then processes each
   * mouse click.
   */
  @Override
  public void processLines() {
    for (int index = 1, letter = 65; !this.getLines().isEmpty();) {
      if ("#".equals(this.getLines().get(0))) {
        break;
      }

      // Read in all icons, regions, and mouse clicks first.
      StringTokenizer tokenizer = new StringTokenizer(this.getLines().remove(0), " ");
      while (tokenizer.hasMoreTokens()) {
        String type = tokenizer.nextToken();
        if ("I".equals(type)) {
          this.addIcon(tokenizer, index);
          index++;
        }
        else if ("R".equals(type)) {
          this.addRegion(tokenizer, letter);
          letter++;
        }
        else {
          this.addMouseClick(tokenizer);
        }
      }
    }

    // Don't process icons that are covered by a region.
    for (int index = 0; index < this.icons.size(); index++) {
      if (this.isInsideRegion(this.icons.get(index)) != null) {
        this.icons.remove(index);
      }
    }

    processMouseClicks();
  }

  /**
   * Adds an icon to a list of icons.
   * 
   * @param tokenizer The tokenizer that contains the coordinates for the icon.
   * @param index The index associated with an icon; starting from 1.
   */
  private void addIcon(StringTokenizer tokenizer, int index) {
    Integer[][] icon = new Integer[1][3];
    icon[0][0] = Integer.parseInt(tokenizer.nextToken());
    icon[0][1] = Integer.parseInt(tokenizer.nextToken());
    icon[0][2] = index;
    this.icons.add(icon);
  }

  /**
   * Adds a region to the list of regions.
   * 
   * @param tokenizer The tokenizer that contains the coordinates for the region.
   * @param letter The letter associated with a region; starting from A.
   */
  private void addRegion(StringTokenizer tokenizer, int letter) {
    Integer[][] region = new Integer[1][5];
    region[0][0] = Integer.parseInt(tokenizer.nextToken());
    region[0][1] = Integer.parseInt(tokenizer.nextToken());
    region[0][2] = Integer.parseInt(tokenizer.nextToken());
    region[0][3] = Integer.parseInt(tokenizer.nextToken());
    region[0][4] = letter;
    this.regions.add(region);
  }

  /**
   * Adds a mouse click to the list of mouse clicks.
   * 
   * @param tokenizer The tokenizer that contains the coordinates for the mouse click.
   */
  private void addMouseClick(StringTokenizer tokenizer) {
    Integer[][] mouseClick = new Integer[1][2];
    mouseClick[0][0] = Integer.parseInt(tokenizer.nextToken());
    mouseClick[0][1] = Integer.parseInt(tokenizer.nextToken());
    this.mouseClicks.add(mouseClick);
  }

  /**
   * Processes all mouse clicks and prints the regions that were clicked on and the icons that were
   * nearest to a mouse click.
   */
  private void processMouseClicks() {
    for (Integer[][] click : this.mouseClicks) {
      Integer[][] region = this.isInsideRegion(click);
      if (region == null) {
        List<Double> distances = new ArrayList<Double>();
        List<Integer> positions = new ArrayList<Integer>();
        for (Integer[][] icon : this.icons) {
          distances.add(this.distanceFormula(click, icon));
          positions.add(icon[0][2]);
        }

        List<Double> minimums = new ArrayList<Double>();
        List<Integer> minPositions = new ArrayList<Integer>();
        minimums.add(distances.remove(0));
        minPositions.add(positions.remove(0));
        while (!distances.isEmpty()) {
          if (minimums.get(0) > distances.get(0)) {
            minimums.clear();
            minPositions.clear();
            minimums.add(distances.remove(0));
            minPositions.add(positions.remove(0));
          }
          else if (minimums.get(0).equals(distances.get(0))) {
            minimums.add(distances.remove(0));
            minPositions.add(positions.remove(0));
          }
          else {
            distances.remove(0);
            positions.remove(0);
          }
        }

        for (int index = 0; index < minPositions.size() - 1; index++) {
          System.out.print("   " + minPositions.get(index));
        }
        System.out.println("   " + minPositions.get(minPositions.size() - 1));
      }
      else {
        System.out.println(Character.toChars(region[0][4]));
      }
    }
  }

  /**
   * Calculates the distance formula:
   * 
   * <pre>
   * <code>
   *    distance = sqrt((x1 - x2)^2 + (y1 - y2)^2).
   * </code>
   * </pre>
   * 
   * @param coordinates1 (x1, y1).
   * @param coordinates2 (x2, y2).
   * @return The distance between two points.
   */
  private double distanceFormula(Integer[][] coordinates1, Integer[][] coordinates2) {
    int x1 = coordinates1[0][0];
    int x2 = coordinates2[0][0];
    int y1 = coordinates1[0][1];
    int y2 = coordinates2[0][1];

    x2 -= x1;
    x2 *= x2;
    y2 -= y1;
    y2 *= y2;

    return Math.sqrt(x2 + y2);
  }

  /**
   * Gets a region if a pair of coordinates is located within it.
   * 
   * @param coordinates The x and y coordinates of an icon or a mouse click.
   * @return The region if the given pair of coordinates is located within it, or <code>null</code>
   * otherwise.
   */
  private Integer[][] isInsideRegion(Integer[][] coordinates) {
    int x = coordinates[0][0], y = coordinates[0][1];
    int topLeftX, topLeftY, bottomRightX, bottomRightY;
    Integer[][] result = null;

    Collections.reverse(this.regions);
    for (Integer[][] region : this.regions) {
      topLeftX = region[0][0];
      topLeftY = region[0][1];
      bottomRightX = region[0][2];
      bottomRightY = region[0][3];
      if (x <= bottomRightX && x >= topLeftX && y >= topLeftY && y <= bottomRightY) {
        result = region;
        break;
      }
    }

    Collections.reverse(this.regions);
    return (result == null) ? null : result;
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing coordinates for icons, regions, and mouse clicks.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    MouseClick mouseClick = new MouseClick();
    mouseClick.setLines(KataUtils.readLines(args[0]));

    if (mouseClick.getLines() != null) {
      mouseClick.processLines();
    }
  }
}
