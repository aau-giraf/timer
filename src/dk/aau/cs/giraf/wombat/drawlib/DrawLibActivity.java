package dk.aau.cs.giraf.wombat.drawlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.SubProfile;
/**
 * This class is the activity which initiate the timer
 * Layer: Draw
 *
 */

public class DrawLibActivity extends Activity {


	public static int frameHeight;
	public static int frameWidth;
	
	private Handler mHandler;
	private Runnable mRunnable;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		/* Hide the status bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		  View main_layout = findViewById(android.R.id.content).getRootView();
		  main_layout.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		  
//		SubProfile sub = new ProgressBar("", "", 0xff3D3D3D, 0xff000066, 0xffB8B8B8, 0xff000000, 3, false);
//		SubProfile sub2 = new ProgressBar("", "", 0xff3D3D3D, 0xff000066, 0xffB8B8B8, 0xff000000, 900, true);
//		sub.setAttachment(sub2);
		  
		/* Get the guardian from timerlib */
		Guardian guard = Guardian.getInstance();
		SubProfile sub = guard.getSubProfile();
		
		
		// Get display size
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		frameHeight = disp.getHeight();
		frameWidth = disp.getWidth();
				
		
		if (sub.getAttachment() == null) {
			/* Set the drawing class as the content view */
			View v = genDrawView(sub,frameWidth);
			v.setKeepScreenOn(true);
			setContentView(v);
		} else {
			/* Since there is an attachement do all this */
			LinearLayout frame = new LinearLayout(this);
			frame.setKeepScreenOn(true);
			GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {sub.bgcolor, 0xFF000000});
			
			/* Initialize views */
			View v = null;
			View v2 = null;
			ImageView i = null;
			ImageView i2 = null;
			ImageView i3 = null;
			switch(sub.getAttachment().getForm()){
			case Timer: // Attach another timer
				frameWidth = frameWidth/2;
				 v = genDrawView(sub,frameWidth);
				frame.addView(v, frameWidth, frameHeight);
				v2 = genDrawView(sub.getAttachment().genSub(),frameWidth);
				frame.addView(v2, frameWidth, frameHeight);
				break;
			case SingleImg: // Attach a single image
				frameWidth = frameWidth/2;
				v = genDrawView(sub,frameWidth);
				frame.addView(v, frameWidth, frameHeight);
				
				i = new ImageView(this);
				i.setImageResource(sub.getAttachment().getImg().getPath());
				i.setBackgroundDrawable(gd);
				frame.addView(i, frameWidth, frameHeight);
				break;
			case SplitImg: // Attach two images
				frameWidth = frameWidth/2;
				v = genDrawView(sub, frameWidth);

				frame.addView(v, frameWidth, frameHeight);
				
				frameWidth = frameWidth/2;
				frameWidth = frameWidth - 15;
				i = new ImageView(this);
				i.setImageResource(sub.getAttachment().getLeftImg().getPath());
				i.setBackgroundDrawable(gd);
				frame.addView(i, frameWidth, frameHeight);
				
				i2 = new ImageView(this);
				i2.setImageResource(sub.getAttachment().getRightImg().getPath());
				i2.setBackgroundDrawable(gd);
				frame.addView(i2, frameWidth, frameHeight);
				
				i3 = new ImageView(this);
				i3.setBackgroundDrawable(gd);
				frame.addView(i3, 30, frameHeight);
				break;
			}
			
			/* Set the content of DrawLibActivity as the above frame*/
			setContentView(frame);
		}
		
		/* Set a timer for the DoneScreenActivity*/
		mHandler = new Handler();
		
		mRunnable = new Runnable() {
            public void run() {
                final Intent mainIntent = new Intent(DrawLibActivity.this, DoneScreenActivity.class);
                DrawLibActivity.this.startActivity(mainIntent);
                DrawLibActivity.this.finish();
            }
        };
		
        /* Set the delay of the intent to the time of the timer + 1 second
         * otherwize the user will have a hard time seeing the timer reach 0*/
		mHandler.postDelayed(mRunnable, (sub.get_totalTime()+1)*1000);
	}
	
	/* When the activity is destroyed, kill the donescreen aswell*/
	public void onDestroy(){
		super.onDestroy();
		mHandler.removeCallbacks(mRunnable);
	}
	
	/**
	 * Generate a view from the subprofile
	 * @param sub
	 * 		Subprofile which should be converted to a view
	 * @param frameWidth
	 * 		The width of the frame it should be withing
	 * @return
	 * 		A view of the subprofile
	 */
	private View genDrawView(SubProfile sub, int frameWidth) {
		switch (sub.formType()) {
		case ProgressBar:
			return new DrawProgressBar(getApplicationContext(), sub, frameWidth);
		case Hourglass:
			return new DrawHourglass(getApplicationContext(), sub, frameWidth);
		case DigitalClock:
			return new DrawDigital(getApplicationContext(), sub, frameWidth);
		case TimeTimer:
			return new DrawWatch(getApplicationContext(), sub, frameWidth);
		default:
			return null;
		}
	}
	
}
