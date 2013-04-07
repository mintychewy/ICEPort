package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

public class MenuBarListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton source = (AbstractButton) e.getSource();
		if(source.getText().equals("Quit")){
			// exit program
			System.exit(0);
		}
		else if(source.getText().equals("About")){
			Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
			
			// create a new modal dialog
			AboutWindow aboutDialog = new AboutWindow();
			aboutDialog.setModal(true);
			aboutDialog.setAlwaysOnTop(true);
			aboutDialog.setLocation(screenDimension.width/2-400,screenDimension.height/2-150);
			aboutDialog.setVisible(true);
		}
		else if(source.getText().equals("User Guide")){
			System.out.println("User Guide");
		}
		else{
			System.out.println("do nothing");
		}
		
	}
	
}
