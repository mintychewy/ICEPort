package gui;

import iceworld.ChatController;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class SoundControllerWindow implements ActionListener {
	
	JDialog soundControl;
	JSlider BGMSlider,SFXSlider;
	JCheckBox muteBGM;
	public JCheckBox muteSFX;
	ChatController chat;
	public boolean selected;
	public void actionPerformed(ActionEvent e){
		chat=new ChatController();
		 
		System.out.println(selected);
		soundControl=new JDialog();
		soundControl.setLocation(270, 380);
		soundControl.setVisible(true);
		soundControl.setSize(400, 100);
		soundControl.setAlwaysOnTop(true);
		soundControl.setLayout(new GridLayout(2,3,10,10));
		BGMSlider=new JSlider();
		JLabel BGM=new JLabel("BGM:");
		SFXSlider=new JSlider();
		SFXSlider.addChangeListener(new ChatController());
		
		JLabel SFX=new JLabel("SFX:");
		muteBGM=new JCheckBox("mute");
		muteSFX=new JCheckBox("mutesfx");
		
		if(LoginPage.MUTE_ON == true)
			 muteSFX.setSelected(true);
		muteSFX.addItemListener(new ChatController());
		soundControl.add(BGM);
		soundControl.add(BGMSlider);
		soundControl.add(muteBGM);
		soundControl.add(SFX);
		soundControl.add(SFXSlider);
		soundControl.add(muteSFX);

	}

}
