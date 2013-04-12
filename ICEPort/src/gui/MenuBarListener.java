package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JDialog;

import core.Application;


public class MenuBarListener implements ActionListener {
	public static QuitConfirmationDialog confirmDialog;
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton source = (AbstractButton) e.getSource();
		if(source.getText().equals("Quit")){
			
			confirmDialog = new QuitConfirmationDialog();
			confirmDialog.setVisible(true);
			
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
		}else if(source.getText().equals("Customise Avatar")){
			Customisation cus = new Customisation();
			cus.setVisible(true);
		}else if(source.getText().equals("Refresh Rate")){
			RefreshRateWindow rrwindow = new RefreshRateWindow();
			rrwindow.setLocation(Application.screenDimension.width/2,Application.screenDimension.height/2);
			rrwindow.setVisible(true);
		}
		else{
			System.out.println("do nothing");
		}
		
	}
	
}
