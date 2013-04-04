package objects;

import iceworld.Constants;
import iceworld.States;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import util.Scaler;

public class MiniMap {
	public BufferedImage mapImage;
	
	public MiniMap(){
		renderMap();
		updateMiniMap();
	}
	
	public void renderMap(){
		mapImage = new BufferedImage(Constants.MINIMAP_SIZE.width, Constants.MINIMAP_SIZE.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = mapImage.createGraphics();	
		
		g.setColor(new Color(0f,0f,0f,0.5f));
		g.fillRect(0, 0, Constants.MINIMAP_SIZE.width, Constants.MINIMAP_SIZE.height);
	}
	
	public void updateMiniMap(){
		renderMap();
		// Active ICEtizen's position dot 
		Point activeUserPos = Scaler.toMiniMapPoint(Scaler.toScreenSpace(States.activeUserPosition));
		Graphics2D g= mapImage.createGraphics();
		g.setColor(Color.WHITE);

		
		g.fillOval(activeUserPos.x-7, activeUserPos.y-7, 14, 14);

		
		g.setColor(Color.RED);
		
		
		g.fillOval(activeUserPos.x-5, activeUserPos.y-5, 10, 10);
	}
	
}
