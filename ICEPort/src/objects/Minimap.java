package objects;

import iceworld.States;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import util.Scaler;

public class Minimap {

	// private
	private BufferedImage mapImage;
	private final int POSITION_DOT_STROKE_SIZE = 14;
	private final int POSITION_DOT_SIZE = 10;
	private final Color BLACK_WITH_50_PERCENT_ALPHA = new Color(0f,0f,0f,0.5f);
	
	// public static 
	public final static Dimension MINIMAP_SIZE = new Dimension(270,180);
	public final static double MINIMAP_SCALE_FACTOR = (double)MINIMAP_SIZE.height/Map.WORLD_SIZE.height;
		
	public Minimap(){
		renderMap();
		updateMiniMap();
	}

	public BufferedImage getImage(){
		return mapImage;
	}

	public void setImage(Image mapImage){
		this.mapImage = (BufferedImage) mapImage;
	}

	public void renderMap(){
		mapImage = new BufferedImage(MINIMAP_SIZE.width, MINIMAP_SIZE.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mapImage.createGraphics();	

		g.setColor(BLACK_WITH_50_PERCENT_ALPHA);
		g.fillRect(0, 0, MINIMAP_SIZE.width, MINIMAP_SIZE.height);
	}

	public void updateMiniMap(){

		// clears the map
		renderMap();

		Graphics2D g= mapImage.createGraphics();
		// Active ICEtizen's position dot 
		Point activeUserPos = Scaler.toMiniMapPoint(Scaler.toScreenSpace(new Point(20,10)));

		g.setColor(Color.WHITE);
		g.fillOval(activeUserPos.x- POSITION_DOT_STROKE_SIZE/2, activeUserPos.y- POSITION_DOT_STROKE_SIZE/2, POSITION_DOT_STROKE_SIZE, POSITION_DOT_STROKE_SIZE);

		g.setColor(Color.RED);
		g.fillOval(activeUserPos.x- POSITION_DOT_SIZE/2, activeUserPos.y- POSITION_DOT_SIZE/2, POSITION_DOT_SIZE,  POSITION_DOT_SIZE);

		// Other ICETizen's position dots

	}
	
	public boolean isMinimapClicked(MouseEvent e){
		//
		return false;
	}

}
