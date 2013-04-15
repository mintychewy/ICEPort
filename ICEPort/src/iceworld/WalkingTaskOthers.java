package iceworld;

import gui.ApplicationMainFrame;
import gui.LoginPage;

import java.awt.Point;
import java.util.TimerTask;

import util.Scaler;

import core.Application;

public class WalkingTaskOthers extends TimerTask{
	
	String key; 
	Point destPosition;
	Point beforeLastKnownPosition;
	public WalkingTaskOthers(String user, Point lastKnownPosition, Point dest){
		beforeLastKnownPosition = new Point();
		beforeLastKnownPosition.x = lastKnownPosition.x;
		beforeLastKnownPosition.y = lastKnownPosition.y;
		destPosition = dest;
		key = user;
	}
	
	@Override
	public void run() {
		
		// LoginPage.app.view.lastKnownPositionList.get(key).x 
		
		//System.out.println("Destination: "+LoginPage.me.getCurrentPosition().toString());
		//System.out.println("Position: "+LoginPage.app.view.lastKnownPositionList.get(key).toString());
		
		if(!LoginPage.app.view.lastKnownPositionList.containsKey(key)){
			cancel();
		} else {
			
		
		beforeLastKnownPosition.x = LoginPage.app.view.lastKnownPositionList.get(key).x;
		beforeLastKnownPosition.y = LoginPage.app.view.lastKnownPositionList.get(key).y;
		//States.activebeforeLastKnownPosition.x = LoginPage.app.view.lastKnownPositionList.get(key).x;
		//States.activebeforeLastKnownPosition.y = LoginPage.app.view.lastKnownPositionList.get(key).y;
		
		// end walking if destination is reached
		if(destPosition.x == LoginPage.app.view.lastKnownPositionList.get(key).x &&
		   destPosition.y == LoginPage.app.view.lastKnownPositionList.get(key).y){
			cancel();
		}
		
		
		// move in the x-axis 
		
		if(destPosition.x > LoginPage.app.view.lastKnownPositionList.get(key).x){
			// walk left
			LoginPage.app.view.lastKnownPositionList.get(key).x += 1;
			

		}else if(destPosition.x < LoginPage.app.view.lastKnownPositionList.get(key).x){
			// walk right
			LoginPage.app.view.lastKnownPositionList.get(key).x -= 1;
		}else{
			// no need to walk in the x-axis anymore
		}
		
		
		// move the in y-axis
		
		if(destPosition.y > LoginPage.app.view.lastKnownPositionList.get(key).y){
			// walk down
			LoginPage.app.view.lastKnownPositionList.get(key).y += 1;
		}else if(destPosition.y < LoginPage.app.view.lastKnownPositionList.get(key).y){
			// walk up
			LoginPage.app.view.lastKnownPositionList.get(key).y -= 1;
		}else{
			// no need to walk in the y-axis anymore
		}
		}
		
		ApplicationMainFrame.view.updateWorld();
	}
}
