package iceworld;

import gui.LoginPage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import javax.swing.JLayeredPane;
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

public class ICEWorldView extends JLayeredPane implements MouseListener,
MouseMotionListener, KeyListener {

	// username is used as a key in the users HashMap
	String controllerUsername;

	public String weather = "sunny";

	/* LAYERS */
	JLayeredPane jlp;

	JPanel mapPanel;
	AnimationTestPanel testPanel;
	

	ArrayList<Timer> timerList;
	
	HashMap<String,Point> lastKnownPositionList;

	private static final long serialVersionUID = 5658988277615488303L;
	public final static Dimension ICEWORLD_VIEWPORT_SIZE = new Dimension(900,600);

	// zooming factor
	// default being 1.0 (i.e., 1.0*100 = 100 %)
	public static double zoom_factor = 1.0;
	/* XY-OFFSET CORRECTION IN ZOOM MODE */
	int zoomCorrectionYOffset = 0;
	int zoomCorrectionXOffset = 0;
	// camera panning position
	public static int deltaX = 0;
	public static int deltaY = 0;

	// Privilege "instant" yell/talk/walk for controller ICEtizen 
	public static String instantYellMessage = "";
	public static String instantTalkMessage = "";

	public static Point controllersLocalPosition;

	// states fetching interval (default: 2000ms)
	public static int REFRESH_INTERVAL = 2000;
	public Thread fetchThread;
	private boolean terminateThread;

	Inhabitant inh;
	Alien ali;

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

	Image yellowIndicator, redIndicator;

	public ICEWorldView() {

		setPreferredSize(ICEWORLD_VIEWPORT_SIZE);


		mapPanel = new JPanel();
		mapPanel.setPreferredSize(new Dimension(900,600));
		mapPanel.setBounds(0,0,900,600);
		//jlp.add(mapPanel, new Integer(10000));
		this.add(mapPanel, new Integer(0));
		testPanel = new AnimationTestPanel();
		//testPanel.setOpaque(false);
		testPanel.setBounds(0,0,900,600);
		testPanel.setBackground(new Color(0,0,0,0));
		//jlp.add(testPanel, new Integer(-10000));
		this.add(testPanel, JLayeredPane.DRAG_LAYER);


		//this.add(jlp);
		/*////////////////////////////////////////*/


		 timerList = new ArrayList<Timer>();

		loadResources();
		setKeybinding();

		inh = new Inhabitant();
		ali = new Alien();

		fetcher = new WorldStatesFetcher();
		fetcher.updateWorldStates();

		loggedinUsers = fetcher.getLoggedinUserMap();

		
		lastKnownPositionList = new HashMap<String,Point>();
		
		// alien
		// the unique point for identifying our controller alien
		Point hashpos = new Point(LoginPage.uniquePosition,-245);
		controllerUsername = "";
		if (LoginPage.me.getType() == 0) {
			// need to find self from the loggedinUsers list		
			for(ICEtizen value : loggedinUsers.values()){

				if(value.getCurrentPosition()!=null)
						lastKnownPositionList.put(value.getUsername(), value.getCurrentPosition());
					// 
					
					if(value.getCurrentPosition().x == hashpos.x && value.getCurrentPosition().y == hashpos.y){
						controllerUsername = value.getUsername();
						LoginPage.me = value;
						System.out.println("You are: "+ controllerUsername);
					}
			}

			LoginPage.me.setCurrentPosition(new Point(0, 0));
			LoginPage.immigration.walk(0, 0);
		}

		// inhabitant
		if (LoginPage.me.getType() != 0) {
			// give the controller a dedicated instance
			LoginPage.me = loggedinUsers.get(LoginPage.me.getUsername());
			controllerUsername = LoginPage.me.getUsername();

			// if there's no position yet, assume 0,0
			if (LoginPage.me.getCurrentPosition() == null) {

				LoginPage.immigration.walk(0, 0);
				// avoid having to re-fetch the states
				LoginPage.me.setCurrentPosition(new Point(0, 0));

			}
		}


		controllersLocalPosition = LoginPage.me.currentPosition;


		addListeners();

		
		
		// set initial lastKnownPositions
		for(ICEtizen value : loggedinUsers.values()) {
			if(value.getUsername().equals(controllerUsername))
				continue;
			
			String key = value.getUsername();
			if(value.getCurrentPosition() == null){
				System.out.println("user: "+value.getUsername()+" has NULL position");
				continue ;
			}
			
			this.lastKnownPositionList.put(key, value.getCurrentPosition());
			
		}
		///
		
		

		initialiseWorld();


		

		/* THREAD FOR FETCHING DATA FROM SERVER */
		terminateThread = false;
		fetchThread = new Thread(new Runnable(){
			public void run() {
				while(!terminateThread){
					System.out.println("fetching states..");
					isolateController();	
					fetcher.updateWorldStates();
					
					
			
					// check whether if a (non-active) ICEtizen needs 
					// to be a scheduled a walk
					for(ICEtizen value : loggedinUsers.values()) {
						if(value.getUsername().equals(controllerUsername))
							continue;
						
						String key = value.getUsername();
						Point lastKnownPosition = lastKnownPositionList.get(key);
						Point dest = value.getCurrentPosition();
						if(!dest.equals(lastKnownPosition)){

							System.out.println(key+" needs walking.");
							Timer timerTemp = new Timer();
							
							System.out.println("LastknownPosition: "+ lastKnownPosition.toString());
							
							timerTemp.schedule(new WalkingTaskOthers(key,lastKnownPosition, dest), 0, 100);
							
							//timerList.add(timerTemp);
						}
					}
					
					
					
					updateWorld();
					try {
						Thread.sleep(REFRESH_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});

		fetchThread.start();
	}


	/**
	 * Isolates the controller ICEtizen from the 
	 * list of loggedin users
	 */
	private void isolateController() {
		//LoginPage.me = loggedinUsers.get(controllerUsername);
	}

	/**
	 * Sets fetching-thread terminate flag to true
	 */
	public void terminate() {
		this.terminateThread = true;
	}

	/**
	 * Starts a thread to fetch data from server
	 */
	public void createNewFetchingThread() {

		/* THREAD FOR FETCHING DATA FROM SERVER */
		terminateThread = false;
		fetchThread = new Thread(new Runnable(){
			public void run() {
				while(!terminateThread){
					System.out.println("fetching states..");
					isolateController();
					
					// check whether if a (non-active) ICEtizen needs 
					// to be a scheduled a walk
					for(ICEtizen value : loggedinUsers.values()) {
						if(value.getUsername().equals(controllerUsername))
							continue;
						
						String key = value.getUsername();
						Point lastKnownPosition = lastKnownPositionList.get(key);
						Point dest = value.getCurrentPosition();
						if(!dest.equals(lastKnownPosition)){
							Timer timerTemp = new Timer();
							
							
							timerTemp.schedule(new WalkingTaskOthers(key,lastKnownPosition, dest), 0, 100);
					
							timerList.add(timerTemp);
						}
					}
					
					
					
					fetcher.updateWorldStates();
					updateWorld();
					try {
						Thread.sleep(REFRESH_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});

	}

	@Override
	public void mousePressed(MouseEvent e) {

		/* INVOKE WALKING FOR THE CONTROLLER ICETIZEN */

		// converts mouseclick position into a tile position
		Point destinationTile = Scaler.toTileSpaceFromViewport(e.getPoint());
		System.out.println("Heading to: " + destinationTile.toString());


		// check whether if it is a valid destination
		if (!world.isFallingIntoTartarus(destinationTile)) {

			LoginPage.me.setCurrentPosition(destinationTile);
			LoginPage.immigration.walk(destinationTile.x, destinationTile.y);

			if(timer != null)
				timer.cancel();
			walkMyself();
		} else {
			System.out.println("Invalid destination point");
		}

	}

	public void zoomChanged() {


		//controllersLocalPosition = Scaler.scalePoint(controllersLocalPosition, zoom_factor);
		/* KEEP MAX-ZOOM IN MEMORY FOR LATER USE */
		mapFull = map;
		worldFull = world;

		/* GET RUNTIME MEMORY INFO FOR DEBUGGING */
		Runtime runtime = Runtime.getRuntime();
		int mb = 1024 * 1024;

		// call garbage collector
		// as we are going to instantiate a new World object
		// which consumes a lot of memory
		System.gc();

		if (zoom_factor == 1) {
			map = mapFull;
			world = worldFull;
			// reset world
			world.getImage().getGraphics()
			.drawImage(map.getImage(), 0, 0, null);
		} else {

			map = new World();
			world = new World();

		}

		System.out.println("Used Memory:"
				+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free mem: " + runtime.freeMemory() / mb);

		deltaX = 0;
		deltaY = 0;
		/*
		 * Still breaks panner deltaX = (int)(deltaX*zoom_factor); deltaY =
		 * (int)(deltaY*zoom_factor);
		 */
		panner = new Panner(world.getImage());

		/* UPDATE SIZE OF AVATAR IMAGES */
		inh = new Inhabitant();
		ali = new Alien();

		updateWorld();
	}

	Timer timer;

	public void walkMyself() {
		long myLong = 100;
		timer = new Timer();
		timer.schedule(new WalkingTask(), 0, myLong);
	}

	/**
	 * Renders the World image with the latest elements
	 */
	public void updateWorld() {

		Graphics2D g2 = (Graphics2D) world.getImage().getGraphics();

		// notify walk 
		for(ICEtizen value : loggedinUsers.values()){
			if(value.getUsername().equals(controllerUsername))
				continue;
			
			// walk(value.getCurrentPosition(),);
		}
		// Update ICEtizen/Alien positions
		populateWorld(g2);

		// Update chat
		// updateChat(g2);

		// Show update
		viewport = panner.getWorldViewport();

		Graphics2D g2Viewport = (Graphics2D) viewport.getGraphics();

		//updateYell(g2Viewport);

		g2Viewport.drawImage(minimap.getImage(), ICEWORLD_VIEWPORT_SIZE.width
				- Minimap.MINIMAP_SIZE.width - 10, 10, null);

		showView();
	}


	/**
	 * This methods paints a yell on the user's screen
	 * 
	 * @param g viewpeort Graphics object
	 */
	public void updateYell(Graphics g) {
		// long lastYelled = (timestamp);
		// long latestYellTimestamp;
		// compare these two

		g.setColor(Minimap.BLACK_WITH_50_PERCENT_ALPHA);
		g.fillRect(0, 300, 900, 300);

		Font font = new Font ("Arial", Font.PLAIN, (instantYellMessage.length() <= 5)?200:100);
		g.setFont(font);
		g.setColor(Color.YELLOW);
		g.drawString(instantYellMessage, 10, (instantYellMessage.length() <=5)?530:500);


	}

	/**
	 * This method draws logged-in ICEtizens and Aliens on the World Image 
	 * including their usernames and other visual indicators 
	 */
	public void populateWorld(Graphics2D g2) {

		// remove all users from the map
		patchCitizens(g2);


		Point pos = null;
		Point currentTileSpacePos = null;

		/* MINIMAP STUFF */
		// for minimap display
		Point miniMapPos = null;
		// clear minimap
		minimap = new Minimap();

		if (zoom_factor == 1.0) {
			// = 0
		} else if (zoom_factor >= 0.8) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 6);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 4);
		} else if (zoom_factor >= 0.5 && zoom_factor < 0.8) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 10);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 3);
		} else if (zoom_factor >= 0.3 && zoom_factor < 0.5) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 7);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 2);
		} else if (zoom_factor < 0.3) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 2);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 1);
		}

	
		

		for(ICEtizen value : loggedinUsers.values()) {
			if(value.getUsername().equals(controllerUsername))
				continue ;
			
			currentTileSpacePos = lastKnownPositionList.get(value.getUsername());
			if(currentTileSpacePos != null) {
				pos = Scaler.toScreenSpace(currentTileSpacePos);
				g2.drawImage((value.getType() == 1) ? inh.avatar : ali.avatar,
						pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
						- zoomCorrectionXOffset,
						(int) (pos.y - ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
						- zoomCorrectionYOffset, this);

				// put a yellow arrow above the head to ICEtizen with the same
				// ICEPort (pid = 245)
				if (value.getIcePortID() == 245) {

					//System.out.println("PLAYER: "+value.getUsername()+" has the same client.");
					g2.drawImage(yellowIndicator, pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
							- zoomCorrectionXOffset,
							(int) (pos.y - ICEtizen.AVATAR_OFFSET_Y
									* zoom_factor)
									- zoomCorrectionYOffset - 10, this);
				}
				
				// put username
				g2.drawString(value.getUsername(), pos.x - 10, pos.y + 10);
				// put ip
				g2.drawString(value.getIPAddress(), pos.x - 10, pos.y + 20);

				minimap.drawUser(currentTileSpacePos, 1);

			}
		}
	
		/*
		
		// iterate the list of logged-in users and draw them
		for (ICEtizen value : loggedinUsers.values()) {

			if(value.getUsername().equals(controllerUsername))
				continue;

			currentTileSpacePos = value.getCurrentPosition();

			if (currentTileSpacePos != null) {

				//System.out.print(" " + currentTileSpacePos.x + ","
				//	+ currentTileSpacePos.y+" ");

				// find the pixel position of the user on the world
				pos = Scaler.toScreenSpace(currentTileSpacePos);

				g2.drawImage((value.getType() == 1) ? inh.avatar : ali.avatar,
						pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
						- zoomCorrectionXOffset,
						(int) (pos.y - ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
						- zoomCorrectionYOffset, this);

				// put a yellow arrow above the head to ICEtizen with the same
				// ICEPort (pid = 245)
				if (value.getIcePortID() == 245) {

					//System.out.println("PLAYER: "+value.getUsername()+" has the same client.");
					g2.drawImage(yellowIndicator, pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
							- zoomCorrectionXOffset,
							(int) (pos.y - ICEtizen.AVATAR_OFFSET_Y
									* zoom_factor)
									- zoomCorrectionYOffset - 10, this);
				}

				// put username
				g2.drawString(value.getUsername(), pos.x - 10, pos.y + 10);
				// put ip
				g2.drawString(value.getIPAddress(), pos.x - 10, pos.y + 20);

				minimap.drawUser(currentTileSpacePos, 1);

			}
		}

		*/
		
		
		// update the controller user
		//currentTileSpacePos = LoginPage.me.getCurrentPosition();


		currentTileSpacePos = controllersLocalPosition;
		pos = Scaler.toScreenSpace(currentTileSpacePos);

		/*
		pos = controllersLocalPosition;
		System.out.println(pos.toString());
		 */

		g2.drawImage((LoginPage.me.getType() == 1) ? inh.avatar : ali.avatar,
				pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
				- zoomCorrectionXOffset, pos.y
				- (int) (ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
				- zoomCorrectionYOffset, this);

		// put a red arrow above the head of the controller ICEtizen
		g2.drawImage(redIndicator, pos.x
				- (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor),
				(int) (pos.y - ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
				- zoomCorrectionYOffset - 10, this);
		// put username
		g2.drawString(LoginPage.me.getUsername(), pos.x - 10, pos.y + 10);
		// put ip
		g2.drawString(LoginPage.me.getIPAddress(), pos.x - 10, pos.y + 20);


		minimap.drawUser(currentTileSpacePos, 0);

		// put username

	}

	/**
	 * Puts empty tiles (from original map) in place of ICEtizens/Aliens This
	 * avoids having to render a new world every time the world updates (aka.
	 * Adaptive tile replacement)
	 */
	public void patchCitizens(Graphics2D g2) {
		Point draw;
		Point tilePos;
		BufferedImage patchImage;

		// just clear out the viewport?

		patchImage = map.getImage().getSubimage(deltaX,deltaY, 900, 600);
		g2.drawImage(patchImage,deltaX,deltaY,null);

		/*
		//System.out.println("patching");
		for(ICEtizen value : loggedinUsers.values()){

			//last know pos
			tilePos = value.getCurrentPosition();

			if(tilePos == null || tilePos.x < 0 || tilePos.x > 100)
				continue;



			draw = Scaler.toScreenSpace(tilePos);
			//System.out.println("draw: ("+draw.x+","+draw.y+")  tilePos: ("+tilePos.x+","+tilePos.y+")");
			patchImage = avatarPatcher.getPatchImage(
						(draw.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
					- zoomCorrectionXOffset), 
					(draw.y - (int)(ICEtizen.AVATAR_OFFSET_Y * zoom_factor))
					- zoomCorrectionYOffset);

			g2.drawImage(patchImage,(draw.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
					- zoomCorrectionXOffset), 
					(draw.y - (int)(ICEtizen.AVATAR_OFFSET_Y * zoom_factor))
					- zoomCorrectionYOffset,null);

		}
		 */
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

	@Override
	public void mouseClicked(MouseEvent e) {
	}


	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
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

	private void addListeners() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		setFocusable(true);
	}

	private void loadResources() {
		yellowIndicator = ImageLoader
				.loadImageFromLocal("images/yellow-arrow.png");
		redIndicator = ImageLoader.loadImageFromLocal("images/red-arrow.png");
	}


	/**
	 * This internal method sets up keybinding
	 * for zooming actions
	 */
	private void setKeybinding() {

		// Zoom-out on "CTRL + -"
		ZoomOutAction zoomOutAction = new ZoomOutAction();
		this.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
						KeyEvent.CTRL_DOWN_MASK), "doZoomOut");
		this.getActionMap().put("doZoomOut", zoomOutAction);

		// Zoom-in on "CTRL + +"
		ZoomInAction zoomInAction = new ZoomInAction();
		this.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,
						KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK),
				"doZoomIn");
		this.getActionMap().put("doZoomIn", zoomInAction);

	}


	private void initialiseWorld() {

		map = new World();
		world = new World();
		minimap = new Minimap();

		avatarPatcher = new Patcher(map.getImage(), ICEtizen.AVATAR_SIZE.width,
				ICEtizen.AVATAR_SIZE.height);

		populateWorld((Graphics2D) world.getImage().getGraphics());

		panner = new Panner(world.getImage());

		updateWorld();
	}

	public void showView() {
		Graphics2D g2map = (Graphics2D) mapPanel.getGraphics();
		if(g2map == null) 
			return;
		g2map.drawImage(viewport,0,0,null);
	}

	public void paintComponent(Graphics g) {

	}

}