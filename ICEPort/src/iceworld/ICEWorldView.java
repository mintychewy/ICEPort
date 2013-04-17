package iceworld;

import gui.FClient;
import gui.FTPListener;
import gui.LoginPage;
import iceworld.given.IcetizenLook;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

import javax.imageio.ImageIO;
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

	HashMap<String, IcetizenLook> looksList;
	HashMap<String, BufferedImage> avatarList;


	/*
		ExecutorService pool = Executors.newFixedThreadPool(10);

		for (int k = 0; k < uids.size(); k++) {
		    pool.submit(new GetLooksTask(uids.get(k)));
		}

		pool.shutdown();
		pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
	 */


	public ICEWorldView() {
		setDoubleBuffered(true);

		looksList = new HashMap<String, IcetizenLook>();
		avatarList = new HashMap<String, BufferedImage>();

		setPreferredSize(ICEWORLD_VIEWPORT_SIZE);

		// create a red zoom-box 
		zoomBox = new BufferedImage(125,84, BufferedImage.TYPE_INT_ARGB);
		Graphics gZoomBox = zoomBox.createGraphics();
		gZoomBox.setColor(new Color(250,1,1,125));
		gZoomBox.fillRect(0, 0, 125, 84);

		ali = new Alien();

		yellImageList = new LinkedList<BufferedImage>();
		talkImageList = new LinkedList<TalkObject>();
		timer = new Timer();

		loadResources();

		setKeybinding();


		// initial world state fetch
		fetcher = new WorldStatesFetcher();
		fetcher.updateWorldStates();

		loggedinUsers = fetcher.getLoggedinUserMap();
		looksList = fetcher.looks;

		for(ICEtizen a : loggedinUsers.values()){
			if(a.getType() == 0){
				continue ;
			}

			avatarList.put(a.getUsername(), a.avatar);

		}

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

		// default position -- controller user at the
		// centre of the viewport

		Point initialDeltaPos = Scaler.toScreenSpace(controllersLocalPosition);

		fixAndSetDelta(new Point(initialDeltaPos.x -450, initialDeltaPos.y -400),0);

		updateWorld();

		createNewFetchingThread();
		fetchThread.start();


		Thread weatherSpriteChangerThread = new Thread(new Runnable() {
			public void run() {

				while(!terminateThread){
					if(tick == 5){
						tick = 0;
					}


					if(weather.equals("Raining")){
						currentWeatherSprite = rainSprite[tick++%5];
					}else if(weather.equals("Sunny")){

						currentWeatherSprite = sunSprite;
					}else if(weather.equals("Cloudy")){

						currentWeatherSprite = cloudSprite[tick++%5];
					}else if(weather.equals("Snowing")){

						currentWeatherSprite = snowSprite[tick++%5];
					}
					updateWorld();
					try {
						Thread.sleep(500);
					} catch(Exception e){
						e.printStackTrace();
					}
				}

			}
		});

		weatherSpriteChangerThread.start();

		// 
		establishFTPListeningServer();
		//
		establishPacListeningServer();

		/* GET RUNTIME MEMORY INFO FOR DEBUGGING */
		Runtime runtime = Runtime.getRuntime();
		int mb = 1024 * 1024;
		System.gc();


		System.out.println("Used Memory:"
				+ (runtime.totalMemory() - runtime.freeMemory()) / mb);
		System.out.println("Free mem: " + runtime.freeMemory() / mb);

	}

	public void establishPacListeningServer(){
		Thread tp = new Thread(new PacListener());
		tp.start();
	}
	
	public void establishFTPListeningServer() {

		Thread tt = new Thread(new FTPListener());
		tt.start();
	}

	public void notifyIncomingPac(String fromIP){
		pac = ImageLoader.loadImageFromLocal("images/profed.png");
		GOT_PAC = true;
	}
	
	public static boolean GOT_PAC = false;
	public void notifyIncomingFTP(String fromIP){
		System.out.println(fromIP+" is requesting to send a file.");
		FClient client = new FClient(fromIP, 12345);
		client.showDialog();
	}

	/**
	 * Checks whether if the input point is a legal
	 * delta position. If it is not, the method will
	 * automatically fix and set the deltas
	 * 
	 * @param p Point to validate for delta eligibility 
	 * @param mode 1 - No need to consider up/left directions
	 *        mode 0 - All directions must be considered
	 * 
	 */
	public void fixAndSetDelta(Point p, int mode) {

		int x = p.x;
		int y = p.y;


		if(mode == 0){

			// Y doesn't exceed lower bound
			if( y + ICEWORLD_VIEWPORT_SIZE.height <= World.WORLD_SIZE.height){
				if( y >= 0 ){
					// Y doesn't exceed upper bound
					deltaY = y;
				} else {
					// Y exceeds upper bound
					deltaY = 0;
				}
			}else{
				// Y exceeds Upper bound
				deltaY = World.WORLD_SIZE.height - ICEWORLD_VIEWPORT_SIZE.height;				

			}

			// X doesn't exceed right bound
			if( x + ICEWORLD_VIEWPORT_SIZE.width <= World.WORLD_SIZE.width){

				if( x >= 0 ){
					// X doesn't exceed left bound
					deltaX = x;
				} else {
					// X exceeds left bound
					deltaX = 0;
				}


			}else{

				// X exceeds right bound
				deltaX = World.WORLD_SIZE.width - ICEWORLD_VIEWPORT_SIZE.width;

			}



		}else if (mode == 1){

			// Y doesn't exceed lower bound
			if(y + ICEWORLD_VIEWPORT_SIZE.height <= World.WORLD_SIZE.height){
				deltaY = y;

				// X does not exceed right bound
				if(x + ICEWORLD_VIEWPORT_SIZE.width <= World.WORLD_SIZE.width){
					deltaX = x;
				}else { // X exceeds right bound
					deltaX = World.WORLD_SIZE.width - ICEWORLD_VIEWPORT_SIZE.width;

				}
			} else { // Y exceeds lower bound 
				deltaY = World.WORLD_SIZE.height - ICEWORLD_VIEWPORT_SIZE.height;
				if(x + ICEWORLD_VIEWPORT_SIZE.width <= World.WORLD_SIZE.width){
					deltaX = x;
				}else {
					deltaX = World.WORLD_SIZE.width - ICEWORLD_VIEWPORT_SIZE.width;

				}
			}

		}

	}

	public void zoomChanged(){
		zoomChanged(null);
	}

	public void zoomChanged(Point overrideDelta) {

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

		Point deltaPos = Scaler.toScreenSpace(controllersLocalPosition);


		if(!cameFromZoomMode){

			deltaPos.x -= 450;
			deltaPos.y -= 400;
			fixAndSetDelta(deltaPos,0);

		}else{

			overrideDelta = new Point((int)(overrideDelta.x*7.2),(int)(overrideDelta.y*7.2));
			System.out.println(overrideDelta.toString());
			fixAndSetDelta(overrideDelta,1);

			cameFromZoomMode = false;
		}

		//deltaX = (int)(deltaX*zoom_factor); 
		//deltaY = (int)(deltaY*zoom_factor);

		panner = new Panner(world.getImage());

		/* UPDATE SIZE OF AVATAR IMAGES */
		ali = new Alien();

		for(ICEtizen a : loggedinUsers.values()) {
			a.setIcetizenLook(looksList.get(a.getUsername()));
			avatarList.put(a.getUsername(),a.avatar);
		}

		LoginPage.me.updateAvatarImage();

		updateWorld();
	}


	public void setCustomise(IcetizenLook look){
		this.inh.look.gidB=look.gidB;
		this.inh.look.gidH=look.gidH;
		this.inh.look.gidS=look.gidS;
		this.inh.look.gidW=look.gidW;

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

		////
		if(ZOOM_MODE_ON && zoom_factor < 0.9){

			Point zoomBoxPos = MouseInfo.getPointerInfo().getLocation();

			zoomBoxPos.x -= this.getLocationOnScreen().x;
			zoomBoxPos.y -= this.getLocationOnScreen().y;

			g2.drawImage(zoomBox,zoomBoxPos.x,zoomBoxPos.y,null);
		}

		///



		// Update chat
		updateChat(g2);

		// Show update
		viewport = panner.getWorldViewport();

		Graphics2D g2Viewport = (Graphics2D) viewport.getGraphics();

		if(WEATHER_ON){
			updateWeather(g2Viewport);
		}

		updateYell(g2Viewport);

		g2Viewport.drawImage(minimap.getImage(), ICEWORLD_VIEWPORT_SIZE.width
				- Minimap.MINIMAP_SIZE.width - 10, 10, null);

		showView();
	}


	public BufferedImage currentWeatherSprite;


	public void updateWeather(Graphics2D g2) {

		g2.drawImage(currentWeatherSprite, 0, 0, null);

	}

	public static int tick = 0;

	private void updateChat(Graphics2D g2) {

		for (TalkObject talkImg : talkImageList) {
			Point p = null;
			if(talkImg.username.equals(controllerUsername)){
				p = Scaler.toScreenSpace(controllersLocalPosition);
			}else{
				if(lastKnownPositionList.get(talkImg.username) != null)
					p = Scaler.toScreenSpace(lastKnownPositionList.get(talkImg.username));
			}
			if(p != null)
				g2.drawImage(talkImg.talkImage, p.x - 150, p.y-(int)(130*zoom_factor)- talkImg.talkImage.getHeight(), null);
		}

	}

	/**
	 * This method paints yells on the user's screen
	 * 
	 * @param g viewport Graphics object
	 */
	public void updateYell(Graphics g) {
		for (BufferedImage yellImg : yellImageList) {
			g.drawImage(yellImg, 0, 300, null);
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


		/*
		if (zoom_factor == 1.0) {
			zoomCorrectionYOffset = 0;
			zoomCorrectionXOffset = 0;
		} else if (zoom_factor >= 0.9) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 20);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 5);
		} else if (zoom_factor >= 0.8 && zoom_factor < 0.9) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 5);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 4);
		}else if (zoom_factor >= 0.7 && zoom_factor < 0.8) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 5);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 4);
		} else if (zoom_factor >= 0.6 && zoom_factor < 0.7) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 4);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 3);
		} else if (zoom_factor >= 0.5 && zoom_factor < 0.6) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 3);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 2);
		} else if (zoom_factor >= 0.4 && zoom_factor < 0.5) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 5);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 4); 
		} else if (zoom_factor >= 0.3 && zoom_factor < 0.4) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 5);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 4);
		} else if (zoom_factor >= 0.2 && zoom_factor < 0.3) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 5);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 4);
		} else if (zoom_factor < 0.2) {
			zoomCorrectionYOffset = (int) ((1.0 / zoom_factor) * 2);
			zoomCorrectionXOffset = (int) ((1.0 / zoom_factor) * 1);
			System.out.println("Y offset: "+zoomCorrectionYOffset);
			System.out.println("X offset: "+zoomCorrectionXOffset);
		}
		 */

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

				g2.drawImage((value.getType() == 1) ? avatarList.get(value.getUsername())/*value.avatar*/ : ali.avatar,
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

				if(value.getIcePortID() == 245){
					minimap.drawUser(currentTileSpacePos, 2);

				}else{
					minimap.drawUser(currentTileSpacePos, 1);

				}

			}
		}



		// update the controller user
		// currentTileSpacePos = LoginPage.me.getCurrentPosition();

		currentTileSpacePos = controllersLocalPosition;
		pos = Scaler.toScreenSpace(currentTileSpacePos);

		/*
		 * pos = controllersLocalPosition; System.out.println(pos.toString());
		 */

		g2.drawImage((LoginPage.me.getType() == 1) ? LoginPage.me.avatar : ali.avatar,
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
		if(ZOOM_MODE_ON){
			updateWorld();
		}


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

		// weather resources
		for(int i = 0; i < 5; i++ ){
			rainSprite[i] = ImageLoader.loadImageFromLocal("rain/r"+(i+1)+".png");
			cloudSprite[i] = ImageLoader.loadImageFromLocal("cloud/c"+(i+1)+".png");
			snowSprite[i] = ImageLoader.loadImageFromLocal("snow/s"+(i+1)+".png");
		}

		sunSprite = ImageLoader.loadImageFromLocal("sun/s1.png");

	}


	@Override
	public void mousePressed(MouseEvent e) {

		this.requestFocus(true);

		if(ZOOM_MODE_ON){
			cameFromZoomMode = true;
			zoom_factor = 1.0;
			ZOOM_MODE_ON = false;

			//System.out.println("deltaX = "+deltaX+", deltaY = "+deltaY);
			zoomChanged(e.getPoint());
			return;
		} 

		Point destinationTile = null;
		// check if it is a minimap coordinate
		if(minimap.isMinimapClicked(e)){
			Point panDelta = Scaler.toScreenSpace(minimap.panUser(e.getPoint()));

			int x = panDelta.x;
			int y = panDelta.y;

			/* VALIDATING DELTA CHANGE */
			fixAndSetDelta(panDelta,1);
			/*

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
			 */




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
							if(pos != null){
								if(!world.isFallingIntoTartarus(pos))	{
									lastKnownPositionList.put(user, pos);
									// fetch the looks for the user
									// then add it to the looks list
									IcetizenLook defaultLooks = new IcetizenLook();
									defaultLooks.gidB = "B102";
									defaultLooks.gidH = "H015";
									defaultLooks.gidS = "S019";
									defaultLooks.gidW = "W045";

									itz.setIcetizenLook(defaultLooks);
									avatarList.put(user,itz.avatar);



								}

							}
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

							// remove from the looks list


						}
					}


					loggedinUsers = fetcher.getLoggedinUserMap();


					for(ICEtizen value : loggedinUsers.values()) {
						if(value.getType() == 0){
							continue;
						}

					}

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

						bf = new BufferedImage(900, 300,
								BufferedImage.TYPE_INT_ARGB);

						Graphics2D g2bf = bf.createGraphics();

						g2bf.setColor(Minimap.BLACK_WITH_50_PERCENT_ALPHA);
						g2bf.fillRect(0, 0, 900, 300);

						Font font = new Font("Arial", Font.PLAIN, (act
								.getDetails().length() <= 5) ? 200 : 100);
						g2bf.setFont(font);
						g2bf.setColor(YellingTaskOthers.SKY_BLUE);
						g2bf.drawString(act.getDetails(), 10, (act.getDetails()
								.length() <= 5) ? 230 : 200);

						yellImageList.add(bf);
						new Timer()
						.schedule(new YellingTaskOthers(bf), 5000, 1);

					}

					/* TALKING SCHEDULER */
					//System.out.println("talkList size: "
					//		+ actionFetcher.talkList.size());
					for (Actions act : actionFetcher.talkList) {

						if(act.getUsername() == null)
							continue;
						if (act.getUsername().equals(controllerUsername))
							continue;
						if (act.getDetails() == null
								|| act.getDetails().length() <= 0)
							continue;

						System.out.println("This guy talked! ==>"
								+ act.getUsername());


						String msg = act.getDetails();

						int lineAmount = (int) Math.ceil(msg.length()/25.0);
						System.out.println("lineAmount = "+lineAmount);


						bf = new BufferedImage(310, lineAmount*25,
								BufferedImage.TYPE_INT_ARGB);

						Graphics2D g2bf = bf.createGraphics();
						g2bf.setFont(TalkingTaskOthers.TALKING_FONT);

						// draw a chat bubble
						g2bf.setColor(Minimap.BLACK_WITH_50_PERCENT_ALPHA);
						g2bf.fillRect(0, 0, 310, lineAmount*25);

						g2bf.setColor(YellingTaskOthers.SKY_BLUE);

						if(lineAmount == 1){
							// already formatted
							g2bf.drawString(msg, 5, 22);

						}else if(lineAmount ==2){
							g2bf.drawString(msg.substring(0,25), 5, 24*1);
							g2bf.drawString(msg.substring(25), 5, 24*2);


						}else if(lineAmount ==3){
							g2bf.drawString(msg.substring(0,25), 5, 24*1);
							g2bf.drawString(msg.substring(25,50), 5, 24*2);
							g2bf.drawString(msg.substring(50), 5, 24*3);

						}else if(lineAmount ==4){
							g2bf.drawString(msg.substring(0,25), 5, 24*1);
							g2bf.drawString(msg.substring(25,50), 5, 24*2);
							g2bf.drawString(msg.substring(50,75), 5, 24*3);
							g2bf.drawString(msg.substring(75), 5, 24*4);

						}





						TalkObject to = new TalkObject(bf,act.getUsername());
						talkImageList.add(to);
						new Timer()
						.schedule(new TalkingTaskOthers(to), TALK_VISIBLE_DURATION, 1);

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
		repaint();
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
		super.paintComponent(g);
		g.drawImage(viewport, 0, 0, null);
		
		/*
		Graphics2D g2 =  (Graphics2D) getGraphics();
		if (g2 == null)
			return;
		g2.drawImage(viewport, 0, 0, null);
		
		if(GOT_PAC){
			g2.drawImage(pac,0,0,null);
		}
		*/
	}


	// username is used as a KEY for every HashMaps
	String controllerUsername;

	ActionFetcher actionFetcher;

	public String weather = "sunny";

	Timer timer;

	public HashMap<String, ICEtizen> loggedinUsers;
	HashMap<String, Point> lastKnownPositionList;

	LinkedList<BufferedImage> yellImageList;
	LinkedList<TalkObject> talkImageList;


	public static boolean ZOOM_MODE_ON = false;
	public static boolean WEATHER_ON = true;
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

	public static String RECEIVED_FILES = "/";
	/* PRIVATE */
	private static final long serialVersionUID = 5658988277615488303L;


	/* XY-OFFSET CORRECTION IN ZOOM MODE */
	int zoomCorrectionYOffset = 0;
	int zoomCorrectionXOffset = 0;


	public Thread fetchThread;
	public static boolean terminateThread = false;

	/* IMAGE RESOURCES */
	Inhabitant inh;
	Alien ali;
	Image yellowIndicator, redIndicator;
	BufferedImage zoomBox;

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

	boolean cameFromZoomMode = false;

	BufferedImage[] rainSprite = new BufferedImage[5];
	BufferedImage sunSprite;
	BufferedImage[] cloudSprite = new BufferedImage[5];
	BufferedImage[] snowSprite = new BufferedImage[5];
	BufferedImage pac;

}
