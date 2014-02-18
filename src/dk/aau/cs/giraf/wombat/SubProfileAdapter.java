package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.SubProfile;
/**
 * This class is an ArrayAdapter which fit the SubProfile object
 * Layer: Layout
 *
 */
public class SubProfileAdapter extends ArrayAdapter<SubProfile> {

	private ArrayList<SubProfile> items;
	Guardian guard = Guardian.getInstance();
	public SubProfileAdapter(Context context, int textViewResourceId,
			ArrayList<SubProfile> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		if(v == null){
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.subprofile_list, null);
		}
		SubProfile sp = items.get(position);
		if( sp != null ){
			/* Find all the views */
			ImageView iv = (ImageView)v.findViewById(R.id.subProfilePic);
			ImageView ivBG = (ImageView)v.findViewById(R.id.subProfilePicBackground);
			TextView tvName = (TextView)v.findViewById(R.id.subProfileName);
			TextView tvDesc = (TextView)v.findViewById(R.id.subProfileDesc);
			
			/* Add a picture corresponding to the content */
			if(iv != null){
				switch(sp.formType()){
				case Hourglass:
					iv.setImageResource(R.drawable.thumbnail_hourglass);
					break;
				case DigitalClock:
					iv.setImageResource(R.drawable.thumbnail_digital);
					break;
				case ProgressBar:
					iv.setImageResource(R.drawable.thumbnail_progressbar);
					break;
				case TimeTimer:
					iv.setImageResource(R.drawable.thumbnail_timetimer);
					break;
				default:
					iv.setImageResource(R.drawable.thumbnail_hourglass);
					break;
				}
			}
			if(ivBG != null){
				
				ivBG.setBackgroundColor(sp.timeLeftColor);
				
			}
			
			/* Set the name and description */
			if(tvName != null){
				tvName.setText(sp.name);
//				tvName.setText("id: " + sp.getId() + " db id: " + sp.getDB_id());
			}
			if(tvDesc != null){
				tvDesc.setText(sp.desc);
			}
			
		}
		
		/* Highlight this profile if it is the chosen one*/
		if(sp.getId() == guard.subProfileID){
			v.setBackgroundResource(R.drawable.list_selected);
			guard.subProfileFirstClick = true;
			guard.subProfileID = -1;
		} else {
			v.setBackgroundResource(R.drawable.list);
		}
		return v;
	}
}
