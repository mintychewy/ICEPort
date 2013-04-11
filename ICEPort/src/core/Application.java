package core;
 
 
 
 
import gui.ApplicationMainFrame;
import gui.LoginPage;
 
import java.awt.Dimension;
import java.awt.Toolkit;
 
 
public class Application {
  
	public static ApplicationMainFrame app;
	public static LoginPage login;
	
	/**
	 * Create an application frame at the centre of the screen
	 */
	public static void createAppAndShowGUI(){
		// Get screen dimension
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Create a new application frame
		//login = new ApplicationMainFrame();
		login = new LoginPage();
		
		// Positioning the frame
		int w = login.getSize().width;
		int h = login.getSize().height;
		int x = (screenDimension.width - w) / 2;
		int y = (screenDimension.height - h) / 2;
		int offset = 15;
		login.setLocation(x, y - offset);
		
		login.pack();
		login.setVisible(true);
	}
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAppAndShowGUI();
			}
		});
	}
}