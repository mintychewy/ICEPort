package gui;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import util.ImageLoader;

public class AboutWindow extends JDialog {
	
	private static final long serialVersionUID = 6992601172376070322L;

	public AboutWindow(){
		createGUI();
	}
	
	private void createGUI(){
		setPreferredSize(new Dimension(800,300));
		Image img = ImageLoader.loadImageFromLocal("images/about.png");
		
		JLabel lb = new JLabel();
		lb.setIcon(new ImageIcon(img));
		
		add(lb);
		setResizable(false);
		pack();
	}
}
