package com.plum.tinyos.log;


import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import com.plum.tinyos.model.PlumConstants;

public class LogWindow extends JInternalFrame {
  private int width;

  private int height;

  private JTextArea textArea = null;

  private JScrollPane pane = null;

  public LogWindow(String title, int width, int height) {
    super(title);
    setSize(width, height);
    textArea = new JTextArea();
    pane = new JScrollPane(textArea);
    getContentPane().add(pane);
    setVisible(true);
    setLocation(0,PlumConstants.LOG_WINDOW_OFFSET_Y);
  }

  /**
   * This method appends the data to the text area.
   * 
   * @param data
   *            the Logging information data
   */
  public void showInfo(String data) {
    textArea.append(data);
    this.getContentPane().validate();
  }
}
