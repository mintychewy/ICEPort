package iceworld;

import gui.ApplicationMainFrame;
import gui.LoginPage;

import java.awt.Point;
import java.util.TimerTask;

import util.Scaler;

import core.Application;

public class WalkingTask extends TimerTask{
	
	Point userLastKnownPosition;
	Point destPosition;
	public WalkingTask(){
		userLastKnownPosition = new Point();
		destPosition = LoginPage.me.getCurrentPosition();
	}
	
	@Override
	public void run() {
		//System.out.println("Destination: "+LoginPage.me.getCurrentPosition().toString());
		//System.out.println("Position: "+Application.app.view.controllersLocalPosition.toString());
		userLastKnownPosition.x = Application.app.view.controllersLocalPosition.x;
		userLastKnownPosition.y = Application.app.view.controllersLocalPosition.y;
		//States.activeUserLastKnownPosition.x = Application.app.view.controllersLocalPosition.x;
		//States.activeUserLastKnownPosition.y = Application.app.view.controllersLocalPosition.y;
		
		// end walking if destination is reached
		if(destPosition.x == Application.app.view.controllersLocalPosition.x &&
		   destPosition.y == Application.app.view.controllersLocalPosition.y){
			cancel();
		}
		
		
		// move in the x-axis 
		
		if(destPosition.x > Application.app.view.controllersLocalPosition.x){
			// walk left
			Application.app.view.controllersLocalPosition.x += 1;
			

		}else if(destPosition.x < Application.app.view.controllersLocalPosition.x){
			// walk right
			Application.app.view.controllersLocalPosition.x -= 1;
		}else{
			// no need to walk in the x-axis anymore
		}
		
		
		// move the in y-axis
		
		if(destPosition.y > Application.app.view.controllersLocalPosition.y){
			// walk down
			Application.app.view.controllersLocalPosition.y += 1;
		}else if(destPosition.y < Application.app.view.controllersLocalPosition.y){
			// walk up
			Application.app.view.controllersLocalPosition.y -= 1;
		}else{
			// no need to walk in the y-axis anymore
		}
		
		LoginPage.app.view.updateWorld();
	}
}
