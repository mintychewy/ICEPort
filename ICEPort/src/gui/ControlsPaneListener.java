package gui;

import iceworld.ICEWorldView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import sfx.Sound;

import core.Application;

public class ControlsPaneListener implements ActionListener {
Sound sound;
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton btn = (AbstractButton) e.getSource();
		if(btn.getText().equals("Logout")){
			if (LoginPage.immigration.logout()){
				System.out.println("Logout OK");
				LoginPage.app.view.terminate();
				LoginPage.app.dispose();
				sound=new Sound("music/scream.wav");
				
			
				Application.login.setVisible(true);
				
			}
		}else if(btn.getText().equals("Zoom Out")){
			if(ICEWorldView.zoom_factor-0.3 >= 0.1388889)
				ICEWorldView.zoom_factor -= 0.3;
			else
				ICEWorldView.zoom_factor = 0.13888889;
		
			System.out.println("zoom_factor: "+ICEWorldView.zoom_factor);
			LoginPage.app.view.zoomChanged();
			LoginPage.app.view.requestFocus();
		}else if(btn.getText().equals("Zoom In")){
			
			if(ICEWorldView.zoom_factor == 1) return;
			if(ICEWorldView.zoom_factor+0.3 <= 1.0)
				ICEWorldView.zoom_factor += 0.3;
			else
				ICEWorldView.zoom_factor = 1.0;
		
			LoginPage.app.view.zoomChanged();
			LoginPage.app.view.requestFocus();
		}else if(btn.getText().equals("Specify Zoom")){
			ZoomAdjustmentWindow zoomAdjustmentWindow = new ZoomAdjustmentWindow();
		}else if(btn.getText().equals("Zoom-to-Area")){
			LoginPage.app.view.ZOOM_MODE_ON = true;
			System.out.println("ZOOM_MODE_ON = "+LoginPage.app.view.ZOOM_MODE_ON);
			
			if(LoginPage.app.view.zoom_factor != 0.13888889){
				LoginPage.app.view.zoom_factor = 0.13888889;
				LoginPage.app.view.zoomChanged();
			}
			LoginPage.app.view.requestFocus(true);
		}
	}
	
}
