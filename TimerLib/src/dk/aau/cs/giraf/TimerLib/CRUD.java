package dk.aau.cs.giraf.TimerLib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Application;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.ProfileApplication;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

public class CRUD {
	Context context;
	Helper oHelp;
	Guardian guard = Guardian.getInstance();
	private int appId; 
	private String _lastUsed = "lastUsed";
	private String _subprofile = "SUBPROFILE";
	private ArrayList<String> lastUsedList;

	public CRUD(int appID, Context context){
		this.context = context;
		oHelp = new Helper(context);
		this.appId = appID;
	}

	void removeLastUsed(Child c, SubProfile p, int profileId){
		removeSubprofileFromProfileId(p,profileId);
	}

	void addLastUsed(Child c, SubProfile p, long profileId){

		Calendar cal = Calendar.getInstance();
		String sec = String.valueOf(cal.get(Calendar.SECOND));
		String min = String.valueOf(cal.get(Calendar.MINUTE));
		String hour = String.valueOf(cal.get(Calendar.SECOND));
		String day = String.valueOf(cal.get(Calendar.DAY_OF_YEAR));
		String year = String.valueOf(cal.get(Calendar.YEAR));

		String timeKey = year+""+day+""+hour+""+min+""+sec;

		p.timeKey = Integer.valueOf(timeKey);

		saveChild(c,p);
	}

	SubProfile retrieveLastUsed(HashMap<String, String> token){
		SubProfile sp = null;
		return sp;
	}

	void initLastUsed(int profileId){
		// Find the guardian by Id
		Profile prof = oHelp.profilesHelper.getProfileById(profileId);

		//Retrieve app by id and profileId
        Application app = oHelp.applicationHelper.getApplicationById(appId);
		ProfileApplication profApp = oHelp.profileApplicationHelper.getProfileApplicationByProfileIdAndApplicationId(app, prof);

		//Retrieve the settings
		Setting<String,String,String> settings = profApp.getSettings();

		if(settings != null){
			Set<String> keys = settings.keySet();
			List<SubProfile> mSubPs = findProfileSettings(profileId);	

			if(mSubPs.size() != 0){
				
				SubProfile[] sortList = new SubProfile[mSubPs.size()];
				
				mSubPs.toArray(sortList);

				sortList = lastUsedSort(sortList);
				//The last used list is now sorted.
				for(SubProfile p : sortList){
					guard.addLastUsed(p);
				}
			}
		}
	}

	private SubProfile[] lastUsedSort(SubProfile[] sp){
		//Bubble sort for lastused, sorts after the date
		int i,j = 0;
		SubProfile t = null;

		for(i = 0; i < sp.length; i++){
			for(j = 1; j < sp.length-1; j++){

				if(sp[j-1].lastUsedTime > sp[j].lastUsedTime){

					t = sp[j-1];
					sp[j-1] = sp[j];
					sp[j] = t;

				}

			}
		}

		return sp;
	}

	//Waiting for admin, we can insert media but not retrieve them :D
	//	public void LoadPictures(){
	//		MediaHelper mp = new MediaHelper(guard.m_context);
	//		
	//		Media m = mp.getMediaById(id)
	//	}

	public void loadGuardian(int guardianID){
		// Load the guardian form Oasis
		Profile mGuardian = oHelp.profilesHelper.getProfileById(guardianID);

		// Find all subprofiles of the child and save it on the child
		ArrayList<SubProfile> mGuardSubPs = findProfileSettings(mGuardian.getId());

		// Load the children from Oasis of the guardian
		List<Profile> mChildren = oHelp.profilesHelper.getChildrenByGuardian(mGuardian);
		guard.Children().clear();

		for (Profile c : mChildren) {
			Child mC;
			String mName;
			// Generate the name of the child
			mName = c.getName();

			mC = new Child(mName);
			mC.setProfileId(c.getId());

			// Find all subprofiles of the child and save it on the child
			List<SubProfile> mSubPs = findProfileSettings(c.getId());
			for (SubProfile subProfile : mSubPs) {
				mC.SubProfiles().add(subProfile);
			}

			// Get the biggest ID from the database
			int id = -1;

            Application app = oHelp.applicationHelper.getApplicationById(appId);
            
            ProfileApplication profApp = oHelp.profileApplicationHelper.getProfileApplicationByProfileIdAndApplicationId(app, c);
            if(profApp != null){

                if(profApp.getSettings() != null){
                    Setting<String, String, String> settings = profApp.getSettings();
                    Set<String> keys = settings.keySet();

                    for (String key : keys) {
                        if(id < Long.valueOf(key)){
                            id = Integer.valueOf(key);
                        }
                    }
                }
                mC.setSubProfileId(id);
            }

			guard.Children().add(mC);
		}

		//Init lastUsed skal laves 
		//guard.clearLastUsed();
		//guard.initLastUsed(mGuardSubPs);
	}
	/**
	 * Stores the subprofile on the child in Oasis
	 * @param c
	 * 		The Child where the subprofile is supposed to be stored
	 * @param sp
	 * 		The subprofile which is to be stored
	 * @return
	 * 		Returns true if it completed, else returns false
	 */
	public boolean saveChild(Child c, SubProfile sp){
		// Convert the subprofile to a hashmap
		HashMap<String, String> hm = sp.getHashMap();

		// Find the app settings on the profileID
        Application app = oHelp.applicationHelper.getApplicationById(appId);
        Profile prof = oHelp.profilesHelper.getProfileById((int)c.getProfileId());        
        ProfileApplication profApp = oHelp.profileApplicationHelper.getProfileApplicationByProfileIdAndApplicationId(app, prof);
        
		Setting<String, String, String> settings = profApp.getSettings();
		if(settings == null){
			settings = new Setting<String, String, String>();
		}
		// Insert the hashmap with the subprofile ID as key
		settings.put(String.valueOf(sp.getDB_id()), hm);
        profApp.setSettings(settings);
//		Profile newProf = oHelp.profilesHelper.getProfileById((int)c.getProfileId());
		oHelp.profileApplicationHelper.modifyProfileApplication(profApp);

		return true;	
	}

