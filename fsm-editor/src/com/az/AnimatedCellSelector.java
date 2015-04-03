package com.az;

import com.mxgraph.swing.util.CellSelector;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellMarker;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

import com.az.AnimatedCellMarker;

/**
 * Derived version of CellSelector to support animated cell markers.
 */
public class AnimatedCellSelector extends CellSelector
{
  protected mxGraphComponent gc;
  private mxGraph graph;
  private mxGraphView view;
  private boolean withScroll;

  /**
   * Constructor just calls the super constructor and keeps a references to a
   * view variables that are needed in the overridden method below.
   */
  public AnimatedCellSelector(final mxGraphComponent gc, final boolean withScroll)
  {
    super(gc, withScroll);
    this.gc = gc;
    this.graph = gc.getGraph();
    this.view = graph.getView();
    this.withScroll = withScroll;
  }

  /**
   * Overriden version of the selectCell method that creates an
   * AnimatedCellMarker instead of an mxCellMarker.
   */
  @Override
  public void selectCell(mxCell c)
  {
    if (c != null)
    {
      mxCellMarker thisCellSelector = this.currentSelectedCells.get(c);
      mxCellState state = this.view.getState(c);

      if (thisCellSelector == null)
      {
        thisCellSelector = new AnimatedCellMarker(gc);
        this.currentSelectedCells.put(c, thisCellSelector);
        thisCellSelector.process(state, thisCellSelector.getMarkerColor(null, state, true), true);
        thisCellSelector.mark();

        if (this.withScroll)
        {
          this.gc.scrollCellToVisible(c, true);
        }
      }
      else
      {
        thisCellSelector.process(state, thisCellSelector.getMarkerColor(null, state, true), true);
        thisCellSelector.mark();
      }
    }
  }
}
