package sfx;
import gui.ApplicationMainFrame;

import java.applet.AudioClip;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;

public class SoundFX // Holds one audio file
{
	public Clip clip;
	public AudioClip audioClip;
 public	SoundFX(String filename)
	{	        
	        try {
		     
		        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(ClassLoader.getSystemClassLoader().getResourceAsStream(filename));
		        
				clip = AudioSystem.getClip();
				
				 FloatControl gcTemp = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				
				 gcTemp.setValue(ApplicationMainFrame.SFX_VOL);
			
		        clip.open(audioInputStream);
		        
		        
		        clip.start();
		        //audioClip = (AudioClip) clip;

			} catch (LineUnavailableException e) {
				
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void playSound()
	{
		clip.loop(clip.LOOP_CONTINUOUSLY); // Play continuously
	}
	public void stopSound()
	{
		clip.stop(); // Stop
	}
	public void playSoundOnce()
	{
		audioClip.play(); // Play only once
	}
	public void volume(float a)
	{
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    System.out.println("This is the minimum gain "+gainControl.getMinimum());
	    System.out.println("This is the maximum gain "+gainControl.getMaximum());
	    gainControl.setValue(a); 
	}
	public void increase()//increase volume
	{
		FloatControl gainControl = (FloatControl) 	clip.getControl(FloatControl.Type.MASTER_GAIN);
		 float a = gainControl.getValue();   
		
		 if(a+1f<=6.0205){
		 gainControl.setValue(a+1f);}
		 else{ gainControl.setValue(6);
		 
		 }
		   
	  }
	public void decrease()//decrease volume
	{	
	
	    FloatControl gainControl = (FloatControl) 	clip.getControl(FloatControl.Type.MASTER_GAIN);
	    float a = gainControl.getValue(); 
	 gainControl.setValue(a-1f); // Reduce volume by 5 decibels.
	   
	}

	
	public void mute()
	{
		BooleanControl bc = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
				if (bc != null) {
				    bc.setValue(true); // true to mute the line, false to unmute
				}
	}
}
