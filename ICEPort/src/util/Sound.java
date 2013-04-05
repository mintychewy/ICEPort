package util;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;

public class Sound // Holds one audio file
{
	Clip clip;

	public Sound(String filename)
	{	        
	        try {
		        URL url = this.getClass().getClassLoader().getResource(filename);
		        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip();
		        clip.open(audioInputStream);
		        clip.start();
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
		AudioClip audioClip = (AudioClip) clip;
		audioClip.loop(); // Play continuously
	}
	public void stopSound()
	{
		clip.stop(); // Stop
	}
	public void playSoundOnce()
	{
		AudioClip audioClip = (AudioClip) clip;
		audioClip.play(); // Play only once
	}
	public void decrease()//decrease volume
	{
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    gainControl.setValue(10.0f); // Reduce volume by 10 decibels.
	}
	public void increase()//increase volume
	{
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
	}
}
