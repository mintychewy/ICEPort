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

import javax.swing.JPanel;

import objects.ICEtizen;
import objects.Inhabitant;
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

	Inhabitant me; 
	
	Panner panner;
	Patcher avatarPatcher;
	BufferedImage viewport;

	
	public static int deltaX = 0;
	public static int deltaY = 0;
	
	public final static Dimension ICEWORLD_VIEWPORT_SIZE = new Dimension(900,
			600);

	public ICEWorldView() {
		setPreferredSize(ICEWORLD_VIEWPORT_SIZE);
		
		
		// create an instance of myself :P
		me = new Inhabitant();
		
		
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
		avatarPatcher = new Patcher(map.getImage(),ICEtizen.AVATAR_SIZE.width,
				ICEtizen.AVATAR_SIZE.height);
		
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

		// converts mouseclick position into a tile position
		Point destinationTile = Scaler.toTileSpaceFromViewport(e.getPoint());
		
		System.out.println("Heading to: "+destinationTile.toString());
		
		// check whether if it is a valid destination
		if(!world.isFallingIntoTartarus(destinationTile)){
			// set destination
			States.activeUserDestination = destinationTile;
			// walk to the destination
			walk();
		}else{
			System.out.println("Invalid destination point");
		}

	}

	Timer timer;

	public void walk() {
		long myLong = 100;
		timer = new Timer();
		timer.schedule(new WalkingTask(), 0, myLong);
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
	 * Renders the World image with the latest elements
	 */
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


	/**
	 * This method draws logged-in ICEtizens and Aliens on the World Image
	 */
	public void populateWorld(Graphics2D g2) {
		
		// remove all users from the map 
		patchCitizens(g2);



		// converts tileSpace to screenSpace coordinates
		Point pos = Scaler.toScreenSpace(States.currentPost);
		
		g2.drawImage(me.avatar, pos.x - ICEtizen.AVATAR_OFFSET_X, pos.y
				- ICEtizen.AVATAR_OFFSET_Y, this);
		
	
	}

	/**
	 * Puts empty tiles (from original map) in place of ICEtizens/Aliens This
	 * avoids having to render a new world every time the world updates (aka.
	 * Adaptive tile replacement)
	 */
	public void patchCitizens(Graphics2D g2) {

		Point draw = Scaler.toScreenSpace(States.activeUserLastKnownPosition);
		
		BufferedImage patchImage = avatarPatcher.getPatchImage(draw.x - ICEtizen.AVATAR_OFFSET_X, draw.y - ICEtizen.AVATAR_OFFSET_Y);

		g2.drawImage(patchImage, draw.x - ICEtizen.AVATAR_OFFSET_X, draw.y
				- ICEtizen.AVATAR_OFFSET_Y, this);
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
