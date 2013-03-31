package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
public class ApplicationMainFrame extends JFrame{
	public final Dimension mainFrameDimension = new Dimension(1050,850);
	final static String VERSION = "0.1a";
	
	JLayeredPane desktop;
	
	
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
		CustomMenuBar menu = new CustomMenuBar();
		setJMenuBar(menu);
		setLayout(new BorderLayout());
		pack();
	}
	
}
