package iceworld;
import gui.LoginPage;
import gui.SoundControllerWindow;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.Timer;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;

import objects.Minimap;
import sfx.Sound;

public class ChatController implements ActionListener,ItemListener {
	public Sound sound;
	SoundControllerWindow control=new SoundControllerWindow();
	public boolean mute;
	public static Font chatFont = new Font("Arial", Font.PLAIN,
			(int) (100 * ICEWorldView.zoom_factor));

	
	

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
	
		AbstractButton btn = (AbstractButton) e.getSource();
		mute = btn.isSelected();
		
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.print(mute);
		ButtonModel btn = LoginPage.app.chat.buttonGroup.getSelection();
		System.out.println("" + btn.getActionCommand());
		String msg = LoginPage.app.chat.getMessage();
		LoginPage.app.chat.clearTextField();

		// TODO report talking to the server

		// yell case
		if (btn.getActionCommand().equals("yell")) {

			

			if (msg == null || msg.length() <= 0) {
				return;
			}
			if (msg.length() > 10) {
				msg = msg.substring(0, 10);
			}

			msg = msg.toUpperCase();

			BufferedImage bf = new BufferedImage(900, 300,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2bf = bf.createGraphics();

			g2bf.setColor(Minimap.BLACK_WITH_50_PERCENT_ALPHA);
			g2bf.fillRect(0, 0, 900, 300);

			Font font = new Font("Arial", Font.PLAIN, (msg.length() <= 5) ? 200
					: 100);
			g2bf.setFont(font);
			g2bf.setColor(YellingTaskOthers.SKY_BLUE);
			g2bf.drawString(msg, 10, (msg.length() <= 5) ? 230 : 200);

			LoginPage.app.view.yellImageList.add(bf);
			new Timer().schedule(new YellingTaskOthers(bf), 5000, 1);
			LoginPage.immigration.yell(msg);
			LoginPage.app.view.updateWorld();
			if(mute==true){
				
			}
			else if(mute==false)
			sound=new Sound("music/scream.wav");	
			} 
		else if (btn.getActionCommand().equals("talk")) {
			
			
			
			
			if (msg == null || msg.length() <= 0) {
				return;
			}
			if (msg.length() > 100) {
				msg = msg.substring(0, 100);
			}


			int lineAmount = (int) Math.ceil(msg.length()/25.0);
			System.out.println("lineAmount = "+lineAmount);
			
			
			BufferedImage bf = new BufferedImage(310, lineAmount*25,
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2bf = bf.createGraphics();
			g2bf.setFont(TalkingTaskOthers.TALKING_FONT);
			
			// draw a chat bubble
			g2bf.setColor(Minimap.BLACK_WITH_50_PERCENT_ALPHA);
			g2bf.fillRect(0, 0, 310, lineAmount*25);
			
			g2bf.setColor(YellingTaskOthers.SKY_BLUE);
			
			if(lineAmount == 1){
				// already formatted
				g2bf.drawString(msg, 5, 22);

			}else if(lineAmount ==2){
				g2bf.drawString(msg.substring(0,25), 5, 24*1);
				g2bf.drawString(msg.substring(25), 5, 24*2);
				
			
			}else if(lineAmount ==3){
				g2bf.drawString(msg.substring(0,25), 5, 24*1);
				g2bf.drawString(msg.substring(25,50), 5, 24*2);
				g2bf.drawString(msg.substring(50), 5, 24*3);

			}else if(lineAmount ==4){
				g2bf.drawString(msg.substring(0,25), 5, 24*1);
				g2bf.drawString(msg.substring(25,50), 5, 24*2);
				g2bf.drawString(msg.substring(50,75), 5, 24*3);
				g2bf.drawString(msg.substring(75), 5, 24*4);

			}

			TalkObject to = new TalkObject(bf,LoginPage.me.getUsername());
			LoginPage.app.view.talkImageList.add(to);
			new Timer().schedule(new TalkingTaskOthers(to), ICEWorldView.TALK_VISIBLE_DURATION, 1);

			LoginPage.immigration.talk(msg);

			LoginPage.app.view.updateWorld();
			
		//	sound=new Sound("music/burp.wav");
			
		
		}

		LoginPage.app.view.requestFocus(true);

	}


}
