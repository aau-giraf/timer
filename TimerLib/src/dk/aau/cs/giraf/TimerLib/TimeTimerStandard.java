package dk.aau.cs.giraf.TimerLib;


public class TimeTimerStandard extends SubProfile {

	public TimeTimerStandard(String name, String description, int bgcolor, int timeLeftColor, int timeSpentColor, int frameColor, int totalTime, boolean changeColor){
		super(name, description, bgcolor, timeLeftColor, timeSpentColor, frameColor, totalTime, changeColor);
	}

	public TimeTimerStandard(TimeTimerStandard obj){
		super(obj.name, obj.desc,obj.bgcolor, obj.timeLeftColor,obj.timeSpentColor,obj.frameColor,obj.get_totalTime(),obj.gradient);
	}
	
	public TimeTimerStandard copy(){
		TimeTimerStandard copyP = new TimeTimerStandard(this);
		copyP.setAttachment(this._attachment);
		copyP.setDoneArt(this.getDoneArt());
		return copyP;
	}
	
	public formFactor formType(){
		return formFactor.TimeTimerStandard;
	}
}
