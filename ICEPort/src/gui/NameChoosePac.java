package gui;

import iceworld.ICEWorldView;

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

public class NameChoosePac extends JDialog {
	
	JTextField field;
	JButton send;
	public NameChoosePac() {
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
				LoginPage.app.view.notifyIncomingPac("me");

				ICEtizen target = LoginPage.app.view.loggedinUsers.get(field.getText());
				if(target == null){
					return;
				}
			
				System.out.println("target info: "+ target.getIPAddress()+ " "+ target.getuid());

				
				if(target.getIPAddress() == null || target.getuid() == 0)
					return;
				
				System.out.println("TARGET IP = "+(target.getIPAddress()));
				System.out.println("TARGET LISTENINGPORT = "+ (8000+target.getuid()));
				
					try {
						System.out.println("pinging target");
						Socket socketClient = new Socket(target.getIPAddress(), (8000+target.getuid()));
						LoginPage.app.view.notifyIncomingPac("me");
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				
				dispose();

			}
			
			
			
		});
		
	}
}
