package sfx;

public class SFXSound {
	String action;
	Sound effect;
	SFXSound (String action){
		
		this.action = action;
		
		SoundEffectAction soundeffect = new SoundEffectAction();
		
		soundeffect.playEffect(effect, action);
		//play();
		
	}
/*public void play(){
		
		
		if(action == "water"){
			
		
			 effect = new Sound("water.wav");
			
			
		}
	}*/
	
	
		
	
	
	public static void main(String[] args){
		
	// here's how to use and u can use the method from sound class	
		SFXSound a = new SFXSound("water");
		
		
	}
	
	
}
