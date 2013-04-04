package iceworld;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import objects.ICEtizen;
import objects.Map;
import objects.MiniMap;
import util.ImageLoader;
import util.Scaler;

public class ICEWorldView extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	MiniMap minimap;
	// The current representation of ICEWorld
	Map world;
	// The background tiles used for adaptive tile replacement
	Map map;
	// What users will see
	BufferedImage viewport;


	/*
	public void updateChat(Graphics2D g2, String message) {
		g2.setColor(Color.BLACK);
		if (chatMessage != null)
			g2.drawString(chatMessage, States.activeUserPosition.x,
					States.activeUserPosition.y);
	}
	*/

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {

		States.activeUserLastKnownPosition = States.activeUserPosition;

		if (States.activeUserIsWalking == false) {
			States.activeUserIsWalking = true;
			//walk();
		}else{
			/*
			 * 1. get timer
			 * 2. cancel task
			 * 3. schedule a new task
			 */
		}

		updateWorld();
	}

	Timer timer;

	public void walk() {
		long myLong = 100;

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// if(States.avatarPosX == States.destinationX){

				if (States.activeUserPosition.y == 89) {
					States.activeUserIsWalking = false;
					this.cancel();
				}

				if (States.activeUserPosition.x < States.activeUserDestination.x
						&& States.activeUserPosition.y < States.activeUserDestination.y) {
					// States.avatarPosX+= 1;
					States.activeUserPosition.y += 1;
				} else if (States.activeUserPosition.x < States.activeUserDestination.x
						&& States.activeUserPosition.y == States.activeUserDestination.y) {
					// States.avatarPosX+= 1;

				} else {
					States.activeUserPosition.y += 1;
				}

				System.out.println(States.activeUserLastKnownPosition.toString());
				States.activeUserLastKnownPosition = States.activeUserPosition;
				System.out.println("UPDATED "+ States.activeUserLastKnownPosition.toString());
				
				updateWorld();
			}
		}, 0, myLong);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	Point currentPoint;

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	BufferedImage redtile = ImageLoader.loadImageFromLocal("images/red-tile.png");
	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * This method draws logged-in ICEtizens and Aliens on the World Image
	 */
	public void populateWorld(Graphics2D g2) {

		patchCitizens(g2);

		ICEtizen icetizen = new ICEtizen();
		
		// converts tileSpace to screenSpace coordinates
		Point pos = Scaler.toScreenSpace(States.activeUserPosition);
		g2.drawImage(icetizen.avatar, pos.x - Constants.AVATAR_OFFSET_X, pos.y
				- Constants.AVATAR_OFFSET_Y, this);

	}

	/**
	 * Puts empty tiles (from original map) in place of ICEtizens/Aliens This
	 * avoids having to render a new world every time the world updates (aka.
	 * Adaptive tile replacement)
	 */
	public void patchCitizens(Graphics2D g2) {

		Point screenspacePoint = Scaler.toMapPoint(new Point(States.activeUserLastKnownPosition.x, States.activeUserLastKnownPosition.y));
		// This is just a demonstration of only one ICEtizen
		// Real implementation requires iteration over a List
		BufferedImage patchUp = Camera.getAvatarPatchImage(map.mapImage,
				screenspacePoint.x,
				screenspacePoint.y);
		g2.drawImage(patchUp, screenspacePoint.x,
				screenspacePoint.y, null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		Panning.pan(e);
		updateWorld();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public ICEWorldView() {
		setPreferredSize(Constants.ICEWORLD_VIEWPORT_SIZE);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		setFocusable(true);
		// initialise world
		map = new Map();
		world = new Map();
		minimap = new MiniMap();
		// set initial camera view position for viewport
		States.deltaX = States.activeUserPosition.x;
		States.deltaY = States.activeUserPosition.y;
		updateWorld();
	}

	public void updateWorld() {
		Graphics2D g2 = (Graphics2D) world.mapImage.getGraphics();

		// Update ICEtizen/Alien positions
		populateWorld(g2);
		
		// Update MiniMap
	
		// Update chat
		// updateChat(g2);
		
		// Update yell
		// updateYell(g2);

		// Show update
		viewport = Camera.getSubImage(world.mapImage, States.deltaX,
				States.deltaY);
		
	
		updateMiniMap();
		
		repaint();
	}
	
	public void updateMiniMap(){
		minimap.updateMiniMap();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		minimap = new MiniMap();
		
		g.drawImage(viewport, 0, 0, null);
		g.drawImage(minimap.mapImage,Constants.ICEWORLD_VIEWPORT_SIZE.width-Constants.MINIMAP_SIZE.width-10,10,null);
	}
	
}
