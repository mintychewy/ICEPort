package iceworld;

import java.awt.Dimension;

public class Constants {
	
	public final static Dimension ICEWORLD_VIEWPORT_SIZE = new Dimension(900,600);
	public static Dimension AVATAR_SIZE = new Dimension(95,120);
	
	public final static Dimension WORLD_SIZE = new Dimension(6480,4320);
	
	// MiniMap
	public final static Dimension MINIMAP_SIZE = new Dimension(270,180);
	public final static double MINIMAP_SCALE_FACTOR = (double)MINIMAP_SIZE.height/WORLD_SIZE.height;
	
	public static final int NUM_TILES = 100;
	// must be modifiable at any time (for zooming)
	public static int TILE_HEIGHT = 32;
	public static int TILE_WIDTH = 64;
	
	// NUM_TILES *+ (Arbitrary offset multiplier/adders)
	public static final int OFFSET_FROM_ORIGIN_X = NUM_TILES * 32 + 25; 
	public static final int OFFSET_FROM_ORIGIN_Y = NUM_TILES + 350; 
	
	
	// Avatar positioning constants
	public static int AVATAR_OFFSET_X = 48;
	public static int AVATAR_OFFSET_Y = 115;
	// Panning
	public static final int DELTA_INC = 300;
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
}
