package dk.aau.cs.giraf.TimerLib;

import java.util.HashMap;

public class Attachment {
	
	Attachment(){
		
	}
	
	public Attachment getAttachment(){
		return this;
	}
	
	public formFactor getForm(){
		return formFactor.undefined;
	}
	
	//May only be used on Timer
	void changeTime(int t){
		
	}
	
	public Art getImg(){
		return null;
	}
	
	public Art getLeftImg(){
		return null;
	}
	
	public Art getRightImg(){
		return null;
	}
	
	public SubProfile genSub(){
		SubProfile asd = null;
		return asd;
	}
	
	public int getColor(){
		return 0;
	}
	
	public Attachment generateAttArt(HashMap map){
		Attachment att = null;
		
		return att;
	}
	
	public HashMap getHashMap(HashMap map){
		//Defines what kind of attachment it is
		map.put("AttachmentForm", String.valueOf(this.getForm()));
		
		//Timer
		map.put("timerForm", String.valueOf(this.getForm()));
		map.put("_bgColor", String.valueOf(-1));
		map.put("_frameColor", String.valueOf(-1));
		map.put("_timeLeftColor", String.valueOf(-1));
		map.put("_timeSpentColor", String.valueOf(-1));
		map.put("_gradient", String.valueOf(false));
		
		//SingleImg
		map.put("singleImgId", String.valueOf(-1));
		
		//SplitImg
		map.put("leftImgId", String.valueOf(-1));
		map.put("rightImgId", String.valueOf(-1));
		
		return map;
	}
}
