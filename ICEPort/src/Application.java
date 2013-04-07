



import gui.ApplicationMainFrame;

import java.awt.Dimension;
import java.awt.Toolkit;


public class Application {
	public static ApplicationMainFrame app;
	/**
	 * Create an application frame at the centre of the screen
	 */
	public static void createAppAndShowGUI(){
		// Get screen dimension
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Create a new application frame
		app = new ApplicationMainFrame();
		
		// Positioning the frame
		int w = app.getSize().width;
		int h = app.getSize().height;
		int x = (screenDimension.width - w) / 2;
		int y = (screenDimension.height - h) / 2;
		int offset = 15;
		app.setLocation(x, y - offset);
		
		app.pack();
		app.setVisible(true);
	}
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAppAndShowGUI();
			}
		});
	}
}
