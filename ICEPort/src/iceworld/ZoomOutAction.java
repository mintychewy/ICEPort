
package iceworld;

import gui.LoginPage;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ZoomOutAction extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent e) {

		if(ICEWorldView.zoom_factor-0.3 >= 0.1388889)
			ICEWorldView.zoom_factor -= 0.3;
		else
			ICEWorldView.zoom_factor = 0.13888889;
	
		LoginPage.app.view.zoomChanged();
	}

}
