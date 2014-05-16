package dk.aau.cs.giraf.TimerLib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Application;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.ProfileApplication;

public class Guardian {
	
	private static Guardian _instance = null;
	
	private ArrayList<Child> _guard = null;
	
	private ArrayList<SubProfile> _lastUsed = null;
	
	private ArrayList<SubProfile> _predef = null;
	
	private ArrayList<Child> _sortedList = null;
	
	private Child _selectedChild = null;
	
	private SubProfile _selectedSubProfile = null;
	
	public ArrayList<Bitmap> ArtList = new ArrayList<Bitmap>();
	
	public String _name = null;
	public CRUD crud;
	
	private int id = -1;
	
	private int _artId = -1;
	
	private Application m_app;
	private Helper oHelp;
	public Profile m_oGuard;
	
	private int childId;
	public int guardianId;
	Context m_context;
	
	public int profilePosition;
	public int profileID;
	public int subProfileID; 			// Stores the id of the saved subprofile
	public boolean subProfileFirstClick;	// When a subprofile is saved, this is set to true
												// When something is clicked in the subprofile list, set to false
	public boolean profileFirstClick;
	public int backgroundColor;
	
	private ArrayList<formFactor> _mode = null;
	
	int getArtId(){
		this._artId++;
		return this._artId;
	}
	
	public void reset(){
		_instance = null;
	}
	
	public ArrayList<formFactor> getMode(){
		if(_mode == null){
			_mode = new ArrayList<formFactor>();
			_mode.add(formFactor.Timer);
			_mode.add(formFactor.SingleImg);
			_mode.add(formFactor.SplitImg);
			return _mode;
		}  else {
			return _mode;
		}
	}
	
	/**
	 * Default constructor for Guardian
	 */
	private Guardian(){	
	}
	
	/**
	 * Generate a new id
	 * @return id
	 */
	int getId(){
		id++;
		return id;
	}
	
	/**
	 * Singleton constructor for guardian.
	 * Always use this instead of the default constructor.
	 * 
	 * Example:
	 * Guardian guard = Guadian.getInstance();
	 * @return Guardian instance
	 */
	public static Guardian getInstance(int m_childId, int m_guardianId, Context c, ArrayList<Bitmap> artList){
		if(_instance == null){

			_instance = new Guardian();
			
			_instance.ArtList = artList;
		
			
			TimerHelper help = new TimerHelper();
            _instance.guardianId = m_guardianId;
			_instance.profileID = m_childId;
			_instance.m_context = c;
			
			_instance.oHelp = new Helper(c);
			
			int appId = _instance.findAppId();
			_instance.guardianId = _instance.findGuardianId();//finder også hele guardian objektet
			
			_instance.createChildren();
			
			_instance.crud = new CRUD(appId, c);
			_instance.crud.loadGuardian(_instance.guardianId);
			
			help.loadPredef();
			
			//_instance.crud.initLastUsed(_instance.m_oGuard.getId());
			
			_instance.publishList();			
			
			//crud.retrieveLastUsed(m_guardianId);
		}
        return _instance;
	}
	
	public static Guardian getInstance(){
			return _instance;
	}
	
	/**
	 * Find the app specified by the mainActivity, if non is avalible a default one is created
	 * @return
	 */
	private int findAppId() {
		// Find the app which has the same package name as this one
		for (Application a : oHelp.applicationHelper.getApplications()) {
			String cname = m_context.getPackageName();
			if(a.getPackage().equalsIgnoreCase(cname)){
				m_app = a;
				break;
			}
		}
			
		if(m_app == null){
			// If no app has been found, generate one and insert it in Oasis
            //(String name, String version, Bitmap icon, String pack, String activity, String description, int author)

            Bitmap emptyImage = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
			m_app = new Application("Wombat", "1", emptyImage, m_context.getPackageName(), "MainActivity", "The Timer", 1);
			m_app.setId(oHelp.applicationHelper.insertApplication(m_app));
		}
		
		return m_app.getId();
	}
	
