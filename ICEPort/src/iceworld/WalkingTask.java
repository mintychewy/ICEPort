package iceworld;

import gui.ApplicationMainFrame;

import java.awt.Point;
import java.util.TimerTask;

public class WalkingTask extends TimerTask{
	
	Point userLastKnownPosition;
	
	public WalkingTask(){
		userLastKnownPosition = new Point();
	}
	
	@Override
	public void run() {
		
		States.activeUserLastKnownPosition.x = States.currentPost.x;
		States.activeUserLastKnownPosition.y = States.currentPost.y;
		
		//System.out.println("CurrentPosition: "+States.currentPost.toString());

		// end walking if destination is reached
		if(States.activeUserDestination.x == States.currentPost.x &&
		   States.activeUserDestination.y == States.currentPost.y){
			System.out.println("Destination Reached");
			cancel();
		}
		
		
		// move in the x-axis 
		
		if(States.activeUserDestination.x > States.currentPost.x){
			// walk left
			States.currentPost.x += 1;
			

		}else if(States.activeUserDestination.x < States.currentPost.x){
			// walk right
			States.currentPost.x -= 1;
		}else{
			// no need to walk in the x-axis anymore
		}
		
		
		// move the in y-axis
		
		if(States.activeUserDestination.y > States.currentPost.y){
			// walk down
			States.currentPost.y += 1;
		}else if(States.activeUserDestination.y < States.currentPost.y){
			// walk up
			States.currentPost.y -= 1;
		}else{
			// no need to walk in the y-axis anymore
		}
		
		
		ApplicationMainFrame.view.updateWorld();
	}
}