	/**
	 * Stores the subprofile on the guardian in Oasis
	 * @param guardianId
	 * 		The guardian ID found in the TimerLoader.guardianID
	 * @param sp
	 * 		The subprofile which is to be stored
	 * @return
	 * 		Returns true if it completed, else returns false
	 */
	public boolean saveGuardian(int guardianId, SubProfile sp){
		// Convert the subprofile to a hashmap
		HashMap<String, String> hm = sp.getHashMap();

		// Find the app settings on the profileID
		Application app = oHelp.applicationHelper.getApplicationById(appId);
        Profile prof = oHelp.profilesHelper.getProfileById(guardianId);
        ProfileApplication profApp = oHelp.profileApplicationHelper.getProfileApplicationByProfileIdAndApplicationId(app,prof);

		Setting<String, String, String> settings = profApp.getSettings();
		if(settings == null){
			settings = new Setting<String, String, String>();
		}

		// Insert the hashmap with the subprofile ID as key
		settings.put(String.valueOf(sp.getDB_id()), hm);
        profApp.setSettings(settings);
//		Profile newProf = oHelp.profilesHelper.getProfileById(guardianId);
        oHelp.profileApplicationHelper.modifyProfileApplication(profApp);

		return true;	
	}


	/**
	 * Loads all subprofiles on the specific ID
	 * @param id
	 * 		The id of the profile holding subprofiles
	 * @return
	 * 		A list of subprofiles extracted from the settings
	 */
	private ArrayList<SubProfile> findProfileSettings(int id) {
		ArrayList<SubProfile> mSubs = new ArrayList<SubProfile>();

        Application app = oHelp.applicationHelper.getApplicationById(appId);
        Profile prof = oHelp.profilesHelper.getProfileById(id);
        ProfileApplication profApp = oHelp.profileApplicationHelper.getProfileApplicationByProfileIdAndApplicationId(app, prof);
        if(profApp != null){
            if(profApp.getSettings() != null){
                Setting<String, String, String> settings = profApp.getSettings();
                Set<String> keys = settings.keySet();

                for (String key : keys) {
                    if(key.equalsIgnoreCase(_lastUsed) == false){
                        if(key.equalsIgnoreCase("count") == false){
                            SubProfile sub = getSubProfile(settings.get(key));
                            mSubs.add(sub);
                        }
                    }
                }
            }
        }
		return mSubs;
	}

