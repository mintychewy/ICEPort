package gui;

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
	public ControlsPanel(){

		sendFileButton = new JButton("Send File");
		add(sendFileButton);
		
		toggleWeather = new JCheckBox("Toggle Weather");
		dragZoomButton  = new JButton("Zoom-to-Area");
		
		zoomInButton = new JButton("Zoom In");
		zoomOutButton = new JButton("Zoom Out");
		zoomLevelField = new JTextField();
		setZoomButton = new JButton("Specify Zoom");
		soundCtrlButton = new JButton("Sound Control");
		logoutButton = new JButton("Logout");
		add(toggleWeather);
		toggleWeather.setSelected(true);
		add(zoomInButton);
		add(zoomOutButton);
		//add(zoomLevelField);
		add(setZoomButton);
		add(soundCtrlButton);
		add(logoutButton);
		addListeners();
		add(dragZoomButton);
	}
	
	public void addListeners(){
		toggleWeather.addItemListener(new CheckBoxListener());
		sendFileButton.addActionListener(new FTPButtonListener());
		dragZoomButton.addActionListener(new ControlsPaneListener());
		sendFileButton.addActionListener(new FTPButtonListener());
		//soundCtrlButton.addActionListener(new SoundButtonListener());
		setZoomButton.addActionListener(new ControlsPaneListener());
		logoutButton.addActionListener(new ControlsPaneListener());
		zoomInButton.addActionListener(new ControlsPaneListener());
		zoomOutButton.addActionListener(new ControlsPaneListener());
	}
}
