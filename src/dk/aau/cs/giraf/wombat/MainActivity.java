package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.view.View;

import com.google.analytics.tracking.android.EasyTracker;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Pictogram;

/**
 * This class is an MainActivity used to initiate WOMBAT
 * Layer: Main
 *
 */
public class MainActivity extends Activity {
	public static Guardian guard = null;
    public static Context context;
    public static Intent svc = null;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        context = getApplicationContext();
//        Helper ohelp = new Helper(context);
//        ohelp.CreateDummyData();

        int guardianId = -1;
        int childId = -1;
        int color;
		/* Get the data sent from the launcher (if there is any) */


		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	guardianId = extras.getInt("currentGuardianID");
        	childId = extras.getInt("currentChildID");
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
        }

        color = getResources().getColor(R.color.GIRAFOrange);
        ArrayList<Bitmap> artList = new ArrayList<Bitmap>();

        artList.add(BitmapFactory.decodeResource(getResources(), R.drawable.p_done));
        artList.add(BitmapFactory.decodeResource(getResources(), R.drawable.p_gaa_til_skema));

        /* Insert hard coded pictograms */
//        Art p_done = new Art(R.drawable.p_done,"Færdig", 0);
//		Art p_skema = new Art(R.drawable.p_gaa_til_skema,"Gå til skema", 1);
//		Art p_taxa = new Art(R.drawable.p_gaa_til_taxa,"Gå til taxa", 2);
//		Art p_ryd_op = new Art(R.drawable.p_ryd_op, "Ryd op", 3);
//
//		artList.add(p_done);
//		artList.add(p_skema);
//		artList.add(p_taxa);
//		artList.add(p_ryd_op);

		/* Initialize the guardian object */
    	guard = Guardian.getInstance(childId, guardianId, getApplicationContext(), artList);
    	guard.backgroundColor = color;
    	
		// Set content view according to main, which implements two fragments
//        if(extras != null) {
            setContentView(R.layout.main);
            Drawable d = getResources().getDrawable(R.drawable.background);
            d.setColorFilter(color, PorterDuff.Mode.OVERLAY);
            findViewById(R.id.mainLayout).setBackgroundDrawable(d);
//        }
//        else {
//            setContentView(R.layout.blank);
//        }
	}


    @Override
    public void onStart() {
        super.onStart();

        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }

	/**
	 * Clear everything in case the user is going to log out
	 */
	public void onBackPressed() {
        if (svc == null) {
		    guard.reset();
        }
		finish();
	}
}
