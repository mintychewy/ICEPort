package iceworld;

import gui.LoginPage;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ZoomOutAction extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent e) {

		if(ICEWorldView.zoom_factor == 1) return;
		if(ICEWorldView.zoom_factor+0.3 <= 1.0)
			ICEWorldView.zoom_factor += 0.3;
		else
			ICEWorldView.zoom_factor = 1.0;
	
		LoginPage.app.view.zoomChanged();
	}

}
