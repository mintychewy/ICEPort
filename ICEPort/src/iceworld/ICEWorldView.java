package iceworld;

import gui.LoginPage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Timer;

import javax.swing.JPanel;
import javax.swing.KeyStroke;

import objects.Alien;
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

	String weather = "raining";
	// 
	Inhabitant inh;
	Alien ali;
	
	
	// zooming factor -- default being 1.0 (i.e., 1.0*100 = 100 %)
	public static double zoom_factor = 1.0;

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

	/* THIS IS FOR KEEPING THE 100% ZOOM IN MEMORY */
	World worldFull;
	World mapFull;

	Panner panner;
	Patcher avatarPatcher;
	BufferedImage viewport;

	// Indicators
	Image yellowIndicator = ImageLoader.loadImageFromLocal("images/yellow-arrow.png");
	Image redIndicator = ImageLoader.loadImageFromLocal("images/red-arrow.png");


	public static int deltaX = 0;
	public static int deltaY = 0;

	public final static Dimension ICEWORLD_VIEWPORT_SIZE = new Dimension(900,
			600);

	public ICEWorldView() {
		setPreferredSize(ICEWORLD_VIEWPORT_SIZE);

		// XXX test
		// KEY BINDING FOR ZOOM
		// zoom out CTRL + - 
		ZoomOutAction zoomOutAction = new ZoomOutAction();

		
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK),
				"doZoomOut");
		this.getActionMap().put("doZoomOut",
				zoomOutAction);
		
		
		// zoom in CTRL + +
		ZoomInAction zoomInAction = new ZoomInAction();
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK),
				"doZoomIn");
		this.getActionMap().put("doZoomIn",
				zoomInAction);
				
		////////////////////////

		inh = new Inhabitant();
		ali = new Alien();

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
		System.out.println("Scaled delta (x"+zoom_factor+") : x-"+deltaX+" y:"+deltaY);
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
			LoginPage.immigration.walk(destinationTile.x, destinationTile.y);
			walk();
		}else{
			System.out.println("Invalid destination point");
		}

	}


	public void zoomChanged(){

		
		
		/* KEEP MAX-ZOOM IN MEMORY FOR LATER USE */
		mapFull = map;
		worldFull = world;

		/* GET RUNTIME MEMORY INFO FOR DEBUGGING */
		Runtime runtime = Runtime.getRuntime();
		int mb = 1024*1024;

		System.gc();

		if(zoom_factor == 1){
			map = mapFull;
			world = worldFull;
			// reset world
			world.getImage().getGraphics().drawImage(map.getImage(), 0, 0, null);
		}else{

			map = new World();
			world = new World();

		}
		System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free mem: "+runtime.freeMemory()/mb);

		deltaX = 0;
		deltaY = 0;
		/* Still breaks panner
		deltaX = (int)(deltaX*zoom_factor);
		deltaY = (int)(deltaY*zoom_factor);
		 */
		panner = new Panner(world.getImage());

		/* UPDATE SIZE OF AVATAR IMAGES */
		inh = new Inhabitant();
		ali = new Alien();

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
		updateYell(g2);

		// Show update
		viewport = panner.getWorldViewport();

		repaint();
	}

	/**
	 * This methods paints a yell on the user's screen
	 * @param g2 World Graphics2D object
	 */
	public void updateYell(Graphics2D g2) {
		// long lastYelled = (timestamp);
		// long latestYellTimestamp;
		// compare these two 

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

		inh = new Inhabitant();

		/* XY-OFFSET CORRECTION IN ZOOM MODE */
		int zoomCorrectionYOffset = 0;
		int zoomCorrectionXOffset = 0;
		if(zoom_factor == 1.0){
			// = 0
		} else if(zoom_factor >= 0.8){
			zoomCorrectionYOffset = (int)((1.0/zoom_factor)*6);
			zoomCorrectionXOffset = (int)((1.0/zoom_factor)*4);
		}else if(zoom_factor >= 0.5 && zoom_factor < 0.8){
			zoomCorrectionYOffset = (int)((1.0/zoom_factor)*10);
			zoomCorrectionXOffset = (int)((1.0/zoom_factor)*3);
		}else if(zoom_factor >= 0.3 && zoom_factor < 0.5){
			zoomCorrectionYOffset = (int)((1.0/zoom_factor)*7);
			zoomCorrectionXOffset = (int)((1.0/zoom_factor)*2);
		}else if(zoom_factor < 0.3){
			zoomCorrectionYOffset = (int)((1.0/zoom_factor)*2);
			zoomCorrectionXOffset = (int)((1.0/zoom_factor)*1);
		
		}

		for (ICEtizen value : loggedinUsers.values()) {

			System.out.print("Users: "+value.getUsername());

			currentTileSpacePos = value.getCurrentPosition();

			if(currentTileSpacePos!=null){

				System.out.print("  "+currentTileSpacePos.x+","+currentTileSpacePos.y);
				pos = Scaler.toScreenSpace(currentTileSpacePos);
				g2.drawImage((value.getType() == 1)?inh.avatar:ali.avatar, pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor) - zoomCorrectionXOffset, (int) (pos.y - ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
						-zoomCorrectionYOffset, this);


				// put a yellow arrow above the head to ICEtizen with the same ICEPort (pid = 245)
				if(value.getIcePortID() == 245){
					g2.drawImage(redIndicator, pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor), (int) (pos.y - ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
							-zoomCorrectionYOffset - 10, this);
				}

				// put username
				g2.drawString(value.getUsername(), pos.x-10, pos.y+10);

				minimap.drawUser(currentTileSpacePos,1);

			}
		}

		// update the controller user
		currentTileSpacePos = LoginPage.me.getCurrentPosition();
		pos = Scaler.toScreenSpace(currentTileSpacePos);

		g2.drawImage((LoginPage.me.getType() == 1)?inh.avatar:ali.avatar, pos.x - (int) (ICEtizen.AVATAR_OFFSET_X*zoom_factor) - zoomCorrectionXOffset, pos.y - (int)(ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
				- zoomCorrectionYOffset , this);

		// put a red arrow above the head of the controller ICEtizen
		g2.drawImage(redIndicator, pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor), (int) (pos.y - ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
				-zoomCorrectionYOffset - 10, this);
		minimap.drawUser(pos,0);

		// put username

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

	}

	@Override
	public void keyPressed(KeyEvent e) {
		panner.pan(e);

		updateWorld();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(viewport, 0, 0, null);
		g.drawImage(minimap.getImage(), ICEWORLD_VIEWPORT_SIZE.width
				- Minimap.MINIMAP_SIZE.width - 10, 10, null);
	}



}
