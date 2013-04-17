package gui;

import iceworld.ICEWorldView;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

public class DirectorySetterWindow extends JDialog {
	
	JTextField field;
	JButton confirm;
	
	public DirectorySetterWindow(){
		createGUI();
		pack();
		
	}
	
	private void createGUI() {
		this.setLayout(new GridLayout(2,1,5,5));
		
		field = new JTextField(ICEWorldView.RECEIVED_FILES);
		confirm = new JButton("Set");
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				ICEWorldView.RECEIVED_FILES = field.getText();
				System.out.println("RECEIVED_FILES : "+ICEWorldView.RECEIVED_FILES);
				dispose();
			}
		});
		add(field);
		add(confirm);
		
	}
}
