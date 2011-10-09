package com.plum.tinyos.ui;

import java.awt.*;
import javax.swing.*;

public class ToolBarButton extends JButton {
	SiteManager sm;
	private static final Insets margins = new Insets(0,0,0,0);

	public ToolBarButton(String text, SiteManager sm) {
		this.sm = sm;
		setMargin(margins);
		//this.setB
		setVerticalTextPosition(BOTTOM);
		setHorizontalTextPosition(CENTER);
		setText(text);
	}
}