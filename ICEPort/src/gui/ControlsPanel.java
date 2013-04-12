package gui;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ControlsPanel extends JPanel {
	
	private static final long serialVersionUID = -644027072382490108L;
	
	JTextField zoomLevelField;
	JButton zoomInButton, zoomOutButton, setZoomButton, soundCtrlButton, logoutButton;
	JButton sendFileButton;
	public ControlsPanel(){

		sendFileButton = new JButton("Send File");
		add(sendFileButton);
		
		
		
		zoomInButton = new JButton("Zoom In");
		zoomOutButton = new JButton("Zoom Out");
		zoomLevelField = new JTextField();
		setZoomButton = new JButton("Specify Zoom");
		soundCtrlButton = new JButton("Sound Control");
		logoutButton = new JButton("Logout");
		add(zoomInButton);
		add(zoomOutButton);
		//add(zoomLevelField);
		add(setZoomButton);
		add(soundCtrlButton);
		add(logoutButton);
		addListeners();
	}
	
	public void addListeners(){
		
		sendFileButton.addActionListener(new FTPButtonListener());
		
		setZoomButton.addActionListener(new ControlsPaneListener());
		logoutButton.addActionListener(new ControlsPaneListener());
		zoomInButton.addActionListener(new ControlsPaneListener());
		zoomOutButton.addActionListener(new ControlsPaneListener());
	}
}
