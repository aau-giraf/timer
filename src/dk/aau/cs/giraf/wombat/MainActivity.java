package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
		
		long guardianId;
		long childId;
		int color;	

		/* Get the data sent from the launcher (if there is any) */
		Bundle extras = getIntent().getExtras();
        if (extras != null) {        	
        	guardianId = extras.getLong("currentGuardianID");
        	childId = extras.getLong("currentChildID");
        	color = extras.getInt("appBackgroundColor");
        } else {
        	guardianId = -1;
        	childId = -3;
        	color = getResources().getColor(R.color.GIRAFOrange);
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
		setContentView(R.layout.main);
		
		Drawable d = getResources().getDrawable(R.drawable.background);
		d.setColorFilter(color, PorterDuff.Mode.OVERLAY);
		findViewById(R.id.mainLayout).setBackgroundDrawable(d);
	}
	
	/**
	 * Clear everything in case the user is going to log out
	 */
	public void onBackPressed() {
		guard.reset();
		finish();
	}
}
