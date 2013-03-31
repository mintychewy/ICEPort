package gui;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ControlsPanel extends JPanel {
	JTextField zoomLevelField;
	JButton zoomInButton, zoomOutButton, setZoomButton;
	public ControlsPanel(){
		zoomInButton = new JButton("Zoom In");
		zoomOutButton = new JButton("Zoom Out");
		zoomLevelField = new JTextField();
		setZoomButton = new JButton("Set Zoom");
		
		add(zoomInButton);
		add(zoomOutButton);
		//add(zoomLevelField);
		add(setZoomButton);
	}
}
