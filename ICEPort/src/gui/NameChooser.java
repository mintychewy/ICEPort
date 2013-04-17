package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objects.ICEtizen;

public class NameChooser extends JDialog {
	
	public JTextField field;
	JButton send;
	public NameChooser() {
		createGUI();
		setPreferredSize(new Dimension(300,200));
		pack();
	}
	private void createGUI(){
		setLayout(new GridLayout(2,1,5,5));

		field = new JTextField();
		send = new JButton("Send");
		
		add(field);
		add(send);
		
		send.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae){
				
				ICEtizen target = LoginPage.app.view.loggedinUsers.get(field.getText());
				if(target == null)
					return;
				if(target.getIPAddress() == null || target.getListeningPort() == 0)
					return;
				
				for(int i = 0; i < 1; i++){
					System.out.println("Going to create a new server");
					FServer server = new FServer();

					try {
						System.out.println("pinging target at: " + target.getIPAddress() +" PORT: "+target.getListeningPort());
						
						Socket socketClient = new Socket(target.getIPAddress(), target.getListeningPort());
					} catch (Exception e){
						System.out.println("Exception");
						dispose();
						return ;
					}
					server.sendFile();
					JOptionPane.showMessageDialog(new JPanel(),"Done sending!");
					dispose();
				}
			}
			
			
		});
		
	}
}
