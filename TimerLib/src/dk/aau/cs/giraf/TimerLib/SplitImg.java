package dk.aau.cs.giraf.TimerLib;

import android.graphics.Bitmap;

import java.util.HashMap;

import dk.aau.cs.giraf.dblib.Helper;

public class SplitImg extends Attachment {
	
	Guardian guard = Guardian.getInstance();
	Helper help = new Helper(guard.m_context);
	
	private Bitmap _leftImg;
	private Bitmap _rightImg;
	
	public SplitImg(Bitmap left, Bitmap right){
		this._leftImg = left;
		this._rightImg = right;
	}
	
	public Attachment getAttachment(){
		return this;
	}
	
	public formFactor getForm(){
		return formFactor.SplitImg;
	}
	
	public Bitmap getLeftImg(){
		return this._leftImg;
	}
	
	public Bitmap getRightImg(){
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
//		map.put("singleImgId", String.valueOf(-1));
//
//		//SplitImg
//		map.put("leftImgId", String.valueOf(this._leftImg.getId()));
//		map.put("rightImgId", String.valueOf(this._rightImg.getId()));
		
		return map;
	}
}
