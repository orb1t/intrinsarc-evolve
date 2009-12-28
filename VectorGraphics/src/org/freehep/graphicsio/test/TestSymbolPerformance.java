// University of California, Santa Cruz, USA and
// CERN, Geneva, Switzerland, Copyright (c) 2000
package org.freehep.graphicsio.test;

import java.awt.*;

import javax.swing.*;

import org.freehep.graphics2d.*;

/**
 * @author Charles Loomis
 * @author Mark Donszelmann
 * @version $Id: TestSymbolPerformance.java,v 1.1 2006-07-03 13:33:11 amcveigh
 *          Exp $
 */
public class TestSymbolPerformance extends JPanel
{

  public TestSymbolPerformance()
  {
    setOpaque(false);
  }

  public void paintComponent(Graphics g)
  {

    VectorGraphics vg = VectorGraphics.create(g);

    Dimension dim = getSize();
    Insets insets = getInsets();

    vg.setColor(Color.RED);
    vg.fillRect(insets.left, insets.top, dim.width - insets.left - insets.right, dim.height - insets.top
        - insets.bottom);

    vg.setColor(Color.BLACK);
    writeSymbols(10000, vg, 0, dim.height / 2, dim.width, VectorGraphicsConstants.SYMBOL_STAR, 4);
    writeSymbols(10000, vg, dim.height / 2, dim.height / 2, dim.width, VectorGraphicsConstants.SYMBOL_CIRCLE, 256);
  }

  private void writeSymbols(int n, VectorGraphics vg, double yo, double height, double width, int type,
      int levelsPerColor)
  {

    long start = System.currentTimeMillis();

    for (int i = 0; i < n; i++)
    {
      double x = Math.random() * width;
      double y = yo + 20 + Math.random() * (height - 20);
      int colorUnit = 256 / levelsPerColor;
      int r = (int) (Math.random() * 256 / colorUnit) * colorUnit;
      int g = (int) (Math.random() * 256 / colorUnit) * colorUnit;
      int b = (int) (Math.random() * 256 / colorUnit) * colorUnit;
      vg.setColor(new Color(r, g, b));
      vg.fillSymbol(x, y, 6, type);
    }
    long end = System.currentTimeMillis();

    vg.setColor(Color.BLACK);
    vg.drawString(n + " symbols of type " + type + " filled in " + (end - start) + " ms", 10, yo + 15);
  }

  public static void main(String[] args)
  {

    // Create a new frame to hold everything.
    TestingFrame frame = new TestingFrame("Test Symbol Performance");

    // Create a new instance of this class and add it to the frame.
    frame.addContent(new TestSymbolPerformance());

    // Give the frame a size and make it visible.
    frame.pack();
    frame.setSize(new Dimension(600, 600));
    frame.show();
  }
}
