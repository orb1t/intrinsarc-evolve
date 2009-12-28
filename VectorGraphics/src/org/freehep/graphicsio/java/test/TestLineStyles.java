// AUTOMATICALLY GENERATED by FreeHEP JAVAGraphics2D

package org.freehep.graphicsio.java.test;

import java.awt.*;

import org.freehep.graphics2d.*;
import org.freehep.graphicsio.java.*;
import org.freehep.graphicsio.test.*;

public class TestLineStyles extends TestingPanel
{

  public TestLineStyles(String[] args) throws Exception
  {
    super(args);
    setName("TestLineStyles");
  } // contructor

  public void paint(Graphics g)
  {
    vg[0] = VectorGraphics.create(g);
    vg[0].setCreator("FreeHEP JAVAGraphics2D");
    Paint0s0.paint(vg);
  } // paint

  private static class Paint0s0
  {
    public static void paint(VectorGraphics[] vg)
    {
      vg[0].setColor(new Color(0, 0, 0, 255));
      vg[0].setFont(new Font("Dialog", 0, 12));
      vg[1] = (VectorGraphics) vg[0].create();
      vg[1].setClip(0, 0, 600, 600);
      vg[1].setColor(new Color(255, 255, 255, 255));
      vg[1].fillRect(0, 0, 600, 600);
      vg[1].setColor(new Color(0, 0, 0, 255));
      vg[1].setStroke(new BasicStroke(0.0f, 2, 0, 10.0f, null, 0.0f));
      vg[1].drawLine(0, 15, 600, 15);
      vg[1].setStroke(new BasicStroke(3.0f, 0, 2, 10.0f, new float[]{5.0f, 2.0f}, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(166.0f, 166.0f),
          new JAVAGeneralPath.LineTo(34.0f, 166.0f), new JAVAGeneralPath.LineTo(34.0f, 133.0f),
          new JAVAGeneralPath.LineTo(166.0f, 133.0f), new JAVAGeneralPath.LineTo(166.0f, 67.0f),
          new JAVAGeneralPath.LineTo(34.0f, 67.0f), new JAVAGeneralPath.LineTo(34.0f, 34.0f),
          new JAVAGeneralPath.LineTo(166.0f, 34.0f)}));
      vg[1].setStroke(new BasicStroke(3.0f, 1, 0, 10.0f, new float[]{0.0f, 7.0f}, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(366.0f, 166.0f),
          new JAVAGeneralPath.LineTo(234.0f, 166.0f), new JAVAGeneralPath.LineTo(234.0f, 133.0f),
          new JAVAGeneralPath.LineTo(366.0f, 133.0f), new JAVAGeneralPath.LineTo(366.0f, 67.0f),
          new JAVAGeneralPath.LineTo(234.0f, 67.0f), new JAVAGeneralPath.LineTo(234.0f, 34.0f),
          new JAVAGeneralPath.LineTo(366.0f, 34.0f)}));
      vg[1].setStroke(new BasicStroke(3.0f, 2, 1, 10.0f, new float[]{10.0f, 5.0f, 2.0f, 5.0f}, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(566.0f, 166.0f),
          new JAVAGeneralPath.LineTo(434.0f, 166.0f), new JAVAGeneralPath.LineTo(434.0f, 133.0f),
          new JAVAGeneralPath.LineTo(566.0f, 133.0f), new JAVAGeneralPath.LineTo(566.0f, 67.0f),
          new JAVAGeneralPath.LineTo(434.0f, 67.0f), new JAVAGeneralPath.LineTo(434.0f, 34.0f),
          new JAVAGeneralPath.LineTo(566.0f, 34.0f)}));
      vg[1].setStroke(new BasicStroke(5.0f, 0, 2, 10.0f, new float[]{5.0f, 2.0f}, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(166.0f, 366.0f),
          new JAVAGeneralPath.LineTo(34.0f, 366.0f), new JAVAGeneralPath.LineTo(34.0f, 333.0f),
          new JAVAGeneralPath.LineTo(166.0f, 333.0f), new JAVAGeneralPath.LineTo(166.0f, 267.0f),
          new JAVAGeneralPath.LineTo(34.0f, 267.0f), new JAVAGeneralPath.LineTo(34.0f, 234.0f),
          new JAVAGeneralPath.LineTo(166.0f, 234.0f)}));
      vg[1].setStroke(new BasicStroke(5.0f, 1, 0, 10.0f, new float[]{0.0f, 7.0f}, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(366.0f, 366.0f),
          new JAVAGeneralPath.LineTo(234.0f, 366.0f), new JAVAGeneralPath.LineTo(234.0f, 333.0f),
          new JAVAGeneralPath.LineTo(366.0f, 333.0f), new JAVAGeneralPath.LineTo(366.0f, 267.0f),
          new JAVAGeneralPath.LineTo(234.0f, 267.0f), new JAVAGeneralPath.LineTo(234.0f, 234.0f),
          new JAVAGeneralPath.LineTo(366.0f, 234.0f)}));
      vg[1].setStroke(new BasicStroke(5.0f, 2, 1, 10.0f, new float[]{10.0f, 5.0f, 2.0f, 5.0f}, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(566.0f, 366.0f),
          new JAVAGeneralPath.LineTo(434.0f, 366.0f), new JAVAGeneralPath.LineTo(434.0f, 333.0f),
          new JAVAGeneralPath.LineTo(566.0f, 333.0f), new JAVAGeneralPath.LineTo(566.0f, 267.0f),
          new JAVAGeneralPath.LineTo(434.0f, 267.0f), new JAVAGeneralPath.LineTo(434.0f, 234.0f),
          new JAVAGeneralPath.LineTo(566.0f, 234.0f)}));
      vg[1].setStroke(new BasicStroke(20.0f, 0, 2, 10.0f, null, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(166.0f, 566.0f),
          new JAVAGeneralPath.LineTo(34.0f, 566.0f), new JAVAGeneralPath.LineTo(34.0f, 533.0f),
          new JAVAGeneralPath.LineTo(166.0f, 533.0f), new JAVAGeneralPath.LineTo(166.0f, 467.0f),
          new JAVAGeneralPath.LineTo(34.0f, 467.0f), new JAVAGeneralPath.LineTo(34.0f, 434.0f),
          new JAVAGeneralPath.LineTo(166.0f, 434.0f)}));
      vg[1].setStroke(new BasicStroke(20.0f, 1, 0, 10.0f, null, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(366.0f, 566.0f),
          new JAVAGeneralPath.LineTo(234.0f, 566.0f), new JAVAGeneralPath.LineTo(234.0f, 533.0f),
          new JAVAGeneralPath.LineTo(366.0f, 533.0f), new JAVAGeneralPath.LineTo(366.0f, 467.0f),
          new JAVAGeneralPath.LineTo(234.0f, 467.0f), new JAVAGeneralPath.LineTo(234.0f, 434.0f),
          new JAVAGeneralPath.LineTo(366.0f, 434.0f)}));
      vg[1].setStroke(new BasicStroke(20.0f, 2, 1, 10.0f, null, 0.0f));
      vg[1].draw(new JAVAGeneralPath(1, new JAVAGeneralPath.PathElement[]{new JAVAGeneralPath.MoveTo(566.0f, 566.0f),
          new JAVAGeneralPath.LineTo(434.0f, 566.0f), new JAVAGeneralPath.LineTo(434.0f, 533.0f),
          new JAVAGeneralPath.LineTo(566.0f, 533.0f), new JAVAGeneralPath.LineTo(566.0f, 467.0f),
          new JAVAGeneralPath.LineTo(434.0f, 467.0f), new JAVAGeneralPath.LineTo(434.0f, 434.0f),
          new JAVAGeneralPath.LineTo(566.0f, 434.0f)}));
      vg[1].dispose();
    } // paint
  } // class Paint0s0

  private VectorGraphics vg[] = new VectorGraphics[2];

  public static void main(String[] args) throws Exception
  {
    new TestLineStyles(args).runTest(600, 600);
  }
} // class
