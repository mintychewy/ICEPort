package iceworld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import objects.ICEtizen;
import objects.Map;
import util.Scaler;

public class ICEWorldView extends JPanel implements MouseListener, MouseMotionListener{
	
	final Dimension ICEWORLD_VIEW_SIZE = new Dimension(900,600);
	public double scale_factor = 1;
	public String chatMessage;
	public int avatarPosX;
	public int avatarPosY;
	public int deltaX = 0;
	public int deltaY = 0;
	Map map;
	BufferedImage usersView;

	public ICEWorldView(){
		setPreferredSize(ICEWORLD_VIEW_SIZE);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		map = new Map();
	}	
	
	public void updateScaleFactor(double scale_factor){
		this.scale_factor = scale_factor;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
	
		Graphics2D g2 = (Graphics2D) g;
		//BufferedImage scaledMap = Scaler.scaleBufferedImage(map.mapImage,1);
		//g.drawImage(scaledMap,0,0,null);
		
		g.drawImage(usersView,0,0,null);
		
		ICEtizen icetizen = new ICEtizen();
		g.drawImage(icetizen.avatar, avatarPosX, avatarPosY, this);
		
		g.setColor(Color.BLACK);
		if(chatMessage != null)
			g.drawString(chatMessage,avatarPosX,avatarPosY);
	
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		avatarPosX = e.getPoint().x-47;
		avatarPosY = e.getPoint().y-60;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		deltaX += 5;
		
		usersView = Camera.getSubImage(map.mapImage,deltaX,deltaY);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	


}
