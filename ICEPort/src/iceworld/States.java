package iceworld;

import java.awt.Point;

public class States {

	// ICEPort camera view states
	public static int deltaX;
	public static int deltaY;
	public static int zoom_level;
	
	// active ICEtizen
	public static Point activeUserPosition = new Point(20,10);
	public static Point activeUserLastKnownPosition = new Point(10,9);
	public static Point activeUserDestination = new Point(20,30);
	public static boolean activeUserIsWalking = false;
	

	// TODO: hashmap for list of online users


}
