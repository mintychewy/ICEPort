package iceworld;
import gui.LoginPage;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class ZoomInAction extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ActionPerformed");
		if(ICEWorldView.zoom_factor == 1) return;
		if(ICEWorldView.zoom_factor+0.3 <= 1.0)
			ICEWorldView.zoom_factor += 0.3;
		else
			ICEWorldView.zoom_factor = 1.0;
	
		LoginPage.app.view.zoomChanged();
	}

}
