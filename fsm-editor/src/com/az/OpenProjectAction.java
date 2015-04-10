package com.az;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import com.mxgraph.examples.swing.SCXMLGraphEditor;

/**
 * An action for opening an az project.
 */
@SuppressWarnings("serial")
public class OpenProjectAction extends AbstractAction
{
  private String lastDir;
  private File file;

  public OpenProjectAction()
  {
    this.file = null;
    this.lastDir = null;
  }

  public void actionPerformed(ActionEvent e)
  {
    SCXMLGraphEditor editor = SCXMLGraphEditor.getEditor(e);
    String wd = "";

    if (lastDir != null)
    {
      wd = lastDir;
    }
    else
    {
      if (editor.getCurrentFile() != null && editor.getCurrentFile().getParent() != null)
      {
        wd = editor.getCurrentFile().getParent();
      }
      else
      {
        String dirOfLastOpenedFile = editor.menuBar.getLastOpenedDir();

        if (dirOfLastOpenedFile != null)
        {
          wd = dirOfLastOpenedFile;
        }
        else
        {
          wd = System.getProperty("user.dir");
        }
      }
    }

    JFileChooser fc = new JFileChooser(wd);
    int rc = fc.showOpenDialog(null);

    if (rc == JFileChooser.APPROVE_OPTION)
    {
      File file = fc.getSelectedFile();
      editor.getProjectModel().loadProjectFile(fc.getSelectedFile());
      lastDir = fc.getSelectedFile().getParent();
    }
  }
}
