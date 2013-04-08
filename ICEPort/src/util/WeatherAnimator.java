package util;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class WeatherAnimator {
	
	static String currentState;
	 static Snow snow;
	 static Rain rain;
	 static Sun sun;
	 //static JPanel weather=new JPanel();
	// static JFrame snowing=new JFrame();
	// static JFrame raining=new JFrame();
	 //static JFrame sunny=new JFrame();
	 
	 	public WeatherAnimator(String currentState){
	 		this.currentState=currentState;
	 	}
		public static JPanel getWeatherPanel(){
			if(currentState.equals("Snowing")){
			//snowing
			return new Snow();
		
			}else if(currentState.equals("Raining")){
			//raining
			return new Rain();
			
			}else if(currentState.equals("Sunny")){
			//sunny
			return new Sun();
			
			}else{
			return new Rain();
				
			}
			
			}
	
	

	

}
