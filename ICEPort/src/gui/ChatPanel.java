package gui;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import chat.ChatController;
@SuppressWarnings("serial")
public class ChatPanel extends JPanel{
	
	private static JTextField field;
	JButton sendButton;
	ButtonGroup buttonGroup;
	JRadioButton talk, yell;
	JPanel buttonPanel;
	
	public ChatPanel(){
		setLayout(new BorderLayout());
		
		field = new JTextField();
		sendButton = new JButton("Send");
		buttonGroup = new ButtonGroup();
		talk = new JRadioButton("Talk");
		yell = new JRadioButton("Yell");
		buttonGroup.add(talk);
		buttonGroup.add(yell);
		
		buttonPanel = new JPanel();
		buttonPanel.add(talk);
		buttonPanel.add(yell);
		add(buttonPanel, BorderLayout.WEST);
		add(field, BorderLayout.CENTER);
		add(sendButton, BorderLayout.EAST);
		
		sendButton.addActionListener(new ChatController());
	}

	public static String getMessage() {
		return field.getText();
	}

	public void setMessage(String message) {
		field.setText(message);
	}
	
}
