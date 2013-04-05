package objects;

import java.awt.Dimension;
import java.awt.Point;

public class Entity {
	
	
	// 
	public static final Dimension AVATAR_SIZE = new Dimension(95,120);
	public static int AVATAR_OFFSET_X = 48;
	public static int AVATAR_OFFSET_Y = 115;
	
	
	
	public int userID, type, ICEPortID;
	public String username, IPAddress;
	
	// Positions
	public Point lastKnownIntendedDestination;
	public String destinationSpecifyTimestamp;
	public Point currentPosition;

	// Actions
	public int actionID;
}
