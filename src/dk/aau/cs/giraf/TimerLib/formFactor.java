package dk.aau.cs.giraf.TimerLib;

public enum formFactor {undefined, SubProfile, Hourglass, TimeTimer, ProgressBar, DigitalClock, Timer, SingleImg, SplitImg;

static formFactor convert(Object input){
	
	formFactor form = SubProfile;
	
	String value = String.valueOf(input);
	
	for(formFactor val : formFactor.values()){
		if(value.equals(val.toString())){
			form = val;
		}
	}
	
	
	return form;
}

}