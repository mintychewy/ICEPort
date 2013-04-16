package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecretListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		NameChoosePac ncpWindow = new NameChoosePac();
		ncpWindow.setVisible(true);
	}

}
