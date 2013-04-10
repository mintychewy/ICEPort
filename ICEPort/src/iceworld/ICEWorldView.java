package iceworld;

import gui.LoginPage;

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
import java.util.HashMap;
import java.util.Timer;

import javax.swing.JPanel;

import objects.ICEtizen;
import objects.Inhabitant;
import objects.Minimap;
import objects.World;
import util.ImageLoader;
import util.Patcher;
import util.Scaler;
import util.WorldStatesFetcher;

public class ICEWorldView extends JPanel implements MouseListener,
		MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 5658988277615488303L;
	
	// zooming factor -- default being 1
	public static double zoom_factor = 1;

	// responsible for fetching everything from the server
	WorldStatesFetcher fetcher;

	// HashMap of logged-in ICEtizens (String = username is the key)
	HashMap<String, ICEtizen> loggedinUsers;
	// Controller user
	
	Minimap minimap;
	// The current representation of ICEWorld
	World world;
	// The background tiles used for adaptive tile replacement
	World map;
	
	Panner panner;
	Patcher avatarPatcher;
	BufferedImage viewport;

	
	
	///
	BufferedImage alienImage = ImageLoader.loadImageFromLocal("images/alien.png");
	///
	
	public static int deltaX = 0;
	public static int deltaY = 0;
	
	public final static Dimension ICEWORLD_VIEWPORT_SIZE = new Dimension(900,
			600);

	public ICEWorldView() {
		setPreferredSize(ICEWORLD_VIEWPORT_SIZE);
		
		// fetcher
		fetcher = new WorldStatesFetcher();
		fetcher.updateWorldStates();
		
		loggedinUsers = fetcher.getLoggedinUserMap();
		
		// alien
		if(LoginPage.me.getType() == 0 ){
			
			// need to find and remove self from the loggedinUsers list
			
			
			LoginPage.me.setCurrentPosition(new Point(0,0));
			LoginPage.immigration.walk(0, 0);
		}
		
		// inhabitant
		if(LoginPage.me.getType()!=0){
			// give the controller a dedicated instance
			LoginPage.me = loggedinUsers.get(LoginPage.me.getUsername());
			
			// remove from the list
			loggedinUsers.remove(LoginPage.me.getUsername());
			
			// if there's no position yet, assume 0,0
			if(LoginPage.me.getCurrentPosition() == null){

				LoginPage.immigration.walk(0, 0);
				// avoid having to re-fetch the states
				LoginPage.me.setCurrentPosition(new Point(0,0));
	
			}
		}

		
		// add listeners
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		setFocusable(true);
	
		// initialise world
		map = new World();
		world = new World();
		minimap = new Minimap();

		// patchers
		avatarPatcher = new Patcher(map.getImage(),ICEtizen.AVATAR_SIZE.width,
				ICEtizen.AVATAR_SIZE.height);
		
		// set initial camera view position for viewport
		populateWorld((Graphics2D)world.getImage().getGraphics());

		deltaX = 0;
		deltaY = 0;
		
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
			//States.activeUserDestination = destinationTile;
			// walk to the destination
			walk();
		}else{
			System.out.println("Invalid destination point");
		}

	}
	
	
	public void zoomChanged(){
		map = null;
		world = null;
		Runtime runtime = Runtime.getRuntime();
		int mb = 1024*1024;
    
		System.gc();
		
		map = new World();
		world = new World();
		
	    System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);
        System.out.println("Free mem: "+runtime.freeMemory()/mb);
 
        panner = new Panner(world.getImage());
		updateWorld();
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

		repaint();
	}


	/**
	 * This method draws logged-in ICEtizens and Aliens on the World Image
	 */
	public void populateWorld(Graphics2D g2) {
		
		// remove all users from the map 
		//patchCitizens(g2);



		// converts tileSpace to screenSpace coordinates
		Point pos = null;
		Point currentTileSpacePos = null;
		
		
		/* MINIMAP STUFF */
		// for minimap display
		Point miniMapPos = null;
		// clear minimap
		minimap = new Minimap();
		
		Inhabitant inh = new Inhabitant();
		
		for (ICEtizen value : loggedinUsers.values()) {
			
		    System.out.print("VALUE: "+value.getUsername());
		
			currentTileSpacePos = value.getCurrentPosition();

	
		    if(currentTileSpacePos!=null){
				System.out.println("  "+currentTileSpacePos.x+","+currentTileSpacePos.y);
		    	 pos = Scaler.toScreenSpace(currentTileSpacePos);
			    	g2.drawImage((value.getType() == 1)?inh.avatar:alienImage, pos.x - ICEtizen.AVATAR_OFFSET_X, pos.y - ICEtizen.AVATAR_OFFSET_Y, this);
			    	minimap.drawUser(pos);
			    	
		    }
		}
		
		// update the controller user
		currentTileSpacePos = LoginPage.me.getCurrentPosition();
		pos = Scaler.toScreenSpace(currentTileSpacePos);
		
    	g2.drawImage((LoginPage.me.getType() == 1)?inh.avatar:alienImage, pos.x - ICEtizen.AVATAR_OFFSET_X, pos.y - ICEtizen.AVATAR_OFFSET_Y, this);
    	minimap.drawUser(pos);
    	
		// TODO Handle walk. If position changed then walk
		Point lastKnownPosition = null;
		// schedule a walk for every users
		// 
	}

	/**
	 * Puts empty tiles (from original map) in place of ICEtizens/Aliens This
	 * avoids having to render a new world every time the world updates (aka.
	 * Adaptive tile replacement)
	 */
	public void patchCitizens(Graphics2D g2) {

		//Point draw = Scaler.toScreenSpace(States.activeUserLastKnownPosition);
		
		//BufferedImage patchImage = avatarPatcher.getPatchImage(draw.x - ICEtizen.AVATAR_OFFSET_X, draw.y - ICEtizen.AVATAR_OFFSET_Y);

		//g2.drawImage(patchImage, draw.x - ICEtizen.AVATAR_OFFSET_X, draw.y
		//		- ICEtizen.AVATAR_OFFSET_Y, this);
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


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(viewport, 0, 0, null);
		g.drawImage(minimap.getImage(), ICEWORLD_VIEWPORT_SIZE.width
				- Minimap.MINIMAP_SIZE.width - 10, 10, null);
	}

}
