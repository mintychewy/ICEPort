package objects;

import iceworld.ICEWorldView;

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
	public static final Color BLACK_WITH_50_PERCENT_ALPHA = new Color(0f,0f,0f,0.5f);
	//public static final Color BLACK_WITH_50_PERCENT_ALPHA = new Color(0f,0f,0f);

	// public static 
	public final static Dimension MINIMAP_SIZE = new Dimension(270,180);
	public final static double MINIMAP_SCALE_FACTOR = (double)MINIMAP_SIZE.height/World.WORLD_SIZE.height;

	public Minimap(){
		renderMap();
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

		g.setColor(Color.black);
		g.fillRect(0, 0, MINIMAP_SIZE.width, MINIMAP_SIZE.height);
		g.setColor(Color.DARK_GRAY);
		
		Point p = Scaler.toTileSpace(new Point(ICEWorldView.deltaX, ICEWorldView.deltaY));
		p = Scaler.toMiniMapPoint(p, 1);
		g.fillRect(p.x, p.y, 37,25);
	}


	public Point panUser(Point pos) {

		int leftBound = 900 - MINIMAP_SIZE.width - 10;
		int rightBound = 900 - 10;
		int topBound = 10;
		int bottomBound = MINIMAP_SIZE.height + 10;
		
		int x = (int)((pos.x - leftBound)*24*ICEWorldView.zoom_factor);
		int y = (int)((pos.y - topBound)*24*ICEWorldView.zoom_factor);
		
		System.out.println(Scaler.toTileSpace(new Point(x,y)));
		System.out.println(x+","+y);
		
		return Scaler.toTileSpace(new Point(x,y));
	}
	
	public void drawUser(Point pos, int mode){
		//renderMap();


		Point drawPos = Scaler.toMiniMapPoint(pos,1);

		Graphics2D g= mapImage.createGraphics();

		// isSelf
		if(mode == 0){
			g.setColor(Color.WHITE);
			g.fillOval(drawPos.x- POSITION_DOT_STROKE_SIZE/2, drawPos.y- POSITION_DOT_STROKE_SIZE/2, POSITION_DOT_STROKE_SIZE, POSITION_DOT_STROKE_SIZE);
		}
		
		if(mode == 2){
			g.setColor(Color.YELLOW);

		} else {
			g.setColor(Color.RED);
		}
		
		g.fillOval(drawPos.x- POSITION_DOT_SIZE/2, drawPos.y- POSITION_DOT_SIZE/2, POSITION_DOT_SIZE,  POSITION_DOT_SIZE);

	
	}


	public boolean isMinimapClicked(MouseEvent e){

		int leftBound = 900 - MINIMAP_SIZE.width - 10;
		int rightBound = 900 - 10;
		int topBound = 10;
		int bottomBound = MINIMAP_SIZE.height + 10;
				
		int x = e.getX();
		int y = e.getY();
		
		if( x > leftBound && x < rightBound && y > topBound && y < bottomBound)
			return true;
		
		return false;
	}

}
