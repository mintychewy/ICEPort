package iceworld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ICEWorldView extends JPanel{
	
	public String chatMessage;
	
	final Dimension ICEWORLD_VIEW_SIZE = new Dimension(900,600);


	public ICEWorldView(){
		setPreferredSize(ICEWORLD_VIEW_SIZE);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		/*
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 900,600);
		g.setColor(Color.WHITE);
		if(chatMessage != null)
			g.drawString(chatMessage,100,100);
		*/
	
		Map map = new Map();
		g.drawImage(map.mapImage,0,0,this);
			
	}
}
