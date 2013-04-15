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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;

import javax.swing.JPanel;
import javax.swing.KeyStroke;

import objects.Alien;
import objects.ICEtizen;
import objects.Inhabitant;
import objects.Minimap;
import objects.World;
import util.ActionFetcher;
import util.Actions;
import util.ICEWorldPeek;
import util.ImageLoader;
import util.Patcher;
import util.Scaler;
import util.WorldStatesFetcher;

public class ICEWorldView extends JPanel implements MouseListener,
MouseMotionListener, KeyListener {



	public ICEWorldView() {

		setPreferredSize(ICEWORLD_VIEWPORT_SIZE);


		inh = new Inhabitant();
		ali = new Alien();
		yellImageList = new LinkedList<BufferedImage>();
		talkImageList = new LinkedList<BufferedImage>();
		timer = new Timer();

		loadResources();
		setKeybinding();


		// initial world state fetch
		fetcher = new WorldStatesFetcher();
		fetcher.updateWorldStates();

		loggedinUsers = fetcher.getLoggedinUserMap();

		lastKnownPositionList = new HashMap<String, Point>();

		// alien
		// the unique point for identifying our controller alien
		Point hashpos = new Point(LoginPage.uniquePosition, -245);
		controllerUsername = "";
		if (LoginPage.me.getType() == 0) {
			// need to find self from the loggedinUsers list
			for (ICEtizen value : loggedinUsers.values()) {

				if (value.getCurrentPosition() == null)
					continue;
				if (value.getCurrentPosition().x == hashpos.x
						&& value.getCurrentPosition().y == hashpos.y) {
					controllerUsername = value.getUsername();
					LoginPage.me = value;
					System.out.println("You are: " + controllerUsername);
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


		weather = fetcher.conditionWeather;

		controllersLocalPosition = LoginPage.me.currentPosition;

		addListeners();

		initialiseWorld();

		// set initial lastKnownPositions
		for (ICEtizen value : loggedinUsers.values()) {
			if (value.getUsername().equals(controllerUsername))
				continue;

			Point p = value.getCurrentPosition();
			if (value.getCurrentPosition() != null){

				boolean isOutOfBound = world.isFallingIntoTartarus(p);

				if(!isOutOfBound)
					lastKnownPositionList.put(value.getUsername(), value.getCurrentPosition());
			}


		}


		// initial actions fetch
		actionFetcher = new ActionFetcher();
		try {
			ActionFetcher.from = Long.parseLong((ICEWorldPeek.getData("time"))
					.substring(20, 30));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		createNewFetchingThread();
		fetchThread.start();



		/* GET RUNTIME MEMORY INFO FOR DEBUGGING */
		Runtime runtime = Runtime.getRuntime();
		int mb = 1024 * 1024;
		System.gc();


		System.out.println("Used Memory:"
				+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free mem: " + runtime.freeMemory() / mb);

	}

	public void zoomChanged() {

		/* GET RUNTIME MEMORY INFO FOR DEBUGGING */
		Runtime runtime = Runtime.getRuntime();
		int mb = 1024 * 1024;
		System.gc();

		map = new World();
		world = new World();

		/*
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
		 */

		System.out.println("Used Memory:"
				+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free mem: " + runtime.freeMemory() / mb);

		deltaX = 0;
		deltaY = 0;

		//deltaX = (int)(deltaX*zoom_factor); 
		//deltaY = (int)(deltaY*zoom_factor);

		panner = new Panner(world.getImage());

		/* UPDATE SIZE OF AVATAR IMAGES */
		inh = new Inhabitant();
		ali = new Alien();

		updateWorld();
	}


	/**
	 * XXX Renders the World image with the latest elements
	 */
	public void updateWorld() {

		Graphics2D g2 = (Graphics2D) world.getImage().getGraphics();

		// notify walk
		for (ICEtizen value : loggedinUsers.values()) {
			if (value.getUsername().equals(controllerUsername))
				continue;

			// walk(value.getCurrentPosition(),);
		}
		// Update ICEtizen/Alien positions
		populateWorld(g2);

		// Update chat
		updateChat(g2);

		// Show update
		viewport = panner.getWorldViewport();

		Graphics2D g2Viewport = (Graphics2D) viewport.getGraphics();

		updateYell(g2Viewport);

		g2Viewport.drawImage(minimap.getImage(), ICEWORLD_VIEWPORT_SIZE.width
				- Minimap.MINIMAP_SIZE.width - 10, 10, null);

		showView();
	}

	private void updateChat(Graphics2D g2) {

		for (BufferedImage talkImg : talkImageList) {
			g2.drawImage(talkImg, 100, 100, null);
		}

	}

	/**
	 * This method paints yells on the user's screen
	 * 
	 * @param g viewport Graphics object
	 */
	public void updateYell(Graphics g) {
		for (BufferedImage yellImg : yellImageList) {
			g.drawImage(yellImg, 0, 0, null);
		}
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
		// clear minimap
		minimap = new Minimap();

		if (zoom_factor == 1.0) {
			zoomCorrectionYOffset = 0;
			zoomCorrectionXOffset = 0;
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

		for (ICEtizen value : loggedinUsers.values()) {
			if (value.getUsername().equals(controllerUsername))
				continue;



			currentTileSpacePos = lastKnownPositionList
					.get(value.getUsername());

			if (currentTileSpacePos != null) {
				if(world.isFallingIntoTartarus(currentTileSpacePos)){
					continue;
				}
				//System.out.println(value.getUsername()+" : "+value.getCurrentPosition().x+","+value.getCurrentPosition().y);

				pos = Scaler.toScreenSpace(currentTileSpacePos);
				g2.drawImage((value.getType() == 1) ? inh.avatar : ali.avatar,
						pos.x - (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
						- zoomCorrectionXOffset,
						(int) (pos.y - ICEtizen.AVATAR_OFFSET_Y * zoom_factor)
						- zoomCorrectionYOffset, this);

				// put a yellow arrow above the head to ICEtizen with the same
				// ICEPort (pid = 245)
				if (value.getIcePortID() == 245) {

					// System.out.println("PLAYER: "+value.getUsername()+" has the same client.");
					g2.drawImage(yellowIndicator, pos.x
							- (int) (ICEtizen.AVATAR_OFFSET_X * zoom_factor)
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



		// update the controller user
		// currentTileSpacePos = LoginPage.me.getCurrentPosition();

		currentTileSpacePos = controllersLocalPosition;
		pos = Scaler.toScreenSpace(currentTileSpacePos);

		/*
		 * pos = controllersLocalPosition; System.out.println(pos.toString());
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


		//System.out.println("Going to get subimage from: "+ deltaX +","+deltaY);

		patchImage = map.getImage().getSubimage(deltaX, deltaY, 900, 600);
		g2.drawImage(patchImage, deltaX, deltaY, null);

		/*
		 * //System.out.println("patching"); for(ICEtizen value :
		 * loggedinUsers.values()){
		 * 
		 * //last know pos tilePos = value.getCurrentPosition();
		 * 
		 * if(tilePos == null || tilePos.x < 0 || tilePos.x > 100) continue;
		 * 
		 * 
		 * 
		 * draw = Scaler.toScreenSpace(tilePos);
		 * //System.out.println("draw: ("+draw
		 * .x+","+draw.y+")  tilePos: ("+tilePos.x+","+tilePos.y+")");
		 * patchImage = avatarPatcher.getPatchImage( (draw.x - (int)
		 * (ICEtizen.AVATAR_OFFSET_X * zoom_factor) - zoomCorrectionXOffset),
		 * (draw.y - (int)(ICEtizen.AVATAR_OFFSET_Y * zoom_factor)) -
		 * zoomCorrectionYOffset);
		 * 
		 * g2.drawImage(patchImage,(draw.x - (int) (ICEtizen.AVATAR_OFFSET_X *
		 * zoom_factor) - zoomCorrectionXOffset), (draw.y -
		 * (int)(ICEtizen.AVATAR_OFFSET_Y * zoom_factor)) -
		 * zoomCorrectionYOffset,null);
		 * 
		 * }
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


	@Override
	public void mousePressed(MouseEvent e) {

		this.requestFocus(true);

		Point destinationTile = null;
		// check if it is a minimap coordinate
		if(minimap.isMinimapClicked(e)){
			Point panDelta = Scaler.toScreenSpace(minimap.panUser(e.getPoint()));

			int x = panDelta.x;
			int y = panDelta.y;

			/* VALIDATING DELTA CHANGE */


			if(y + ICEWORLD_VIEWPORT_SIZE.height <= World.WORLD_SIZE.height){
				deltaY = y;
				if(x + ICEWORLD_VIEWPORT_SIZE.width <= World.WORLD_SIZE.width){
					deltaX = x;
				}else {
					deltaX = World.WORLD_SIZE.width - ICEWORLD_VIEWPORT_SIZE.width;

				}
			} else {
				deltaY = World.WORLD_SIZE.height - ICEWORLD_VIEWPORT_SIZE.height;
				if(x + ICEWORLD_VIEWPORT_SIZE.width <= World.WORLD_SIZE.width){
					deltaX = x;
				}else {
					deltaX = World.WORLD_SIZE.width - ICEWORLD_VIEWPORT_SIZE.width;

				}
			}





			updateWorld();
		} else {

			/* INVOKES WALKING FOR THE CONTROLLER ICETIZEN */

			// converts mouseclick position into a tile position

			destinationTile = Scaler.toTileSpaceFromViewport(e.getPoint());

			System.out.println("Heading to: " + destinationTile.toString());

			// check whether if it is a valid destination
			if (!world.isFallingIntoTartarus(destinationTile)) {

				// set intended destination
				LoginPage.me.setCurrentPosition(destinationTile);

				// stops the current walking path 
				timer.cancel();
				// sets a new walking path
				walkMyself();

				// reports walking to the server
				LoginPage.immigration.walk(destinationTile.x, destinationTile.y);

			} else {
				System.out.println("Invalid destination point");
			}

		}




	}

	private void walkMyself() {
		long myLong = 100;
		timer = new Timer();
		timer.schedule(new WalkingTask(), 0, myLong);
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

	/**
	 * Starts a thread to fetch data from server
	 * Walks/Talks/Yells will be scheduled if
	 * there is an update 
	 */
	public void createNewFetchingThread() {

		/* THREAD FOR FETCHING DATA FROM SERVER */
		terminateThread = false;
		fetchThread = new Thread(new Runnable() {
			public void run() {
				while (!terminateThread) {
					System.out.println("fetching states..");
					fetcher.updateWorldStates();
					actionFetcher.fetchActions();

					String user;
					Point pos;
					// If there is a new character logged in, 
					// add it to the lastKnownPositionList
					for(ICEtizen itz : fetcher.getLoggedinUserMap().values()) {
						user = itz.getUsername();
						pos  = itz.getCurrentPosition();

						if(!lastKnownPositionList.containsKey(user)) {
							if(pos != null)
								if(!world.isFallingIntoTartarus(pos))	
									lastKnownPositionList.put(user, pos);
						}		
					}


					// If there is a logged-out user, 
					// remove it from the lastKnownPositionList
					for(ICEtizen itz : loggedinUsers.values()) {
						user = itz.getUsername();


						if(!fetcher.getLoggedinUserMap().containsKey(user)) {
							// remove the node
							lastKnownPositionList.put(user, null);
							lastKnownPositionList.remove(user);
							HashMap<String,Point> temp = new HashMap<String, Point>();
							temp.putAll(lastKnownPositionList);
							lastKnownPositionList = temp;
						}
					}


					loggedinUsers = fetcher.getLoggedinUserMap();


					try {
						ActionFetcher.from = Long.parseLong((ICEWorldPeek
								.getData("time")).substring(20, 30));
					} catch (Exception e1) {					
						e1.printStackTrace();
					}
					weather = fetcher.conditionWeather;

					/* WALKING SCHEDULER */
					// check whether if a (non-active) ICEtizen needs
					// to be a scheduled a walk
					for (ICEtizen value : loggedinUsers.values()) {
						if (value.getUsername().equals(controllerUsername))
							continue;

						String key = value.getUsername();
						Point lastKnownPosition = lastKnownPositionList
								.get(key);
						if (lastKnownPosition == null)
							continue;
						Point dest = value.getCurrentPosition();
						if (!dest.equals(lastKnownPosition)) {

							new Timer().schedule(new WalkingTaskOthers(key,
									lastKnownPosition, dest), 0, 100);

						}

					}

					// SCHEDULERS
					BufferedImage bf;
					/* YELLING SCHEDULER */
					//System.out.println("yellList size: "
					//		+ actionFetcher.yellList.size());
					for (Actions act : actionFetcher.yellList) {
						String uName = act.getUsername();

						if(uName != null){
							if (act.getUsername().equals(controllerUsername))
								continue;
						}
						if (act.getDetails() == null
								|| act.getDetails().length() <= 0)
							continue;

						System.out.println("This guy yelled! ==>"
								+ uName);

						bf = new BufferedImage(900, 600,
								BufferedImage.TYPE_INT_ARGB);

						Graphics2D g2bf = bf.createGraphics();

						g2bf.setColor(Minimap.BLACK_WITH_50_PERCENT_ALPHA);
						g2bf.fillRect(0, 300, 900, 300);

						Font font = new Font("Arial", Font.PLAIN, (act
								.getDetails().length() <= 5) ? 200 : 100);
						g2bf.setFont(font);
						g2bf.setColor(YellingTaskOthers.SKY_BLUE);
						g2bf.drawString(act.getDetails(), 10, (act.getDetails()
								.length() <= 5) ? 530 : 500);

						yellImageList.add(bf);
						new Timer()
						.schedule(new YellingTaskOthers(bf), 5000, 1);

					}

					/* TALKING SCHEDULER */
					//System.out.println("talkList size: "
					//		+ actionFetcher.talkList.size());
					for (Actions act : actionFetcher.talkList) {

						if (act.getUsername().equals(controllerUsername))
							continue;
						if (act.getDetails() == null
								|| act.getDetails().length() <= 0)
							continue;

						System.out.println("This guy talked! ==>"
								+ act.getUsername());

						bf = new BufferedImage(900, 600,
								BufferedImage.TYPE_INT_ARGB);

						Graphics2D g2bf = bf.createGraphics();
						g2bf.setColor(Color.black);
						g2bf.drawString(act.getDetails(), 100, 200);

						talkImageList.add(bf);
						new Timer()
						.schedule(new TalkingTaskOthers(bf), TALK_VISIBLE_DURATION, 1);

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

	}

	public void showView() {
		Graphics2D g2 =  (Graphics2D) getGraphics();
		if (g2 == null)
			return;
		g2.drawImage(viewport, 0, 0, null);
	}



	/**
	 * This internal method sets up keybinding for zooming actions
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

	/**
	 * Sets fetching-thread terminate flag to true
	 */
	public void terminate() {
		this.terminateThread = true;
	}


	public void paintComponent(Graphics g) {
	}


	// username is used as a KEY for every HashMaps
	String controllerUsername;

	ActionFetcher actionFetcher;

	public String weather = "sunny";

	Timer timer;

	public HashMap<String, ICEtizen> loggedinUsers;
	HashMap<String, Point> lastKnownPositionList;

	LinkedList<BufferedImage> yellImageList;
	LinkedList<BufferedImage> talkImageList;




	// states fetching interval (default: 2000ms)
	public static int REFRESH_INTERVAL = 2000;
	// chat bubble visible duration (default: 5000ms)
	public static int TALK_VISIBLE_DURATION = 5000;

	public final static Dimension ICEWORLD_VIEWPORT_SIZE = new Dimension(900,600);
	// zooming factor
	// default being 1.0 (i.e., 1.0*100 = 100 %)
	public static double zoom_factor = 1.0;
	// camera panning position
	public static int deltaX = 0;
	public static int deltaY = 0;
	// controller ICEtizen's position
	public static Point controllersLocalPosition;

	/* PRIVATE */
	private static final long serialVersionUID = 5658988277615488303L;


	/* XY-OFFSET CORRECTION IN ZOOM MODE */
	int zoomCorrectionYOffset = 0;
	int zoomCorrectionXOffset = 0;


	public Thread fetchThread;
	private boolean terminateThread;

	/* IMAGE RESOURCES */
	Inhabitant inh;
	Alien ali;
	Image yellowIndicator, redIndicator;


	// responsible for fetching everything from the server
	WorldStatesFetcher fetcher;



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



}