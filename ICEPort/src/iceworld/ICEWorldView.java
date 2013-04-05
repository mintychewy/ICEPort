package iceworld;

import java.awt.Dimension;
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

import objects.Entity;
import objects.ICEtizen;
import objects.Map;
import objects.Minimap;
import util.ImageLoader;
import util.Patcher;
import util.Scaler;

public class ICEWorldView extends JPanel implements MouseListener,
		MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 5658988277615488303L;

	Minimap minimap;
	// The current representation of ICEWorld
	Map world;
	// The background tiles used for adaptive tile replacement
	Map map;

	ICEtizen me; 
	
	Panner panner;
	Patcher avatarPatcher;
	BufferedImage viewport;

	
	public static int deltaX = 0;
	public static int deltaY = 0;
	
	public final static Dimension ICEWORLD_VIEWPORT_SIZE = new Dimension(900,
			600);

	public ICEWorldView() {
		setPreferredSize(ICEWORLD_VIEWPORT_SIZE);
		
		// add listeners
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		setFocusable(true);
	
		// initialise world
		map = new Map();
		world = new Map();
		minimap = new Minimap();

		// patchers
		avatarPatcher = new Patcher(map.getImage(),Entity.AVATAR_SIZE.width,
				Entity.AVATAR_SIZE.height);
		
		// set initial camera view position for viewport
		populateWorld((Graphics2D)world.getImage().getGraphics());

		deltaX = States.currentPost.x;
		deltaY = States.currentPost.y;
		
		// panner
		panner = new Panner(world.getImage());
		updateWorld();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

		States.activeUserLastKnownPosition = States.currentPost;


		if (minimap.isMinimapClicked(e)) {
			// set destination

			// walk to destination
			walk();
		}

		if (States.activeUserIsWalking == false) {
			States.activeUserIsWalking = true;
			 walk();
		} else {
			/*
			 * 1. get timer 2. cancel task 3. schedule a new task
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
				
				// 1. see which direction the avatar has to walk
				
				// 2. increment of both X and Y positions until
				// the destination is reached
				
				if (States.currentPost.y == 89) {
					States.activeUserIsWalking = false;
					this.cancel();
				}

				if (States.currentPost.x < States.activeUserDestination.x
						&& States.currentPost.y < States.activeUserDestination.y) {
					 States.currentPost.y+= 1;
					States.currentPost.y += 1;
				} else if (States.currentPost.x < States.activeUserDestination.x
						&& States.currentPost.y == States.activeUserDestination.y) {
					 States.currentPost.x+= 1;

				} else {
					States.currentPost.y += 1;
				}

				System.out.println(States.activeUserLastKnownPosition
						.toString());
				States.activeUserLastKnownPosition = States.currentPost;
				System.out.println("UPDATED "
						+ States.activeUserLastKnownPosition.toString());

				updateWorld();
			}
		}, 0, myLong);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	BufferedImage redtile = ImageLoader
			.loadImageFromLocal("images/red-tile.png");

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

		me = new ICEtizen();

		// converts tileSpace to screenSpace coordinates
		Point pos = Scaler.toScreenSpace(States.currentPost);
		g2.drawImage(me.avatar, pos.x - Entity.AVATAR_OFFSET_X, pos.y
				- Entity.AVATAR_OFFSET_Y, this);

	}

	/**
	 * Puts empty tiles (from original map) in place of ICEtizens/Aliens This
	 * avoids having to render a new world every time the world updates (aka.
	 * Adaptive tile replacement)
	 */
	public void patchCitizens(Graphics2D g2) {

		Point screenspacePoint = Scaler.toMapPoint(new Point(
				States.activeUserLastKnownPosition.x,
				States.activeUserLastKnownPosition.y), deltaX, deltaY);

		// This is just a demonstration of only one ICEtizen
		// Real implementation requires iteration over a List
		BufferedImage patchUp = avatarPatcher.patch(screenspacePoint.x,screenspacePoint.y);

		g2.drawImage(patchUp, screenspacePoint.x, screenspacePoint.y, null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		panner.pan(e);

		updateWorld();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void updateWorld() {
		Graphics2D g2 = (Graphics2D) world.getImage().getGraphics();

		// Update ICEtizen/Alien positions
		populateWorld(g2);

		// Update MiniMap

		// Update chat
		// updateChat(g2);

		// Update yell
		// updateYell(g2);

		// Show update
		viewport = panner.getWorldViewport();

		updateMiniMap();

		repaint();
	}

	public void updateMiniMap() {
		minimap.updateMiniMap();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		minimap = new Minimap();

		g.drawImage(viewport, 0, 0, null);
		g.drawImage(minimap.getImage(), ICEWORLD_VIEWPORT_SIZE.width
				- Minimap.MINIMAP_SIZE.width - 10, 10, null);
	}

}
