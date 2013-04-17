package gui;

import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Application;

public class SoundControlWindow extends JDialog{
	JDialog sound;
	//JSlider bgm, sfx ;
	JPanel pan;
	JCheckBox mbgm;
	JCheckBox msfx;
	Label bglb, sflb;
	JButton in  , btnApply;
	JLabel bg, sfx;
	//Sound soung
	public SoundControlWindow() {
		createGUI();
		//setPreferredSize(new Dimension(400,300));
		//pack();
	}
	
	public void createGUI(){
		sound = new JDialog();
		
		in = new JButton("+");
		in.setBounds(174, 17, 49, 28);
		InEvent i = new InEvent();
		in.addActionListener(i);
		pan = new JPanel(new GridLayout (3,1));
		
		/*pan = new JPanel(new GridLayout(4,1));
		bgm = new JSlider(SwingConstants.HORIZONTAL, 0, 200, 10);
		bgm.setMajorTickSpacing(10);
		bgm.setPaintTicks(true);
		pan.add(bgm);
		EventAdBgm  adbgm = new EventAdBgm();
		bgm.addChangeListener(adbgm);
		bglb= new Label ("Background Volume");
		pan.add(bglb);
		
		
		
		sfx = new JSlider(SwingConstants.HORIZONTAL, 0, 200, 10);
		sfx.setMajorTickSpacing(10);
		sfx.setPaintTicks(true);
		pan.add(sfx);
		EventAdSfx adsfx = new EventAdSfx();
		sfx.addChangeListener(adsfx);
		sflb = new Label("Sound Effect Volume");
		pan.add(sflb);
		
		mbgm = new JCheckBox();
		msfx = new JCheckBox();
		
		mbgm.setText("Mute Background Music");
		msfx.setText("Mute Sound Effect");
		
		pan.add(mbgm);
		pan.add(msfx);
		
		MuteBEvent mb = new MuteBEvent();
		mbgm.addActionListener(mb);
		
		MuteSEvent ms = new MuteSEvent();
		msfx.addActionListener(ms);

	

		
		// we want the JCheckBox to be checked as the current MUTE_ON state
		/*if(ApplicationMainFrame.MUTE_ON == true){
			msfx = new JCheckBox(); 
			pan.add(msfx);
		} else {

			mbgm = new JCheckBox();
			pan.add(mbgm);
			
		}*/
		
		
		//sound.add(pan);
		JButton button = new JButton("+");
		button.setBounds(174, 17, 49, 28);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage.app.sound.increase();
				System.out.println("+");
				 
		}
			
	});
		
		
		pan.add(button);
		// int sound = (int)(slider.getValue())
		JButton btnApply = new JButton("-");
		btnApply.setBounds(174, 17, 49, 28);
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					LoginPage.app.sound.decrease();
				
					System.out.println("-");
					
			}
		});
		
		
		pan.add(btnApply);
		bg = new JLabel("Adjust Background Music Volume");
		
		
		
		pan.add(bg);
		sound.add(pan);
		sound.setLocationRelativeTo(null);
		sound.pack();
		sound.setVisible(true);
	
		
	}
	
	public class InEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			LoginPage.app.sound.increase();
		}
		
	}
	/*public class MuteBEvent implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JCheckBox mbgm = (JCheckBox) e.getSource();
			if(mbgm.isSelected()){
				mbgm.setSelected(true);
				ApplicationMainFrame.MUTE_ON=true;
			}
		}
	}
	
	public class MuteSEvent implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JCheckBox msfx = (JCheckBox) e.getSource();
			if(msfx.isSelected()){
				msfx.setSelected(true);
				//ApplicationMainFrame.MUTE_ON=true;
			}
		}
	}*/
	
	public class EventAdBgm implements ChangeListener {


		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			JSlider source = (JSlider)(e.getSource());
			float value = (float)source.getValue();
			LoginPage.app.sound.volume(value);
			
		}	
	}
	
	public class EventAdSfx implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)(e.getSource());
			float f = (float)source.getValue();
			//LoginPage.sfx.sound.volume(f);
		}
		
	}
	
	
	
	

}
