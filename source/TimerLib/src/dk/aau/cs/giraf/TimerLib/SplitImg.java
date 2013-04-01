package dk.aau.cs.giraf.TimerLib;

import java.util.HashMap;

import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Media;

public class SplitImg extends Attachment {
	
	Guardian guard = Guardian.getInstance();
	Helper help = new Helper(guard.m_context);
	
	private Art _leftImg;
	private Art _rightImg;
	
	public SplitImg(Art left, Art right){
		this._leftImg = left;
		this._rightImg = right;
	}
	
	public Attachment getAttachment(){
		return this;
	}
	
	public formFactor getForm(){
		return formFactor.SplitImg;
	}
	
	public Art getLeftImg(){
		return this._leftImg;
	}
	
	public Art getRightImg(){
		return this._rightImg;
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
		map.put("_gradient", String.valueOf(-1));
		
		//SingleImg
		map.put("singleImgId", String.valueOf(-1));
		
		//SplitImg
		map.put("leftImgId", String.valueOf(this._leftImg.getId()));
		map.put("rightImgId", String.valueOf(this._rightImg.getId()));
		
		return map;
	}
}
