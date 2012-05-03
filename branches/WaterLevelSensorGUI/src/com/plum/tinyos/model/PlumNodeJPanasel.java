package com.plum.tinyos.model;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;

public class PlumNodeJPanasel extends JPanel {

	private BindingGroup m_bindingGroup;
	private com.plum.tinyos.model.PlumNode plumNode = new com.plum.tinyos.model.PlumNode();
	private JSlider batteryLeftJSlider;
	private JSlider idJSlider;
	private JSlider spaceLeftJSlider;
	private JTextField unixTimeJTextField;

	public PlumNodeJPanasel(com.plum.tinyos.model.PlumNode newPlumNode) {
		this();
		setPlumNode(newPlumNode);
	}

	public PlumNodeJPanasel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0E-4 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0E-4 };
		setLayout(gridBagLayout);

		JLabel batteryLeftLabel = new JLabel("BatteryLeft:");
		GridBagConstraints labelGbc_0 = new GridBagConstraints();
		labelGbc_0.insets = new Insets(5, 5, 5, 5);
		labelGbc_0.gridx = 0;
		labelGbc_0.gridy = 0;
		add(batteryLeftLabel, labelGbc_0);

		batteryLeftJSlider = new JSlider();
		GridBagConstraints componentGbc_0 = new GridBagConstraints();
		componentGbc_0.insets = new Insets(5, 0, 5, 5);
		componentGbc_0.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_0.gridx = 1;
		componentGbc_0.gridy = 0;
		add(batteryLeftJSlider, componentGbc_0);

		JLabel idLabel = new JLabel("Id:");
		GridBagConstraints labelGbc_1 = new GridBagConstraints();
		labelGbc_1.insets = new Insets(5, 5, 5, 5);
		labelGbc_1.gridx = 0;
		labelGbc_1.gridy = 1;
		add(idLabel, labelGbc_1);

		idJSlider = new JSlider();
		GridBagConstraints componentGbc_1 = new GridBagConstraints();
		componentGbc_1.insets = new Insets(5, 0, 5, 5);
		componentGbc_1.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_1.gridx = 1;
		componentGbc_1.gridy = 1;
		add(idJSlider, componentGbc_1);

		JLabel spaceLeftLabel = new JLabel("SpaceLeft:");
		GridBagConstraints labelGbc_2 = new GridBagConstraints();
		labelGbc_2.insets = new Insets(5, 5, 5, 5);
		labelGbc_2.gridx = 0;
		labelGbc_2.gridy = 2;
		add(spaceLeftLabel, labelGbc_2);

		spaceLeftJSlider = new JSlider();
		GridBagConstraints componentGbc_2 = new GridBagConstraints();
		componentGbc_2.insets = new Insets(5, 0, 5, 5);
		componentGbc_2.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_2.gridx = 1;
		componentGbc_2.gridy = 2;
		add(spaceLeftJSlider, componentGbc_2);

		JLabel unixTimeLabel = new JLabel("UnixTime:");
		GridBagConstraints labelGbc_3 = new GridBagConstraints();
		labelGbc_3.insets = new Insets(5, 5, 5, 5);
		labelGbc_3.gridx = 0;
		labelGbc_3.gridy = 3;
		add(unixTimeLabel, labelGbc_3);

		unixTimeJTextField = new JTextField();
		GridBagConstraints componentGbc_3 = new GridBagConstraints();
		componentGbc_3.insets = new Insets(5, 0, 5, 5);
		componentGbc_3.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_3.gridx = 1;
		componentGbc_3.gridy = 3;
		add(unixTimeJTextField, componentGbc_3);

		if (plumNode != null) {
			m_bindingGroup = initDataBindings();
		}
	}

	protected BindingGroup initDataBindings() {
		BeanProperty<com.plum.tinyos.model.PlumNode, java.lang.Integer> batteryLeftProperty = BeanProperty
				.create("batteryLeft");
		BeanProperty<javax.swing.JSlider, java.lang.Integer> valueProperty = BeanProperty
				.create("value");
		AutoBinding<com.plum.tinyos.model.PlumNode, java.lang.Integer, javax.swing.JSlider, java.lang.Integer> autoBinding = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, plumNode,
						batteryLeftProperty, batteryLeftJSlider, valueProperty);
		autoBinding.bind();
		//
		BeanProperty<com.plum.tinyos.model.PlumNode, java.lang.Integer> idProperty = BeanProperty
				.create("id");
		BeanProperty<javax.swing.JSlider, java.lang.Integer> valueProperty_1 = BeanProperty
				.create("value");
		AutoBinding<com.plum.tinyos.model.PlumNode, java.lang.Integer, javax.swing.JSlider, java.lang.Integer> autoBinding_1 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, plumNode,
						idProperty, idJSlider, valueProperty_1);
		autoBinding_1.bind();
		//
		BeanProperty<com.plum.tinyos.model.PlumNode, java.lang.Integer> spaceLeftProperty = BeanProperty
				.create("spaceLeft");
		BeanProperty<javax.swing.JSlider, java.lang.Integer> valueProperty_2 = BeanProperty
				.create("value");
		AutoBinding<com.plum.tinyos.model.PlumNode, java.lang.Integer, javax.swing.JSlider, java.lang.Integer> autoBinding_2 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, plumNode,
						spaceLeftProperty, spaceLeftJSlider, valueProperty_2);
		autoBinding_2.bind();
		//
		BeanProperty<com.plum.tinyos.model.PlumNode, java.lang.String> unixTimeProperty = BeanProperty
				.create("unixTime");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty = BeanProperty
				.create("text");
		AutoBinding<com.plum.tinyos.model.PlumNode, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_3 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, plumNode,
						unixTimeProperty, unixTimeJTextField, textProperty);
		autoBinding_3.bind();
		//
		BindingGroup bindingGroup = new BindingGroup();
		bindingGroup.addBinding(autoBinding);
		bindingGroup.addBinding(autoBinding_1);
		bindingGroup.addBinding(autoBinding_2);
		bindingGroup.addBinding(autoBinding_3);
		//
		return bindingGroup;
	}

	public com.plum.tinyos.model.PlumNode getPlumNode() {
		return plumNode;
	}

	public void setPlumNode(com.plum.tinyos.model.PlumNode newPlumNode) {
		setPlumNode(newPlumNode, true);
	}

	public void setPlumNode(com.plum.tinyos.model.PlumNode newPlumNode,
			boolean update) {
		plumNode = newPlumNode;
		if (update) {
			if (m_bindingGroup != null) {
				m_bindingGroup.unbind();
				m_bindingGroup = null;
			}
			if (plumNode != null) {
				m_bindingGroup = initDataBindings();
			}
		}
	}

}
