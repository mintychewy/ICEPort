package gui;

import iceworld.ICEWorldView;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ApplicationMainFrame extends JFrame{
	public static ICEWorldView view;
	public final Dimension mainFrameDimension = new Dimension(900,750);
	final static String VERSION = "0.1a";
	
	public JButton logoutButton, sendButton, soundButton, zoomInButton, zoomOutButton, setZoomButton; 
	
	public ApplicationMainFrame(){
		super("ICE Port - version "+VERSION+" by Cerntainly");
		
		// TODO set frame icon
		/*
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("icon.png")));
		*/
		
		initGUI();
		setVisible(true);
	}
	
	public void initGUI(){
		setPreferredSize(mainFrameDimension);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		// Creating and adding the MenuBar
		CustomMenuBar menu = new CustomMenuBar();
		setJMenuBar(menu);
		
		setLayout(new BorderLayout());
		
		view = new ICEWorldView();
		
		add(view, BorderLayout.CENTER);
		
		ChatPanel chat = new ChatPanel();
		
		add(chat, BorderLayout.SOUTH);
		
		pack();
	}
	
}
