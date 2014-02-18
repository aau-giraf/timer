package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.SubProfile;
import dk.aau.cs.giraf.oasis.lib.Helper;
/**
 * This class is a ListFragment which fit the SubProfile object
 * Layer: Layout
 *
 */
public class SubProfileFragment extends android.app.ListFragment {
	Guardian guard = Guardian.getInstance();
	ListView thisListView;

	@Override
	// Start the list empty
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(guard.profileID >= 0){
			Helper helper = new Helper(getActivity());
			int position;
			// Marks the selected profile in the guard singleton
			position = guard.publishList().indexOf(helper.profilesHelper.getProfileById(guard.profileID));
			if(position != -1){
				guard.publishList().get(position).select();
				ArrayList<SubProfile> subprofiles = guard.getChild().SubProfiles();
				SubProfileAdapter adapter = new SubProfileAdapter(getActivity(),
						android.R.layout.simple_list_item_1, subprofiles);
				setListAdapter(adapter);
			}	
			else {
				setListAdapter(null);
			}
			
			
		}else {
			setListAdapter(null);
		}
		ListView lv = getListView();
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					final int row, long arg3) {
				
					TextView tv = new TextView(getActivity());
					tv.setText(getString(R.string.delete_description) + " " + guard.getChild().SubProfiles().get(row).name + "?");
					tv.setTextColor(0xFFFFFFFF);
				
					final WDialog deleteDialog = new WDialog(getActivity(), R.string.delete_subprofile_message);
					deleteDialog.addTextView(getString(R.string.delete_description) + " " + guard.getChild().SubProfiles().get(row).name + "?", 1);
					deleteDialog.addButton(R.string.delete_yes, 2, new View.OnClickListener() {
						
						public void onClick(View v) {
							if (guard.getChild() != null && guard.getChild().deleteCheck()) {
								guard.getChild().SubProfiles().get(row)
										.delete();
								CustomizeFragment cf = (CustomizeFragment) getFragmentManager()
										.findFragmentById(R.id.customizeFragment);
								cf.setDefaultProfile();
								Toast t = Toast.makeText(getActivity(),
										R.string.delete_subprofile_toast,
										5000);
								t.show();
								loadSubProfiles();
								} else {
									Toast t = Toast.makeText(getActivity(),
											R.string.cannot_delete_subprofile_toast, 5000);
									t.show();
								}
							deleteDialog.dismiss();
						}
					});

					deleteDialog.addButton(R.string.delete_no, 3, new View.OnClickListener() {
						
						public void onClick(View v) {
							deleteDialog.cancel();
							
						}
					});

					deleteDialog.show();
				return true;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		loadSubProfiles();
	}
	
	/**
	 * Inserts the templates on profile id in the details list
	 * 
	 */
	public void loadSubProfiles() {
		if(guard.getChild() != null){
		ArrayList<SubProfile> subprofiles = guard.getChild().SubProfiles();
		SubProfileAdapter adapter = new SubProfileAdapter(getActivity(),
				android.R.layout.simple_list_item_1, subprofiles);
		setListAdapter(adapter);
		}
	}

	public void onListItemClick(ListView lv, View view, int position, long id) {
		if (guard.subProfileFirstClick) {
			for (int i = 0; i < lv.getChildCount(); i++) {
				lv.getChildAt(i).setBackgroundResource(R.drawable.list);
			}
			guard.subProfileFirstClick = false;
		}
		for (int i = 0; i < lv.getChildCount(); i++) {
			lv.getChildAt(i).setSelected(false);
		}
		view.setSelected(true);
		guard.subProfileID = guard.getChild().SubProfiles().get(position).getId();
		
		
		@SuppressWarnings("unused")
		SubProfile asd = guard.getChild().SubProfiles().get(position);
		
		CustomizeFragment fragment = (CustomizeFragment) getFragmentManager()
				.findFragmentById(R.id.customizeFragment);
		fragment.loadSettings(guard.getChild().SubProfiles().get(position));
	}

}
