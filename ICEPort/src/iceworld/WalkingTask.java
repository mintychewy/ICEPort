package iceworld;

import gui.ApplicationMainFrame;
import gui.LoginPage;

import java.awt.Point;
import java.util.TimerTask;

import core.Application;

public class WalkingTask extends TimerTask{
	
	Point userLastKnownPosition;
	
	public WalkingTask(){
		userLastKnownPosition = new Point();
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
		if(LoginPage.me.getCurrentPosition().x == Application.app.view.controllersLocalPosition.x &&
		   LoginPage.me.getCurrentPosition().y == Application.app.view.controllersLocalPosition.y){
			System.out.println("Destination Reached");
			cancel();
		}
		
		
		// move in the x-axis 
		
		if(LoginPage.me.getCurrentPosition().x > Application.app.view.controllersLocalPosition.x){
			// walk left
			Application.app.view.controllersLocalPosition.x += 1;
			

		}else if(LoginPage.me.getCurrentPosition().x < Application.app.view.controllersLocalPosition.x){
			// walk right
			Application.app.view.controllersLocalPosition.x -= 1;
		}else{
			// no need to walk in the x-axis anymore
		}
		
		
		// move the in y-axis
		
		if(LoginPage.me.getCurrentPosition().y > Application.app.view.controllersLocalPosition.y){
			// walk down
			Application.app.view.controllersLocalPosition.y += 1;
		}else if(LoginPage.me.getCurrentPosition().y < Application.app.view.controllersLocalPosition.y){
			// walk up
			Application.app.view.controllersLocalPosition.y -= 1;
		}else{
			// no need to walk in the y-axis anymore
		}
		
		
		ApplicationMainFrame.view.updateWorld();
	}
}
