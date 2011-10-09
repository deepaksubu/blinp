package com.plum.tinyos.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import com.plum.tinyos.model.PlumNode;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import javax.swing.JTextField;

public class PlumNodeJPanel extends JPanel {

	private BindingGroup m_bindingGroup;
	private PlumNode plumNode;
	private JTextField textField1;
	private JProgressBar spaceLeftJProgressBar;
	private JTextField textField;

	public PlumNodeJPanel(PlumNode newPlumNode) {
		this();
		setPlumNode(newPlumNode);
	}

	public PlumNodeJPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0E-4 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0E-4 };
		setLayout(gridBagLayout);

		JLabel labelLeftId = new JLabel("Name");
		GridBagConstraints gbc_labelLeftId = new GridBagConstraints();
		gbc_labelLeftId.insets = new Insets(0, 0, 5, 5);
		gbc_labelLeftId.gridx = 0;
		gbc_labelLeftId.gridy = 0;
		add(labelLeftId, gbc_labelLeftId);

		textField = new JTextField();
		textField.setEditable(false);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(textField, gbc_textField);
		textField.setColumns(10);

		JLabel batteryLeftLabel = new JLabel("BatteryLeft:");
		GridBagConstraints labelGbc_1 = new GridBagConstraints();
		labelGbc_1.insets = new Insets(5, 5, 5, 5);
		labelGbc_1.gridx = 0;
		labelGbc_1.gridy = 1;
		add(batteryLeftLabel, labelGbc_1);

/*		batteryLeftJProgressBar = new JProgressBar();
		GridBagConstraints componentGbc_1 = new GridBagConstraints();
		componentGbc_1.insets = new Insets(5, 0, 5, 0);
		componentGbc_1.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_1.gridx = 1;
		componentGbc_1.gridy = 1;
		add(batteryLeftJProgressBar, componentGbc_1);
*/
		textField1 = new JTextField();
		textField1.setEditable(false);
		GridBagConstraints gbc_textField1 = new GridBagConstraints();
		gbc_textField.insets = new Insets(5, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		add(textField1, gbc_textField1);
		textField1.setColumns(10);

		JLabel spaceLeftLabel = new JLabel("SpaceLeft:");
		GridBagConstraints labelGbc_2 = new GridBagConstraints();
		labelGbc_2.insets = new Insets(5, 5, 5, 5);
		labelGbc_2.gridx = 0;
		labelGbc_2.gridy = 2;
		add(spaceLeftLabel, labelGbc_2);

		spaceLeftJProgressBar = new JProgressBar();
		GridBagConstraints componentGbc_2 = new GridBagConstraints();
		componentGbc_2.insets = new Insets(5, 0, 5, 0);
		componentGbc_2.fill = GridBagConstraints.HORIZONTAL;
		componentGbc_2.gridx = 1;
		componentGbc_2.gridy = 2;
		add(spaceLeftJProgressBar, componentGbc_2);

		if (plumNode != null) {
			m_bindingGroup = initDataBindings();
		}

		JButton download = new JButton();
		download.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//Download data code here

			}

		});

	}

	public PlumNode getPlumNode() {
		return plumNode;
	}

	public void setPlumNode(PlumNode newPlumNode) {
		setPlumNode(newPlumNode, true);
	}

	public void setPlumNode(PlumNode newPlumNode,
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

	protected BindingGroup initDataBindings() {
		BeanProperty<PlumNode, Integer> batteryLeftProperty = BeanProperty
				.create("batteryLeft");
		BeanProperty<JTextField, Integer> valueProperty_1 = BeanProperty
				.create("value");
		AutoBinding<PlumNode, Integer, JTextField, Integer> autoBinding_1 = Bindings
				.createAutoBinding(UpdateStrategy.READ, plumNode,
						batteryLeftProperty, textField1,
						valueProperty_1);
		autoBinding_1.bind();
		//
		BeanProperty<PlumNode, Integer> spaceLeftProperty = BeanProperty
				.create("spaceLeft");
		BeanProperty<JProgressBar, Integer> valueProperty_2 = BeanProperty
				.create("value");
		AutoBinding<PlumNode, Integer, JProgressBar, Integer> autoBinding_2 = Bindings
				.createAutoBinding(UpdateStrategy.READ, plumNode,
						spaceLeftProperty, spaceLeftJProgressBar,
						valueProperty_2);
		autoBinding_2.bind();
		//
		BeanProperty<PlumNode, Integer> plumNodeBeanProperty = BeanProperty
				.create("id");
		BeanProperty<JTextField, String> jTextFieldBeanProperty = BeanProperty
				.create("text");
		AutoBinding<PlumNode, Integer, JTextField, String> autoBinding = Bindings
				.createAutoBinding(UpdateStrategy.READ, plumNode,
						plumNodeBeanProperty, textField,
						jTextFieldBeanProperty, "auto_binding_id");
		autoBinding.bind();
		//
		BindingGroup bindingGroup = new BindingGroup();
		//
		bindingGroup.addBinding(autoBinding_1);
		bindingGroup.addBinding(autoBinding_2);
		bindingGroup.addBinding(autoBinding);
		return bindingGroup;
	}
}
