package dk.aau.cs.giraf.TimerLib;

public class Art {
	
	private int _path;
	private String _caption;
	private int _id = -1;
	Guardian guard = Guardian.getInstance();
	
	public Art(int path, String caption, int id){
		this._path = path;
		this._caption = caption;
		this._id = id;
	}
	
	public String getName(){
		return this._caption;
	}
	
	public formFactor getForm(){
		return formFactor.undefined;
	}
	
	public int getPath(){
		return this._path;
	}
	
	int getId(){
		return this._id;
	}
}
