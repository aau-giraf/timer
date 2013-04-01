package dk.aau.cs.giraf.TimerLib;

import java.util.HashMap;

public class SubProfile implements Comparable<SubProfile>{
	
	Guardian guard = Guardian.getInstance();
	private long _id = -1;
	public String name = "Default";
	public String desc = "Default desc";
	public int bgcolor = 0xffffffff;
	public int timeLeftColor = 0xffffffff;
	public int timeSpentColor = 0xffffffff;
	public int frameColor = 0xffffffff;
	private int _totalTime = 100;
	public boolean gradient = false;
	public boolean save = true;
	public boolean saveAs = true;
	protected Attachment _attachment = null;
	boolean _AttaBool = false;
	private long DB_id = -1;
	private Attachment _doneArt = null;
	int lastUsedTime = -1;
	long refChild = -1;
	long refPro = -1;
	long timeKey = 0;

	//constructor
	public SubProfile(String name, String description, int bgcolor, int timeLeftColor, int timeSpentColor, int frameColor, int totalTime, boolean changeColor){
		if(this._id == -1){
			this._id = guard.getId();
		}		this.name = name;
		this.desc = description;
		this.bgcolor = bgcolor;
		this.timeLeftColor = timeLeftColor;
		this.timeSpentColor = timeSpentColor;
		this.frameColor = frameColor;
		this._totalTime = totalTime;
		this.gradient = changeColor;
	}
		
	public SubProfile(String name, String description, int bgcolor, int timeLeftColor, int timeSpentColor, int frameColor, boolean changeColor){
		if(this._id == -1){
			this._id = guard.getId();
		}
		this.name = name;
		this.desc = description;
		this.bgcolor = bgcolor;
		this.timeLeftColor = timeLeftColor;
		this.timeSpentColor = timeSpentColor;
		this.frameColor = frameColor;
		this.gradient = changeColor;
	}
	
	public SubProfile(SubProfile obj){
		if(this._id == -1){
			this._id = guard.getId();
		}
		this.name = obj.name;
		this.desc = obj.desc;
		this.bgcolor = obj.bgcolor;
		this.timeLeftColor = obj.timeLeftColor;
		this.timeSpentColor = obj.timeSpentColor;
		this.frameColor = obj.frameColor;
		this._totalTime = obj._totalTime;
		this.gradient = obj.gradient;
	}
	
	public Attachment getDoneArt(){
		return this._doneArt;
	}
	
	public void setDoneArt(Attachment art){
		this._doneArt = art;
	}
	
	/**
	 * Generates a hashmap with settings of the subprofile
	 * @param p
	 * 		The subprofile which is supposed to be stored to the hashmap
	 * @return
	 * 		A hashmap with the settings stored in
	 */
	public HashMap<String, String> getHashMap(){
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("db_id", String.valueOf(this.getDB_id()));
		map.put("type", this.formType().toString());		
		map.put("Attachment", String.valueOf(this._AttaBool));		
		map.put("Name", this.name);		
		map.put("desc", this.desc);		
		map.put("bgcolor", String.valueOf(this.bgcolor));		
		map.put("timeLeftColor", String.valueOf(this.timeLeftColor));		
		map.put("timeSpentColor", String.valueOf(this.timeSpentColor));		
		map.put("frameColor", String.valueOf(this.frameColor));
		map.put("totalTime", String.valueOf(this.get_totalTime()));		
		map.put("gradient", String.valueOf(this.gradient));		
		map.put("save", String.valueOf(this.save));		
		map.put("saveAs", String.valueOf(this.saveAs));
		map.put("refChild", String.valueOf(this.refChild));		
		map.put("refPro", String.valueOf(this.refPro));
		map.put("timeKey", String.valueOf(this.timeKey));
		
		if(this._doneArt != null){
			map.put("doneArtType", String.valueOf(this._doneArt.getForm()));
			switch(this._doneArt.getForm()){
			case SingleImg:
				map.put("doneArtPic", String.valueOf(this._doneArt.getImg().getId()));
				map.put("doneArtLeftPic", String.valueOf(-1));
				map.put("doneArtRightPic", String.valueOf(-1));
				break;
			case SplitImg:
				map.put("doneArtPic", String.valueOf(-1));
				map.put("doneArtLeftPic", String.valueOf(this._doneArt.getLeftImg().getId()));
				map.put("doneArtRightPic", String.valueOf(this._doneArt.getRightImg().getId()));
				break;
			}
		} else {
			map.put("doneArtType", String.valueOf(formFactor.undefined));
			map.put("doneArtPic", String.valueOf(-1));
			map.put("doneArtLeftPic", String.valueOf(-1));
			map.put("doneArtRightPic", String.valueOf(-1));
		}
		
		// TODO: Attachment this needs some testing and remember to add it to the crud!!!!
		if(this._AttaBool){
			map = this._attachment.getHashMap(map);				
		}	else {
			Attachment tempAttachment = new Attachment();
			map = tempAttachment.getHashMap(map);
		}
		
		return map;
	}
	
