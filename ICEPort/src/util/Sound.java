package util;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;

public class Sound // Holds one audio file
{
	static Clip clip;

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
	public static void stopSound()
	{
		clip.stop(); // Stop
	}
	public void playSoundOnce()
	{
		AudioClip audioClip = (AudioClip) clip;
		audioClip.play(); // Play only once
	}
	public static void decrease(float x)//decrease volume
	{
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    gainControl.setValue(-60.0f*x+ gainControl.getValue()); // Reduce volume by 10 decibels.
	   
	}
	public static void increase(float x)//increase volume
	{
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    gainControl.setValue(60.0f*x+ gainControl.getValue()); // increase volume by 10 decibels.
	}
}