	/**
	 * Search for the guard specified by the mainActivity, if non is existing a default one is created
	 * @return
	 */
	private int findGuardianId() {
		
		if(guardianId != -1){
			// Does the original guard exist
			m_oGuard = oHelp.profilesHelper.getProfileById(guardianId);
		} else {
			m_oGuard = null;
		}
	
		// If not, try the default guard
		if(m_oGuard == null){
			for (Profile p : oHelp.profilesHelper.getProfiles()) {
				if(p.getName().equals("Tony Stark")){
					m_oGuard = p;
					break;
				}
			}
			
			// If thats not valid either, make the default guard
			if(m_oGuard == null){
                //Profile(String name, long phone, Bitmap picture, String email, Roles role, String address, Setting<String, String, String> settings, int userId, int departmentId, int author)
				m_oGuard = new Profile("Mette Als", 88888888, null, "Mette@yoMomma.dk", Profile.Roles.GUARDIAN, "yoMommaLane 62", null, 1, 1);
				m_oGuard.setId(oHelp.profilesHelper.insertProfile(m_oGuard));
                ProfileApplication profApp = new ProfileApplication();
                profApp.setProfileId(m_oGuard.getId());
                profApp.setApplicationId(m_app.getId());
                oHelp.profileApplicationHelper.insertProfileApplication(profApp);
                oHelp.profilesHelper.authenticateProfile("jkkxlagqyrztlrexhzofekyzrnppajeobqxcmunkqhsbrgpxdtqgygnmbhrgnpphaxsjshlpupgakmirhpyfaivvtpynqarxsghhilhkqvpelpreevykxurtppcggkzfaepihlodgznrmbrzgqucstflhmndibuymmvwauvdlyqnnlxkurinuypmqypspmkqavuhfwsh");
//				oHelp.applicationHelper.attachAppToProfile(m_app, m_oGuard);
//				oHelp.profilesHelper.setCertificate("jkkxlagqyrztlrexhzofekyzrnppajeobqxcmunkqhsbrgpxdtqgygnmbhrgnpphaxsjshlpupgakmirhpyfaivvtpynqarxsghhilhkqvpelpreevykxurtppcggkzfaepihlodgznrmbrzgqucstflhmndibuymmvwauvdlyqnnlxkurinuypmqypspmkqavuhfwsh", m_oGuard);
				
			}
		} 
		
		return m_oGuard.getId();
	}
	
	
	private void createChildren() {		
		if(oHelp.profilesHelper.getChildrenByGuardian(m_oGuard).isEmpty()){
			List<String> names = new ArrayList<String>();
			names.add("Sigurd");
			names.add("Marcus");
			names.add("Emil");
			names.add("Lukas");
			names.add("Mads");
			names.add("Nikolaj");
			
			for (String s : names) {
				Profile newProf = new Profile(s, 88888888, null, "kiddo@yoMomma.dk", Profile.Roles.CHILD, "kiddoLane 61", null, 1, 1);
                newProf.setId(oHelp.profilesHelper.insertProfile(newProf));
				oHelp.profilesHelper.attachChildToGuardian(newProf, m_oGuard);
                ProfileApplication profApp = new ProfileApplication();
                profApp.setProfileId(m_oGuard.getId());
                profApp.setApplicationId(m_app.getId());
                oHelp.profileApplicationHelper.insertProfileApplication(profApp);
			}
		}
	}
	
	
	/**
	 * Stores the subprofile on the guardian in Oasis
	 * @param currSubP
	 * 		The subprofile which is to be stored
	 * @return
	 * 		Returns true if it completed, else returns false
	 */
	public void saveGuardian(SubProfile currSubP) {
		crud.saveGuardian(guardianId, currSubP);
	}	
	
	/**
	 * Stores the subprofile on the child in Oasis
	 * @param c
	 * 		The Child where the subprofile is supposed to be stored
	 * @param sp
	 * 		The subprofiel which is to be stored
	 * @return
	 * 		Returns true if it completed, else returns false
	 */
	public void saveChild(Child c, SubProfile sp){
		crud.saveChild(c, sp);
	}
	
	/**
	 * ArrayList Children of Child object type
	 * @return ArrayList<Child>
	 */
	public ArrayList<Child> Children(){
		if(_guard == null){
		_guard = new ArrayList<Child>();
		}
		return _guard;
	}
	
	/**
	 * ArrayList lastUsed of SubProfile object type
	 * @return ArrayList<SubProfile> lastUsed
	 */
	private ArrayList<SubProfile> lastUsed(){
		if(_lastUsed == null){
		_lastUsed = new ArrayList<SubProfile>();
		}
		return _lastUsed;
	}
	
	/**
	 * Not in use atm
	 */
	private void initLastUsedPredef(){
		if(_lastUsed == null){
			_lastUsed = new ArrayList<SubProfile>();
		}
		if(_predef == null){
			_predef = new ArrayList<SubProfile>();
		}
	}
	
