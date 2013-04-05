package iceworld;

import gui.ApplicationMainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatController implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Get the message string from the chatbox
		//ApplicationMainFrame.view.chatMessage = ChatPanel.getMessage();
		ApplicationMainFrame.view.repaint();
	}

}
