package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dk.aau.cs.giraf.TimerLib.formFactor;
/**
 * This class is an ArrayAdapter which is used for the Attachment function
 * Layer: Layout
 *
 */
public class ModeAdapter extends ArrayAdapter<formFactor> {

	private ArrayList<formFactor> items;

	public ModeAdapter(Context context, int textViewResourceId,
			ArrayList<formFactor> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.profile_list, null);
		}
		// TODO: Pictures
		formFactor c = items.get(position);
		if (c != null) {
			ImageView iv = (ImageView) v.findViewById(R.id.profilePic);
			TextView tv = (TextView) v.findViewById(R.id.profileName);

			if (iv != null) {
				iv.setImageResource(R.drawable.default_profile);
			}
			if (tv != null) {
				if (c == formFactor.Timer) {
					tv.setText(R.string.formfactor_timer);
				} else if (c == formFactor.SingleImg) {
					tv.setText(R.string.formfactor_singleimg);
				} else if (c == formFactor.SplitImg) {
					tv.setText(R.string.formfactor_splitimg);
				}
			}

		}
		
//			if(c.getProfileId() == Guardian.profileID){
//				v.setBackgroundResource(R.drawable.list_selected);
//				Guardian.profileFirstClick = true;
//				Guardian.profileID = -1;
//			}
		return v;
	}
}