	/**
	 * getChild is used to return a selected child
	 * @return selected child
	 */
	public Child getChild(){
		return _selectedChild;
	}
	
	/**
	 * Selects a specific child
	 * @param c The child you wish to select
	 */
	void setChild(Child c){
		_selectedChild = c;
	}
	/**
	 * getSubProfile is used to return a selected SubProfile
	 * @return selected subprofile
	 */
	public SubProfile getSubProfile(){
		return _selectedSubProfile;
	}
	/**
	 * Select a specific SubProfile
	 * @param p The SubProfile you wish to select
	 */
	void setSubProfile(SubProfile p){
		_selectedSubProfile = p;
	}

	//Waiting for admin
	void initLastUsed(ArrayList<SubProfile> profile){
		_lastUsed = new ArrayList<SubProfile>();
		for(SubProfile p : profile){
			_lastUsed.add(p);
		}
	}
	
	private ArrayList reverse(ArrayList list){
		ArrayList value = new ArrayList();
		for(int i = list.size()-1; i > -1; i--){
			value.add(list.get(i));
		}
		return value;
	}
	
	/**
	 * Add a SubProfile to the list of lastUsed
	 * @param profile The SubProfile which you want to add to the lastUsed list
	 */
	public void addLastUsed(SubProfile profile){
		lastUsed();
//		crud.retrieveLastUsed(guardianId);
		boolean exists = false;
		
		Child child = null;
		GETOUTOFHERE:
		for(Child c : publishList()){
			for(SubProfile p : c.SubProfiles()){
				if(profile.getId() == p.getId()){
					child = c;
					break GETOUTOFHERE;
				}
			}
		}
		
		for(int i = 0; i < _lastUsed.size(); i++){
			//Checks if the SubProfile is already on the list.
			if(_lastUsed.get(i).getId() == profile.getId()){
				_lastUsed.remove(i);				
				_lastUsed.add(profile);
				//crud.removeLastUsed(child, profile, guardianId);
				//crud.addLastUsed(child,profile,guardianId);
				exists = true;
				break;
			}
		}
		
		if(!exists){
			_lastUsed.add(profile);
//			crud.addLastUsed(child,profile,guardianId);
		}
		
		if(_lastUsed.size() > 10){
//			crud.removeLastUsed(child, profile, guardianId);
			_lastUsed.remove(_lastUsed.remove(0));
			//crud.removeLastUsed(child, profile, guardianId);
		}
		
	}
	
	/**
	 * Clears last
	 */
	public void clearLastUsed(){
		if(_lastUsed != null){
			_lastUsed.clear();
		}
	}
	
	/**
	 * ArrayList of predefined of the SubProfile Object type
	 * @return ArrayList<SubProfile> predefined profiles
	 */
	ArrayList<SubProfile> predefined(){
		if(_predef == null){
		_predef = new ArrayList<SubProfile>();
		}
		return _predef;
	}
	/**
	 * This method is use to generate a list of lastUsed, predefined and all the children.
	 * ONLY use this method when you wish to generate a list of all the lists. Never use this list to execute methods on.
	 * @return A sorted list of lastUsed, predefined and all the children.
	 */
	public ArrayList<Child> publishList(){
		if(_sortedList == null){
			_sortedList = new ArrayList<Child>();
		}
		
		_sortedList.clear();
		Child lastUsedChild = new Child("Last Used");
		lastUsedChild.setProfileId(-3);
		lastUsedChild.SubProfiles().addAll(reverse(lastUsed()));
		lastUsedChild.setLock();
		lastUsedChild.lockDelete();
		_sortedList.add(lastUsedChild);
		
		Child predefChild = new Child("Predefined Profiles");
		predefChild.setProfileId(-2);
		Collections.sort(predefined());
		predefChild.SubProfiles().addAll(predefined());
		predefChild.setLock();
		predefChild.lockDelete();
		_sortedList.add(predefChild);
		
		if(_guard != null){
			Collections.sort(_guard);
			for(Child p : _guard){
				Collections.sort(p.SubProfiles());
			}
			_sortedList.addAll(_guard);
		}
		
		return _sortedList;
	}

	public void delete(Child c, SubProfile subProfile) {
		c.SubProfiles().remove(subProfile);
		crud.removeSubprofileFromProfileId(subProfile, c.getProfileId());
		//TODO: DELETE THIS
		//Toast.makeText(m_context, subProfile.getDB_id() + " ", Toast.LENGTH_SHORT).show();
	}


}