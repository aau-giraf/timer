package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	public View getView(final int position, View convertView, ViewGroup parent){
		View v = convertView;
        final Context c = getContext();
		if(v == null){
			LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.subprofile_list, null);
		}
		SubProfile sp = items.get(position);
		if( sp != null ){
			/* Find all the views */
			ImageView iv = (ImageView)v.findViewById(R.id.subProfilePic);
			ImageView ivBG = (ImageView)v.findViewById(R.id.subProfilePicBackground);
			TextView tvName = (TextView)v.findViewById(R.id.subProfileName);
			TextView tvDesc = (TextView)v.findViewById(R.id.subProfileDesc);
            ImageButton deleteButton = (ImageButton)v.findViewById(R.id.subProfileDelete);
            deleteButton.setTag(position);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    final WDialog deleteDialog = new WDialog(c, R.string.delete_subprofile_message);
                    deleteDialog.addTextView(c.getResources().getText(R.string.delete_description) + " " + guard.getChild().SubProfiles().get(position).name + "?", 1);

                    deleteDialog.addButton(R.string.delete_yes, 2, new View.OnClickListener() {
                        public void onClick(View v) {
                            if(guard.getChild() != null && guard.getChild().deleteCheck()) {
                                guard.getChild().SubProfiles().get(position).delete();
                                Toast t = Toast.makeText(c,
                                        R.string.delete_subprofile_toast,
                                        Toast.LENGTH_LONG);
                                t.show();
                                notifyDataSetChanged();
                            }
                            else {
                                Toast t = Toast.makeText(c,
                                        R.string.cannot_delete_subprofile_toast,
                                        Toast.LENGTH_LONG);
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
                }
            });



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
