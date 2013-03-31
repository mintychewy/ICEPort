package chat;

import gui.ChatPanel;
import gui.ApplicationMainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatController implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	
		ApplicationMainFrame.view.chatMessage = ChatPanel.getMessage();
		ApplicationMainFrame.view.repaint();
	}

}
