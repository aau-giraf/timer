package dk.aau.cs.giraf.TimerLib;

public enum formFactor {undefined, SubProfile, Hourglass, TimeTimer, TimeTimerStandard, ProgressBar, DigitalClock, Timer, SingleImg, SplitImg, Sound1, Sound2, Sound3, Sound4, Sound5, Sound6;

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