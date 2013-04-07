package iceworld;

import gui.ApplicationMainFrame;

import java.util.TimerTask;

public class WalkingTask extends TimerTask{
	
	@Override
	public void run() {
		

		if(States.activeUserDestination.x > States.currentPost.x){
			// walk left
			States.currentPost.x += 1;
		}else if(States.activeUserDestination.x < States.currentPost.x){
			// walk right
			States.currentPost.x -= 1;
		}else{
			// no need to walk in the x-axis anymore
		}
		
		if(States.activeUserDestination.y > States.currentPost.y){
			// walk down
			States.currentPost.y += 1;
		}else if(States.activeUserDestination.y < States.currentPost.y){
			// walk up
			States.currentPost.y -= 1;
		}else{
			// no need to walk in the y-axis anymore
		}
		
		States.activeUserLastKnownPosition = States.currentPost;
	
		ApplicationMainFrame.view.updateWorld();
	}
}
