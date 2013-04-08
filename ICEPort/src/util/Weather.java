package util;



import javax.swing.JFrame;


public class Weather {
	static Snow snow;
	static Rain rain;
	static Sun sun;
	static JFrame snowing=new JFrame();
	static JFrame raining=new JFrame();
	static JFrame sunny=new JFrame();
	public static void main(String[] args){
		//snowing
		snow=new Snow();
		snowing.setSize(900,750);
		snowing.setContentPane(snow);
		snowing.setVisible(true);
		
		//raining
		rain=new Rain();
		raining.setSize(900,750);
		raining.setContentPane(rain);
		raining.setVisible(true);
		
		//sunny
		sun=new Sun();
		sunny.setSize(900,750);
		sunny.setContentPane(sun);
		sunny.setVisible(true);
		
		}

}
