package iceworld;

import gui.LoginPage;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import javax.swing.ButtonModel;

public class ChatController implements ActionListener {
	
	public static Font chatFont = new Font("Arial", Font.PLAIN, (int)(100*ICEWorldView.zoom_factor));

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
			new Timer().schedule(new SelfChat(), 5000);

			LoginPage.app.view.updateWorld();

		}
		
		LoginPage.app.view.requestFocus(true);

	}

}