	public int get_totalTime() {
		return _totalTime;
	}
	
	/**
	 * Sets the time of the subprofile and its attachment
	 * @param time
	 * 		Total runtime of the subprofile (in seconds)
	 */
	public void set_totalTime(int _totalTime) {
		this._totalTime = _totalTime;
		// Syncronizes the time of the profile and its attachment
		if(this._attachment != null){
			this._attachment.changeTime(_totalTime);
		}
	}
	
	public void select(){
		guard.setSubProfile(this);
	}
	
	public void delete(){
		START:
			for(Child c : guard.Children()){
				for(SubProfile p : c.SubProfiles()){
					if(p._id == this._id){
						//Toast.makeText(guard.m_context, "Id: " + this.getId() + " DB_id: " + this.getDB_id(), 4000).show();
						guard.delete(c, this);
						break START;
					}
				}
			}
	}
	
	public SubProfile(){
		if(this._id == -1){
			this._id = guard.getId();
		}
	}
	
	public long getId(){
		return this._id;
	}
	
	protected void setId(long l){
		this._id = l;
	}
	
	public Attachment getAttachment(){
		return _attachment;
	}
	
	
	public void setAttachment(Attachment p){
		if(p != null){
			this._AttaBool = true;
			this._attachment = p;
			this._attachment.changeTime(this._totalTime);
		} else {
			this._attachment = null;
			this._AttaBool = false;
		}
	}
	
	public formFactor formType(){
		return formFactor.SubProfile;
	}
	
	public SubProfile copy(){
		return this;
	}
	
	public SubProfile save(SubProfile oldProfile, boolean override){
		START:
		for(Child c : guard.Children()){
			for(SubProfile p : c.SubProfiles()){
				if(p._id == oldProfile._id){
					
					this.setDB_id(oldProfile.getDB_id());
					this.setId(oldProfile.getId());
					c.remove(p);
					c.save(this, override);
					break START;
					
				}
			}
		}
		
		return this;
		
	}
	
	public void addLastUsed(SubProfile oldProfile){
		if(oldProfile == null){
			guard.addLastUsed(this);
		} else {
		this._id = oldProfile._id;
		this.refPro = oldProfile.DB_id;
		long ref = 0;
		for(Child c : guard.Children()){
			for(SubProfile p : c.SubProfiles()){
				if(p.getId() == this.getId()){
					ref = c.getProfileId();
				}
			}
		}
		for(SubProfile p : guard.predefined()){
			if(p.getId() == this.getId()){
				ref = -2;
			}
		}
		this.refChild = ref;
		guard.addLastUsed(this);
		}
	}

	public boolean equals(Object o) {
        if (!(o instanceof SubProfile))
            return false;
        SubProfile n = (SubProfile) o;
        return n.name.equals(name) && n.name.equals(name);
    }

    public int hashCode() {
        return 31*name.hashCode() + name.hashCode();
    }

    public String toString() {
	return name + " " + name;
    }

    public int compareTo(SubProfile n) {
        int lastCmp = name.compareTo(n.name);
        return (lastCmp != 0 ? lastCmp : name.compareTo(n.name));
    }

	public SubProfile toHourglass() {
		Hourglass form = new Hourglass(this.name, this.desc, this.bgcolor, this.timeLeftColor, this.timeSpentColor, this.frameColor, this._totalTime, this.gradient);
		form.setId(this.getId());		
			form.setAttachment(this._attachment);
			form.setDoneArt(this._doneArt);
		return form;
	}

	public SubProfile toProgressBar() {
		ProgressBar form = new ProgressBar(this.name, this.desc, this.bgcolor, this.timeLeftColor, this.timeSpentColor, this.frameColor, this._totalTime, this.gradient);
		form.setId(this.getId());
			form.setAttachment(this._attachment);
			form.setDoneArt(this._doneArt);
		return form;
	}

	public SubProfile toTimeTimer() {
		TimeTimer form = new TimeTimer(this.name, this.desc, this.bgcolor, this.timeLeftColor, this.timeSpentColor, this.frameColor, this._totalTime, this.gradient);
		form.setId(this.getId());
			form.setAttachment(this._attachment);
			form.setDoneArt(this._doneArt);
		return form;
	}

	public SubProfile toDigitalClock() {
		DigitalClock form = new DigitalClock(this.name, this.desc, this.bgcolor, this.timeLeftColor, this.timeSpentColor, this.frameColor, this._totalTime, this.gradient);
		form.setId(this.getId());
			form.setAttachment(this._attachment);
			form.setDoneArt(this._doneArt);
		return form;
	}

	public long getDB_id() {
		return DB_id;
	}

	public void setDB_id(long dB_id) {
		if(this.DB_id < 0){
			this.DB_id = dB_id;
		}
	}
}
