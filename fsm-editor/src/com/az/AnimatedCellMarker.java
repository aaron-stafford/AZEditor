package com.az;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import com.mxgraph.view.mxCellState;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEvent;

/**
 *
 */
public class AnimatedCellMarker extends mxCellMarker
{
  /**
   * Holds the color to be used when unmarking.
   */
  protected transient Color unmarkingColor;

  /**
   * Flag to indicate if this cell is currently playing an unmark effect
   */
  protected transient boolean unmarking = false;

  /**
   * A thread that updates the color for the animated unmarking effect.
   */
  private CellMarkerEffectThread effectThread = null;

  public AnimatedCellMarker(mxGraphComponent gc)
  {
    super(gc);
  }

  protected Color getValidColor()
  {
    return validColor;
  }

  protected void setMarkedState(mxCellState markedState)
  {
    this.markedState = markedState;
  }

  protected boolean getUnmarking()
  {
    return unmarking;
  }

  protected void setUnmarking(boolean unmarking)
  {
    this.unmarking = unmarking;
  }

  /**
   * Overridden version of the base class process method to handle unmarking.
   */
  @Override
  public void process(mxCellState state, Color color, boolean isValid)
  {
    if (isValid)
    {
      validState = state;
    }
    else
    {
      validState = null;
    }

    if (state != markedState || color != currentColor)
    {
      currentColor = color;

      if (state != null && currentColor != null)
      {
        markedState = state;
        mark();
      }
      else if (markedState != null)
      {
        unmark();
      }
    }
    else if (state == markedState && unmarking)
    {
      unmark();
    }
  }

  /**
   * Overridden version of the base class unmark method to handle the case
   * where unmarking is in progress.
   */
  @Override
  public void unmark()
  {
    if (getParent() != null)
    {
      if (unmarking)
      {
        if (validState != null)
        {
          effectThread.restartEffect();
        }

        return;
      }

      unmarking = true;
      effectThread = new CellMarkerEffectThread(this);
      eventSource.fireEvent(new mxEventObject(mxEvent.MARK));
    }
  }

  /**
   * Overridden version of base class paint method to handle the unmarking
   * state.
   */
  @Override
  public void paint(Graphics g)
  {
    if (markedState != null && currentColor != null)
    {
      ((Graphics2D) g).setStroke(DEFAULT_STROKE);

      if (unmarking)
      {
        g.setColor(unmarkingColor);
      }
      else
      {
        g.setColor(currentColor);
      }

      if (markedState.getAbsolutePointCount() > 0)
      {
        Point last = markedState.getAbsolutePoint(0).getPoint();

        for (int i = 1; i < markedState.getAbsolutePointCount(); i++)
        {
          Point current = markedState.getAbsolutePoint(i).getPoint();
          g.drawLine(last.x - getX(), last.y - getY(), current.x
                     - getX(), current.y - getY());
          last = current;
        }
      }
      else
      {
        g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
      }
    }
  }
}
