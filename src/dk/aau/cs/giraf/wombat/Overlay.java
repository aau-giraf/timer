package dk.aau.cs.giraf.wombat;

        import android.app.Service;
        import android.content.Intent;
        import android.graphics.PixelFormat;
        import android.media.MediaPlayer;
        import android.os.Handler;
        import android.os.IBinder;
        import android.view.Display;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;
        import android.view.View.OnTouchListener;
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.Toast;
        import android.content.Context;

        import dk.aau.cs.giraf.TimerLib.Guardian;
        import dk.aau.cs.giraf.TimerLib.SubProfile;
        import dk.aau.cs.giraf.wombat.drawlib.*;

public class Overlay extends Service {
    View view;
    private Handler mHandler;
    private Runnable mRunnable;
    private MediaPlayer mediaPlayer;
    @Override
    public IBinder
    onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        mediaPlayer = MediaPlayer.create(Overlay.this, CustomizeFragment.soundindex); //soundplayer with song
        Guardian guard = Guardian.getInstance();
        SubProfile sub = guard.getSubProfile();
        sub.getAttachment();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        DrawLibActivity.frameHeight = disp.getHeight()/DrawLibActivity.scale;
        DrawLibActivity.frameWidth = disp.getWidth()/DrawLibActivity.scale;
        view = GetWatchViews(sub, DrawLibActivity.frameWidth);

        //Layoutparams bestemme hvordan denne service skal fungere
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                DrawLibActivity.frameWidth,
                DrawLibActivity.frameHeight,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,//System overlay er det der tvinger servicen til altid at blive vist
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//gør at man ikke kan klikke på det
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL//hvis FLAG_NOT_FOCUSABLE ikke er slået til endnu så sender den klik events videre til det der ligger nedenunder
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);//Gør vinduet gennemsigtigt, men da vi viser et view gør det intet


        params.gravity = Gravity.RIGHT | Gravity.TOP;//får overlayet til at ligge til højre i toppen
        params.setTitle("Load Average");
        wm.addView(view, params);

        mHandler = new Handler();

        final Intent overlayIntent = new Intent(Overlay.this, DoneScreenActivity.class);
        overlayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        mRunnable = new Runnable() {
            public void run() {
                getApplicationContext().startActivity(overlayIntent);
                getApplicationContext().stopService(MainActivity.svc);
                MainActivity.svc = null;

            }
        };

        /* Set the delay of the intent to the time of the timer + 1 second
         * otherwize the user will have a hard time seeing the timer reach 0*/
        mHandler.postDelayed(mRunnable, (sub.get_totalTime()+1)*1000);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(view != null)
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
            view = null;
        }
        mHandler.removeCallbacks(mRunnable);
    }

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
