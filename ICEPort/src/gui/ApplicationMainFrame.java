package gui;

import iceworld.ICEWorldView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class ApplicationMainFrame extends JFrame{
	public static ICEWorldView view;
	public JPanel bottomPanel;
	public final Dimension mainFrameDimension = new Dimension(900,710);
	final static String VERSION = "0.1a";
	public ChatPanel chat;
	public JButton logoutButton, sendButton, soundButton, zoomInButton, zoomOutButton, setZoomButton; 
	
	
	
	public ApplicationMainFrame(){
		super("ICE Port - version "+VERSION+" by Cerntainly");
		
		// TODO set frame icon
		/*
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("icon.png")));
		*/
		
		initGUI();
		
	
		//setResizable(false);
		
		
		// start playing BGM
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
		chat = new ChatPanel();
		bottomPanel.add(chat);
		
		// Add Additional Controls Panel
		ControlsPanel control = new ControlsPanel();
		bottomPanel.add(control);
		
		add(bottomPanel, BorderLayout.SOUTH);
		pack();
	}
	
}
