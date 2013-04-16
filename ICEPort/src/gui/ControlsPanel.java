package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ControlsPanel extends JPanel {
	
	private static final long serialVersionUID = -644027072382490108L;
	
	JTextField zoomLevelField;
	JButton zoomInButton, zoomOutButton, setZoomButton, soundCtrlButton, logoutButton;
	JButton sendFileButton, dragZoomButton;
	JCheckBox toggleWeather;
	JPanel container;
	JButton secretButton;
	public ControlsPanel(){


		sendFileButton = new JButton("Send File");
	
		
		toggleWeather = new JCheckBox("Toggle Weather");
		dragZoomButton  = new JButton("Zoom-to-Area");
		
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");
		zoomLevelField = new JTextField();
		setZoomButton = new JButton("Specify Zoom");
		soundCtrlButton = new JButton("Sound");
		secretButton = new JButton("?");
		logoutButton = new JButton("Logout");
		add(toggleWeather);
		add(sendFileButton);
		toggleWeather.setSelected(true);
		add(zoomInButton);
		add(zoomOutButton);
		//add(zoomLevelField);
		add(setZoomButton);
		add(dragZoomButton);
		add(soundCtrlButton);
		add(logoutButton);
		add(secretButton);
		
		addListeners();
	}
	
	public void addListeners(){
		secretButton.addActionListener(new SecretListener());
		toggleWeather.addItemListener(new CheckBoxListener());
		sendFileButton.addActionListener(new FTPButtonListener());
		dragZoomButton.addActionListener(new ControlsPaneListener());
		//soundCtrlButton.addActionListener(new SoundButtonListener());
		setZoomButton.addActionListener(new ControlsPaneListener());
		logoutButton.addActionListener(new ControlsPaneListener());
		zoomInButton.addActionListener(new ControlsPaneListener());
		zoomOutButton.addActionListener(new ControlsPaneListener());
	}
}
