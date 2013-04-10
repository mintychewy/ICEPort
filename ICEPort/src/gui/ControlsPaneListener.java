package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import core.Application;

public class ControlsPaneListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton btn = (AbstractButton) e.getSource();
		if(btn.getText().equals("Logout")){
			if (LoginPage.immigration.logout()){
				System.out.println("Logout OK");
				LoginPage.app.dispose();
				Application.login.setVisible(true);
			}
		}
	}
	
}
