package iceworld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ICEWorldView extends JPanel{
	public String chatMessage;
	final Dimension ICEWORLD_VIEW_SIZE = new Dimension(900,600);
	public ICEWorldView(){
		setPreferredSize(ICEWORLD_VIEW_SIZE);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 900,600);
		g.setColor(Color.WHITE);
		if(chatMessage != null)
			g.drawString(chatMessage,10,10);
	}
}
