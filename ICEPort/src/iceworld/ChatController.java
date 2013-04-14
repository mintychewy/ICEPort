package iceworld;

import gui.LoginPage;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;

import javax.swing.ButtonModel;

import objects.Minimap;

public class ChatController implements ActionListener {
	
	public static Font chatFont = new Font("Arial", Font.PLAIN, (int)(100*ICEWorldView.zoom_factor));

	@Override
	public void actionPerformed(ActionEvent e) {
		ButtonModel btn = LoginPage.app.chat.buttonGroup.getSelection();
		System.out.println(""+btn.getActionCommand());
		String msg = LoginPage.app.chat.getMessage();
		LoginPage.app.chat.clearTextField();

		
		// TODO report talking to the server
		
		// yell case
		if(btn.getActionCommand().equals("yell")){
			
			if(msg == null || msg.length() <= 0){
				return;
			}
			if(msg.length() > 10){
				msg = msg.substring(0,10);
			}
			
			msg = msg.toUpperCase();

			ICEWorldView.instantYellMessage = msg;
			LoginPage.immigration.yell(msg);
			
			BufferedImage bf = new BufferedImage(900,600,BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2bf = bf.createGraphics();

			g2bf.setColor(Minimap.BLACK_WITH_50_PERCENT_ALPHA);
			g2bf.fillRect(0, 300, 900, 300);


			Font font = new Font ("Arial", Font.PLAIN, (msg.length() <= 5)?200:100);
			g2bf.setFont(font);
			g2bf.setColor(YellingTaskOthers.SKY_BLUE);
			g2bf.drawString(msg, 10, (msg.length() <=5)?530:500);							
			
			LoginPage.app.view.yellImageList.add(bf);
			new Timer().schedule(new YellingTaskOthers(bf), 5000, 1);
			
			LoginPage.app.view.updateWorld();

		}else if(btn.getActionCommand().equals("talk")){
			if(msg == null || msg.length() <= 0){
				return;
			}
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