	/**
	 * Loads a subprofile from the hashmap
	 * @param hm
	 * 		The hashmap which holds the subprofile
	 * @return
	 * 		The subprofile extracted from the hashmap
	 */
	private SubProfile getSubProfile(HashMap<String, String> hm){		
		SubProfile p = new SubProfile();
		/* Load all settings from the hash table */
		p.name = String.valueOf(hm.get("Name"));
		p.desc = String.valueOf(hm.get("desc"));	
		p.bgcolor = Integer.valueOf((String)hm.get("bgcolor"));
		p.timeLeftColor = Integer.valueOf((String)hm.get("timeLeftColor"));
		p.timeSpentColor = Integer.valueOf((String)hm.get("timeSpentColor"));
		p.frameColor = Integer.valueOf((String)hm.get("frameColor"));
		p.set_totalTime(Integer.valueOf((String)hm.get("totalTime")));
		p.gradient = Boolean.valueOf((String)hm.get("gradient"));
		formFactor factor = formFactor.convert(hm.get("type"));
		p.refChild = Integer.valueOf(hm.get("refChild"));
		p.refPro = Integer.valueOf(hm.get("refPro"));
		p.timeKey = Integer.valueOf(hm.get("timeKey"));


		/* Change the subprofile to the correct type */
		p = convertType(p,factor);

		p.setDB_id(Integer.valueOf(hm.get("db_id")));
		p.save = Boolean.valueOf((String)hm.get("save"));
		p.saveAs = Boolean.valueOf((String)hm.get("saveAs"));

		p._AttaBool = (Boolean.valueOf((String) hm.get("Attachment")));
		//Attachment
		if(p._AttaBool){
			//Attachment attachP = new Attachment();
			formFactor aFactor = formFactor.convert(hm.get("AttachmentForm"));
			formFactor tFactor = formFactor.convert(hm.get("timerForm"));
			Attachment atc = null;
			switch(aFactor){
			case Timer:
				int t_bgColor = Integer.valueOf(hm.get("_bgColor"));
				int t_frameColor = Integer.valueOf(hm.get("_frameColor"));
				int t_timeLeftColor = Integer.valueOf(hm.get("_timeLeftColor"));
				int t_timeSpentColor = Integer.valueOf(hm.get("_timeSpentColor"));
				boolean t_gradient = Boolean.valueOf(hm.get("_gradient"));
				int t_time = Integer.valueOf((String)hm.get("totalTime"));

                atc = new Timer(tFactor,t_bgColor,t_timeLeftColor, t_timeSpentColor, t_frameColor,t_time, t_gradient);
				break;
			case SingleImg:
//				int asd = Integer.valueOf(hm.get("singleImgId"));
                atc = new SingleImg(guard.ArtList.get(0));
				break;
			case SplitImg:
                atc = new SplitImg(guard.ArtList.get(0),guard.ArtList.get(1));
				break;
			}
			p.setAttachment(atc);

			//Slutbilled

			formFactor sFactor = formFactor.convert(hm.get("doneArtType"));
			Attachment slutbilled = null;
			switch(sFactor){
			case SingleImg:
				slutbilled = new SingleImg(guard.ArtList.get(0));
				break;
			case SplitImg:
				slutbilled = new SplitImg(guard.ArtList.get(0),guard.ArtList.get(1));
				break;
			}
			p.setDoneArt(slutbilled);

			//			attachP = convertType(attachP, aFactor);
			//			
			//			p.setAttachment(attachP);

		}	

		return p;
	}


	SubProfile convertType(SubProfile p, formFactor factor){
		switch (factor) {
		case Hourglass:
			p = new Hourglass(p.name, p.desc, p.bgcolor, p.timeLeftColor, p.timeSpentColor, p.frameColor, p.get_totalTime(), p.gradient);
			break;
		case TimeTimer:
			p = new TimeTimer(p.name, p.desc, p.bgcolor, p.timeLeftColor, p.timeSpentColor, p.frameColor, p.get_totalTime(), p.gradient);
			break;
        case TimeTimerStandard:
            p = new TimeTimerStandard(p.name, p.desc, p.bgcolor, p.timeLeftColor, p.timeSpentColor, p.frameColor, p.get_totalTime(), p.gradient);
            break;
		case ProgressBar:
			p = new ProgressBar(p.name, p.desc, p.bgcolor, p.timeLeftColor, p.timeSpentColor, p.frameColor, p.get_totalTime(), p.gradient);
			break;
		case DigitalClock:
			p = new DigitalClock(p.name, p.desc, p.bgcolor, p.timeLeftColor, p.timeSpentColor, p.frameColor, p.get_totalTime(), p.gradient);
			break;
		default:
			p = new SubProfile();
			break;
		}

		return p;

	}

	/**
	 * Remove the specific subprofile from the profile (Child/Guardian)
	 * @param p
	 * 		The subprofile which is supposed to be removed
	 * @param profileId
	 * 		The profile which the subprofile is going to be removed from
	 */
	public void removeSubprofileFromProfileId(SubProfile p, int profileId) {
		// Find the profile by Id

		// Find the Wombat App
        Application app = oHelp.applicationHelper.getApplicationById(appId);
        Profile prof = oHelp.profilesHelper.getProfileById(profileId);
        ProfileApplication profApp = oHelp.profileApplicationHelper.getProfileApplicationByProfileIdAndApplicationId(app, prof);

		// Get the settings from the profile and update
		Setting<String, String, String> settings = profApp.getSettings();
		settings.remove(String.valueOf(p.getDB_id()));
        profApp.setSettings(settings);

		// Update the app
//		oHelp.appsHelper.modifyAppByProfile(app, prof);

        oHelp.profileApplicationHelper.modifyProfileApplication(profApp);

	}

}
