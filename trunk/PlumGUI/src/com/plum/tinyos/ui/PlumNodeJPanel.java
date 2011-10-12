package com.plum.tinyos.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;

import com.plum.tinyos.model.PlumNode;

public class PlumNodeJPanel extends JPanel {

	private BindingGroup m_bindingGroup;
	private com.plum.tinyos.model.PlumNode plumNode = new com.plum.tinyos.model.PlumNode();
	private JTextField idJTextField;
	private JTextField unixTimeJTextField;
	private JTextField batteryLeftJTextField;
	private JProgressBar spaceLeftJProgressBar;

	public PlumNodeJPanel(com.plum.tinyos.model.PlumNode newPlumNode) {
		this();
		setPlumNode(newPlumNode);
	}

	public PlumNodeJPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0E-4 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0E-4 };
		setLayout(gridBagLayout);

		JLabel idLabel = new JLabel("Id:");
		GridBagConstraints labelGbc_0 = new GridBagConstraints();
		labelGbc_0.insets = new Insets(5, 5, 5, 5);
		labelGbc_0.gridx = 0;
		labelGbc_0.gridy = 0;
		add(idLabel, labelGbc_0);

		idJTextField = new JTextField();
		GridBagConstraints componentGbc_0 = new GridBagConstraints();
		componentGbc_0.insets = new Insets(5, 0, 5, 5);
		componentGbc_0.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_0.gridx = 1;
		componentGbc_0.gridy = 0;
		idJTextField.setEditable(false);
		add(idJTextField, componentGbc_0);

		JLabel unixTimeLabel = new JLabel("UnixTime:");
		GridBagConstraints labelGbc_1 = new GridBagConstraints();
		labelGbc_1.insets = new Insets(5, 5, 5, 5);
		labelGbc_1.gridx = 0;
		labelGbc_1.gridy = 1;
		add(unixTimeLabel, labelGbc_1);

		unixTimeJTextField = new JTextField();
		GridBagConstraints componentGbc_1 = new GridBagConstraints();
		componentGbc_1.insets = new Insets(5, 0, 5, 5);
		componentGbc_1.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_1.gridx = 1;
		componentGbc_1.gridy = 1;
		unixTimeJTextField.setEditable(false);
		add(unixTimeJTextField, componentGbc_1);

		JLabel batteryLeftLabel = new JLabel("BatteryLeft:");
		GridBagConstraints labelGbc_2 = new GridBagConstraints();
		labelGbc_2.insets = new Insets(5, 5, 5, 5);
		labelGbc_2.gridx = 0;
		labelGbc_2.gridy = 2;
		add(batteryLeftLabel, labelGbc_2);

		batteryLeftJTextField = new JTextField();
		GridBagConstraints componentGbc_2 = new GridBagConstraints();
		componentGbc_2.insets = new Insets(5, 0, 5, 5);
		componentGbc_2.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_2.gridx = 1;
		componentGbc_2.gridy = 2;
		batteryLeftJTextField.setEditable(false);
		add(batteryLeftJTextField, componentGbc_2);

		JLabel spaceLeftLabel = new JLabel("SpaceLeft:");
		GridBagConstraints labelGbc_3 = new GridBagConstraints();
		labelGbc_3.insets = new Insets(5, 5, 5, 5);
		labelGbc_3.gridx = 0;
		labelGbc_3.gridy = 3;
		add(spaceLeftLabel, labelGbc_3);

		spaceLeftJProgressBar = new JProgressBar();
		GridBagConstraints componentGbc_3 = new GridBagConstraints();
		componentGbc_3.insets = new Insets(5, 0, 5, 5);
		componentGbc_3.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_3.gridx = 1;
		componentGbc_3.gridy = 3;
		add(spaceLeftJProgressBar, componentGbc_3);

		if (plumNode != null) {
			m_bindingGroup = initDataBindings();
		}
	}

	protected BindingGroup initDataBindings() {
		BeanProperty<com.plum.tinyos.model.PlumNode, java.lang.Integer> idProperty = BeanProperty
				.create("id");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty = BeanProperty
				.create("text");
		AutoBinding<com.plum.tinyos.model.PlumNode, java.lang.Integer, javax.swing.JTextField, java.lang.String> autoBinding = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, plumNode,
						idProperty, idJTextField, textProperty);
		autoBinding.bind();
		//
		BeanProperty<com.plum.tinyos.model.PlumNode, java.lang.String> unixTimeProperty = BeanProperty
				.create("unixTime");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_1 = BeanProperty
				.create("text");
		AutoBinding<com.plum.tinyos.model.PlumNode, java.lang.String, javax.swing.JTextField, java.lang.String> autoBinding_1 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, plumNode,
						unixTimeProperty, unixTimeJTextField, textProperty_1);
		autoBinding_1.bind();
		//
		BeanProperty<com.plum.tinyos.model.PlumNode, java.lang.Integer> batteryLeftProperty = BeanProperty
				.create("batteryLeft");
		BeanProperty<javax.swing.JTextField, java.lang.String> textProperty_2 = BeanProperty
				.create("text");
		AutoBinding<com.plum.tinyos.model.PlumNode, java.lang.Integer, javax.swing.JTextField, java.lang.String> autoBinding_2 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, plumNode,
						batteryLeftProperty, batteryLeftJTextField,
						textProperty_2);
		autoBinding_2.bind();
		//
		BeanProperty<com.plum.tinyos.model.PlumNode, java.lang.Integer> spaceLeftProperty = BeanProperty
				.create("spaceLeft");
		BeanProperty<javax.swing.JProgressBar, java.lang.Integer> valueProperty = BeanProperty
				.create("value");
		AutoBinding<com.plum.tinyos.model.PlumNode, java.lang.Integer, javax.swing.JProgressBar, java.lang.Integer> autoBinding_3 = Bindings
				.createAutoBinding(AutoBinding.UpdateStrategy.READ, plumNode,
						spaceLeftProperty, spaceLeftJProgressBar, valueProperty);
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
