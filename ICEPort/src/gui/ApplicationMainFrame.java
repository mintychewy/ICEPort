package gui;

import iceworld.ICEWorldView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Sound;

@SuppressWarnings("serial")
public class ApplicationMainFrame extends JFrame{
	public static ICEWorldView view;
	public JPanel bottomPanel;
	public final Dimension mainFrameDimension = new Dimension(900,750);
	final static String VERSION = "0.1a";
	
	public JButton logoutButton, sendButton, soundButton, zoomInButton, zoomOutButton, setZoomButton; 
	
	
	// sound
	public Sound sound;
	
	public ApplicationMainFrame(){
		super("ICE Port - version "+VERSION+" by Cerntainly");
		
		// TODO set frame icon
		/*
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("icon.png")));
		*/
		
		initGUI();
		
		// setResizable(true) for debugging purpose
		//setResizable(false);
		
		
		// start playing BGM
		
		// turn it off for now -- it's f*cking annoying 
		//sound = new Sound("music/grooving.wav");
		setVisible(true);
	}
	
	public void initGUI(){
		setPreferredSize(mainFrameDimension);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		// Creating and adding the MenuBar
		CustomMenuBar menu = new CustomMenuBar();
		setJMenuBar(menu);
		
		setLayout(new BorderLayout());
		
		// Add the main ICEWorld View 
		view = new ICEWorldView();
		add(view, BorderLayout.CENTER);
		

		// Bottom panel containing chat and controls panels
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(2,1,0,0));
		
		// Add the chat panel
		ChatPanel chat = new ChatPanel();
		bottomPanel.add(chat);
		
		// Add Additional Controls Panel
		ControlsPanel control = new ControlsPanel();
		bottomPanel.add(control);
		
		add(bottomPanel, BorderLayout.SOUTH);
		pack();
	}
	
}