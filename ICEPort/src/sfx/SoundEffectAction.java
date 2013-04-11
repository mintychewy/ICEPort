package sfx;
//this is where you'll add the sound and it's action

public class SoundEffectAction{
	Sound effect;
	String action;
	public void playEffect(Sound effect, String action){
		this.effect = effect;
		this.action = action;
		play();
	}
	public void play(){
		
		
		if(action == "water"){
			
		
			effect = new Sound("water.wav");
		
			
		}
	}
}
	