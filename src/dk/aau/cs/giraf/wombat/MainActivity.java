package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ComponentName;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
/**
 * This class is an MainActivity used to initiate WOMBAT
 * Layer: Main
 *
 */
public class MainActivity extends Activity {
	Guardian guard = null;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

        long guardianId = -1;
        long childId = -3;
        int color;

		/* Get the data sent from the launcher (if there is any) */
		Bundle extras = getIntent().getExtras();
        if (extras != null) {        	
        	guardianId = extras.getLong("currentGuardianID");
        	childId = extras.getLong("currentChildID");
        	color = extras.getInt("appBackgroundColor");
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Timer")
                    .setMessage(R.string.launch_from_giraf)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Process.killProcess(Process.myPid());
                        }
                    }).show();
        	color = getResources().getColor(R.color.Black);
        }

        ArrayList<Art> artList = new ArrayList<Art>();
        
        /* Insert hard coded pictograms */
        Art p_done = new Art(R.drawable.p_done,"F�rdig", 0);
		Art p_skema = new Art(R.drawable.p_gaa_til_skema,"G� til skema", 1);
		Art p_taxa = new Art(R.drawable.p_gaa_til_taxa,"G� til taxa", 2);
		Art p_ryd_op = new Art(R.drawable.p_ryd_op, "Ryd op", 3);
		
		artList.add(p_done);
		artList.add(p_skema);
		artList.add(p_taxa);
		artList.add(p_ryd_op);

		/* Initialize the guardian object */
    	guard = Guardian.getInstance(childId, guardianId, getApplicationContext(), artList);    	
    	guard.backgroundColor = color;
    	
		// Set content view according to main, which implements two fragments
        if(extras != null) {
            setContentView(R.layout.main);
            Drawable d = getResources().getDrawable(R.drawable.background);
            d.setColorFilter(color, PorterDuff.Mode.OVERLAY);
            findViewById(R.id.mainLayout).setBackgroundDrawable(d);
        }
        else {
            setContentView(R.layout.blank);
        }

		

	}
	
	/**
	 * Clear everything in case the user is going to log out
	 */
	public void onBackPressed() {
		guard.reset();
		finish();
	}

    public void switchUser(View v){
        long guardId = guard.guardianId;
        // change color of profile button from faded back to solid color
        // 0f is invisible and 1f is solid
        //changeProfileButton.setAlpha(1f);

        // this is the code for launching the profile selector in the launcher project
        // the launcher then creates a new instance of the tortoise project

        // create a new intent
        Intent intent = new Intent("dk.aau.cs.giraf.launcher.action.SELECTPROFILE");

        // put package name
        intent.putExtra("appPackageName", "dk.aau.cs.giraf.wombat");

        // put Activity name
        intent.putExtra("appActivityName", "dk.aau.cs.giraf.wombat.MainActivity");

        // put App Background Color
        intent.putExtra("appBackgroundColor", 0xFF16A765);

        // Put current guardian id
        intent.putExtra("currentGuardianID", guardId);

        intent.setComponent(new ComponentName("dk.aau.cs.giraf.launcher", "dk.aau.cs.giraf.launcher.ProfileSelectActivity"));

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
        guard.reset();
        finish();
    }
}
