package gui;

import iceworld.ICEWorldView;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RefreshRateWindow extends JDialog {

	public RefreshRateWindow() {
		createGUI();
		pack();
	}

	private void createGUI() {
		this.setLayout(new BorderLayout());
		
		JPanel container = new JPanel();

		container.setLayout(new GridLayout(1,2,5,5));
		JLabel label = new JLabel("Set refresh interval (in seconds)");
		container.add(label);
		JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL,
				1, 10, 1);

		framesPerSecond.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)(e.getSource());

				if (!source.getValueIsAdjusting()) {

					ICEWorldView.REFRESH_INTERVAL = ((JSlider) e.getSource()).getValue() * 1000;
					System.out.println(""+ICEWorldView.REFRESH_INTERVAL);
					// kill the existing fetcher thread
					LoginPage.app.view.terminate();
					
					while(LoginPage.app.view.fetchThread.getState() != Thread.State.TERMINATED){
						//wait until thread is terminated
					}
					LoginPage.app.view.createNewFetchingThread();

					LoginPage.app.view.fetchThread.start();
					
				}


			}

		});

		//Turn on labels at major tick marks.
		framesPerSecond.setMajorTickSpacing(2);
		framesPerSecond.setMinorTickSpacing(1);
		framesPerSecond.setPaintTicks(true);
		framesPerSecond.setPaintLabels(true);

		container.add(framesPerSecond);
		this.add(container);
	}

}
