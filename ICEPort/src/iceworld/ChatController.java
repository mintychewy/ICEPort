package iceworld;

import gui.LoginPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonModel;

public class ChatController implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonModel btn = LoginPage.app.chat.buttonGroup.getSelection();
		
		System.out.println(""+btn.getActionCommand());
		String msg = LoginPage.app.chat.getMessage();
		
		
		// TODO report talking to the server
		
		// yell case
		if(btn.getActionCommand().equals("yell")){
			if(msg.length() > 10){
				msg = msg.substring(0,10);
			}
			
			msg = msg.toUpperCase();

			ICEWorldView.instantYellMessage = msg;
			LoginPage.immigration.yell(msg);
			LoginPage.app.view.updateWorld();

		}else if(btn.getActionCommand().equals("talk")){
			if(msg.length() > 100){
				msg = msg.substring(0,100);
			}
			
			ICEWorldView.instantTalkMessage = msg;
			LoginPage.immigration.talk(msg);
			LoginPage.app.view.updateWorld();

		}
		
		LoginPage.app.view.requestFocus(true);

	}

}
