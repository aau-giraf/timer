package dk.aau.cs.giraf.TimerLib;

public class DigitalClock extends SubProfile {
/**
 * Default constructor
 * @param name Name of the digital clock
 * @param description You can choose to put a description, but it will be replaced by auto generated description anyway.
 * @param size How big you want the clock to be.
 * @param bgcolor Background color
 * @param timeLeftColor time left color
 * @param timeSpentColor time spent color
 * @param frameColor frame color
 * @param totalTime Total time
 * @param changeColor Whatever you want the time to change color as gradient
 */
	public DigitalClock(String name, String description, int bgcolor, int timeLeftColor, int timeSpentColor, int frameColor, int totalTime, boolean changeColor){
		super(name, description, bgcolor, timeLeftColor, timeSpentColor, frameColor,totalTime,changeColor);
	}
	/**
	 * Copy constructor
	 * @param obj DigitalClock you wish to copy
	 */
	public DigitalClock(DigitalClock obj){
		super(obj.name, obj.desc,obj.bgcolor, obj.timeLeftColor,obj.timeSpentColor,obj.frameColor,obj.get_totalTime(),obj.gradient);
	}
	
	/**
	 * Copy method
	 */
	public DigitalClock copy(){
		DigitalClock copyP = new DigitalClock(this);
		copyP.setAttachment(this._attachment);
		copyP.setDoneArt(this.getDoneArt());
		return copyP;
	}	
	
	/**
	 * Used to check what type the object is.
	 */
	public formFactor formType(){
		return formFactor.DigitalClock;
	}
	
}
