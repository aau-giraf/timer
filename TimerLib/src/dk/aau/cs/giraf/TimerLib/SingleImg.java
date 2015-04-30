package dk.aau.cs.giraf.TimerLib;

import android.graphics.Bitmap;

import java.util.HashMap;

import dk.aau.cs.giraf.dblib.Helper;

public class SingleImg extends Attachment {
	
	Guardian guard = Guardian.getInstance();
	Helper help = new Helper(guard.m_context);
	
	private Bitmap _art = null;
	
	public SingleImg(Bitmap art){
		this._art = art;
	}
	
	public Attachment getAttachment(){
		return this;
	}
	
	public formFactor getForm(){
		return formFactor.SingleImg;
	}
	
	public Bitmap getImg(){
		return this._art;
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
		//map.put("singleImgId", String.valueOf(this._art.getId()));
		
		//SplitImg
//		map.put("leftImgId", String.valueOf(-1));
//		map.put("rightImgId", String.valueOf(-1));
		
		return map;
	}

}
