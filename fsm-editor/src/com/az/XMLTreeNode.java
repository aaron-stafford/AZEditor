package com.az;

import org.w3c.dom.Element;

/**
 * A class that encapsulates the nodes of the az project tree view.
 */
public class XMLTreeNode
{
  private Element element;

  public XMLTreeNode(Element element)
  {
    this.element = element;
  }

  public Element getElement()
  {
    return element;
  }

  public String toString()
  {
    String value = this.getDisplayName();

    if (value != null && !value.equals(""))
    {
      return value;
    }

    return element.getNodeName();
  }

  public String getDiagram()
  {
    return element.getAttribute("diagram");
  }

  public String getDisplayName()
  {
    return element.getAttribute("displayName");
  }
}
