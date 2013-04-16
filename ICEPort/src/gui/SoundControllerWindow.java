package gui;
import util.Sound;
import iceworld.ChatController;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.FloatControl;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SoundControllerWindow implements ActionListener{
	JSlider BGMSlider,SFXSlider;
	JDialog soundControl;
	JCheckBox muteBGM,muteSFX;
	boolean selected;
	public int newValue,oldValue,newVolume,oldVolume;
	int value;
	JButton x,y;
	
	public void actionPerformed(ActionEvent e) {
		//when sound button is clicked
		
		soundControl=new JDialog();
		soundControl.setLayout(new GridLayout(2,3,10,10));
		soundControl.setLocation(270, 380);
		soundControl.setVisible(true);
		soundControl.setSize(300, 120);
		soundControl.setAlwaysOnTop(true);
		BGMSlider=new JSlider();
		BGMSlider.setPaintTicks(true);
		BGMSlider.setMajorTickSpacing(10);
		
		
		oldValue=BGMSlider.getValue();
		oldVolume=SFXSlider.getValue();
		JLabel BGM=new JLabel("BGM:");
		SFXSlider=new JSlider();
		SFXSlider.setPaintTicks(true);
		SFXSlider.setMajorTickSpacing(10);
		JLabel SFX=new JLabel("SFX:");
		muteBGM=new JCheckBox("mute");
		muteBGM.setSelected(selected);
		muteSFX=new JCheckBox("mute");
	//	BGM.setBounds(20, 20, 50, 20);
	//	SFX.setBounds(20, 60, 50, 20);
	//	BGMSlider.setBounds(100, 20, 100, 20);
	//	SFXSlider.setBounds(100, 60, 100, 20);
	//	muteBGM.setBounds(200, 20, 50, 20);
	//	muteSFX.setBounds(200, 50, 50, 20);
		soundControl.add(BGM);
		soundControl.add(BGMSlider);
		soundControl.add(muteBGM);
		soundControl.add(SFX);
		soundControl.add(SFXSlider);
		soundControl.add(muteSFX);
if(LoginPage.MUTE_ON == true)
			 muteSFX.setSelected(true);
		muteSFX.addItemListener(new ChatController());
	
		muteBGM.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(muteBGM.isSelected()){
				Sound.stopSound();
				selected=true;
				}
				else if(!muteBGM.isSelected()){
					new Sound("music/grooving.wav");
					selected=false;
				}
			}
		});
		
		BGMSlider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				JSlider source = (JSlider) e.getSource();
				  if (!source.getValueIsAdjusting()) {
		           newValue=(int)source.getValue();
		          if(newValue>oldValue){
		        	  float x=newValue-oldValue;
		        	  Sound.increase(x/100);
		          }
		          else{
		        	  float x=oldValue-newValue;
		        	  Sound.decrease(x/100);
		          }
		           
				  }  
				
			}
			
		});
		
		
	}

}
