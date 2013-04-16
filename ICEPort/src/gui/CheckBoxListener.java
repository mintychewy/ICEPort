package gui;

import iceworld.ICEWorldView;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

public class CheckBoxListener implements ItemListener {

	@Override
	public void itemStateChanged(ItemEvent e) {
		boolean isSelected = ((JCheckBox)e.getSource()).isSelected();
		if(isSelected){
			ICEWorldView.WEATHER_ON = true;
		} else {
			ICEWorldView.WEATHER_ON = false;
		}
	}

}
