package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FTPButtonListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		
		NameChooser nc = new NameChooser();
		nc.setLocation(700,400);

		nc.setVisible(true);
	
	}

}


