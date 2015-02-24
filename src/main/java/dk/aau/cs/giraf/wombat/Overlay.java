package dk.aau.cs.giraf.wombat;

        import android.app.Service;
        import android.content.Intent;
        import android.graphics.PixelFormat;
        import android.os.Handler;
        import android.os.IBinder;
        import android.view.Display;
        import android.view.Gravity;
        import android.view.View;
        import android.view.WindowManager;
        import android.content.Context;

        import java.util.ArrayList;

        import dk.aau.cs.giraf.TimerLib.Guardian;
        import dk.aau.cs.giraf.TimerLib.SubProfile;
        import dk.aau.cs.giraf.wombat.drawlib.*;

public class Overlay extends Service {
    View view;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    public IBinder
    onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Get the needed profiles
        Guardian guard = Guardian.getInstance();
        SubProfile sub = guard.getSubProfile();
        sub.getAttachment();

        //Find the size of the screen
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DrawLibActivity.frameHeight = display.getHeight()/DrawLibActivity.scale;
        DrawLibActivity.frameWidth = display.getWidth()/DrawLibActivity.scale;
        view = GetWatchViews(sub, DrawLibActivity.frameWidth);//Finds the watch that will be shown

        //Layoutparams decides how the overlay behaves
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                DrawLibActivity.frameWidth,
                DrawLibActivity.frameHeight,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,//System overlay forces the overlay to always be visible
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//Makes the overlay non focusable
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL//Sends any touch events to the activity underneath
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,//Checks for touch events outside the overlay
                PixelFormat.TRANSLUCENT);//Makes the overlay transparent


        params.gravity = Gravity.RIGHT | Gravity.TOP;//Places the overlay in the top right corner of the screen
        params.setTitle("Load Average");//Sets the title of the overlay
        wm.addView(view, params);//Adds the watch to the overlay

        //Intent for starting the donescreen
        final Intent overlayIntent = new Intent(Overlay.this, DoneScreenActivity.class);
        overlayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        //This runnable is a container that contains code, that will be executed when it's called
        mRunnable = new Runnable() {
            public void run() {
                getApplicationContext().startActivity(overlayIntent);
                getApplicationContext().stopService(MainActivity.svc);
                MainActivity.svc = null;

            }
        };

        /* Set the delay of the intent to the time of the timer + 1 second
         * otherwize the user will have a hard time seeing the timer reach 0
         * Also the code in the runnable is delayed, so it's called when the timer ends*/
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, (sub.get_totalTime()+1)*1000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //When the timer ends, it removes the watch
        if(view != null)
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
            view = null;
        }

        //Stops the runnable from being called
        mHandler.removeCallbacks(mRunnable);
    }

    //Finds the appropriate watch that will be shown in the overlay
    public View GetWatchViews(SubProfile sub, int frameWidth) {
        switch (sub.formType()) {
            case ProgressBar:
                return new DrawProgressBar(getApplicationContext(), sub, frameWidth);
            case Hourglass:
                return new DrawHourglass(getApplicationContext(), sub, frameWidth);
            case DigitalClock:
                return new DrawDigital(getApplicationContext(), sub, frameWidth);
            case TimeTimer:
                return new DrawWatch(getApplicationContext(), sub, frameWidth);
            case TimeTimerStandard:
                return new DrawStandardWatch(getApplicationContext(), sub, frameWidth);
            default:
                return null;
        }
    }
}
