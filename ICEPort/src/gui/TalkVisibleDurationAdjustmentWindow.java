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

public class TalkVisibleDurationAdjustmentWindow extends JDialog {

	
	public TalkVisibleDurationAdjustmentWindow() {
		createGUI();
		pack();
	}

	private void createGUI() {
		this.setLayout(new BorderLayout());
		
		JPanel container = new JPanel();

		container.setLayout(new GridLayout(1,2,5,5));
		JLabel label = new JLabel("Set chat bubble visible duration (in seconds)");
		container.add(label);
		JSlider slider = new JSlider(JSlider.HORIZONTAL,
				1, 10, 1);

		slider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)(e.getSource());

				if (!source.getValueIsAdjusting()) {

					ICEWorldView.TALK_VISIBLE_DURATION = ((JSlider) e.getSource()).getValue() * 1000;
					System.out.println("TALK_VISIBLE_DURATION updated to: "+ICEWorldView.TALK_VISIBLE_DURATION);
				}


			}

		});

		//Turn on labels at major tick marks.
		slider.setMajorTickSpacing(2);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		container.add(slider);
		this.add(container);
	}
	
}
