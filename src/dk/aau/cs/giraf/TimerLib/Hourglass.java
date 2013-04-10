package dk.aau.cs.giraf.TimerLib;


public class Hourglass extends SubProfile {
	
	public Hourglass(String name, String description, int bgcolor, int timeLeftColor, int timeSpentColor, int frameColor, int totalTime, boolean changeColor){
		super(name, description, bgcolor, timeLeftColor, timeSpentColor, frameColor, totalTime, changeColor);
	}

	public Hourglass(Hourglass obj){
		super(obj.name, obj.desc,obj.bgcolor, obj.timeLeftColor,obj.timeSpentColor,obj.frameColor,obj.get_totalTime(),obj.gradient);
	}
	
	public Hourglass copy(){
		Hourglass copyP = new Hourglass(this);
		
		copyP.setAttachment(this._attachment);
		copyP.setDoneArt(this.getDoneArt());
		
		return copyP;
	}
	
	public formFactor formType(){
		return formFactor.Hourglass;
	}
}
