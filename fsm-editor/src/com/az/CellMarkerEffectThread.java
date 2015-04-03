package com.az;

import com.mxgraph.swing.handler.mxCellMarker;
import java.awt.Color;

/**
 * A class to encapsulate the unmarking behaviour of nodes and edges.
 */
public class CellMarkerEffectThread extends Thread
{
  private long effectDuration = 1500; // in milliseconds
  private AnimatedCellMarker cellMarker = null;
  private long startTime = 0;
  private long endTime = 0;
  private float percentComplete = 0.f;
  private boolean keepGoing = true;
  private float[] originalColors = null;

  public CellMarkerEffectThread(AnimatedCellMarker cellMarker)
  {
    this.cellMarker = cellMarker;
    this.keepGoing = true;
    this.startTime = System.currentTimeMillis();
    this.endTime = startTime + effectDuration;
    this.percentComplete = 0.f;
    originalColors = cellMarker.getValidColor().getColorComponents(null);
    start();
  }

  /**
   * This thread reduces the alpha channel of a color from 1.f to 0.f over a
   * period defined by effectDuration. When complete it tells the cellMarker to
   * remove mark the cell as unmarked.
   */
  public void run()
  {
    while (keepGoing)
    {
      try
      {
        if (cellMarker != null)
        {
          long currentTime = System.currentTimeMillis();

          if (endTime > currentTime)
          {
            long effectLifeSpan = currentTime - startTime;
            this.percentComplete = 1.f - ((float)effectLifeSpan / (float)effectDuration);
            cellMarker.unmarkingColor = new Color(originalColors[0], originalColors[1], originalColors[2], this.percentComplete);
            cellMarker.repaint();
          }
          else
          {
            keepGoing = false;
            cellMarker.setVisible(false);
            cellMarker.getParent().remove(cellMarker);
            cellMarker.setMarkedState(null);
            cellMarker.setUnmarking(false);
            cellMarker = null;
          }
        }

        Thread.sleep(100);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * If the node or edge is visited again while unmarking the effect resets to
   * the start.
   */
  public void restartEffect()
  {
    this.startTime = System.currentTimeMillis();
    this.endTime = startTime + effectDuration;
  }
}
