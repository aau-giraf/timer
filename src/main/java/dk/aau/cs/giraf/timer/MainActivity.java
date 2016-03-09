package dk.aau.cs.giraf.timer;

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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.analytics.tracking.android.EasyTracker;

import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.dblib.Helper;
import dk.aau.cs.giraf.dblib.models.Pictogram;
import dk.aau.cs.giraf.dblib.models.Profile;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.gui.GirafProfileSelectorDialog;
import dk.aau.cs.giraf.showcaseview.ShowcaseManager;
import dk.aau.cs.giraf.showcaseview.ShowcaseView;
import dk.aau.cs.giraf.showcaseview.targets.ViewTarget;

/**
 * This class is an MainActivity used to initiate WOMBAT
 * Layer: Main
 *
 */
public class MainActivity extends GirafActivity implements GirafProfileSelectorDialog.OnSingleProfileSelectedListener, ShowcaseManager.ShowcaseCapable {
	public static Guardian guard = null;
    public static Context context;
    public static Intent svc = null;

    /**
     * Used to showcase views
     */
    private ShowcaseManager showcaseManager;
    private boolean isFirstRun;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        context = getApplicationContext();
//        Helper ohelp = new Helper(context);
//        ohelp.CreateDummyData();

        long guardianId = 2;
        long childId = -1;
        int color;
		/* Get the data sent from the launcher (if there is any) */

        if (ActivityManager.isUserAMonkey()) {
            Helper h = new Helper(this);
            guardianId = h.profilesHelper.getGuardians().get(0).getId();
            childId = h.profilesHelper.getChildren().get(0).getId();
        }
        else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                guardianId = extras.getLong("currentGuardianID");
                childId = extras.getLong("currentChildID");
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Tidstager")
                        .setMessage(R.string.launch_from_giraf)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Process.killProcess(Process.myPid());
                            }
                        }).show();
            }

        }
        final GirafButton helpGirafButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_help));
        helpGirafButton.setId(R.id.help_button);
        helpGirafButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.toggleShowcase();
            }
        });
        addGirafButtonToActionBar(helpGirafButton, GirafActivity.RIGHT);

        color = getResources().getColor(R.color.GIRAFOrange);
        ArrayList<Bitmap> artList = new ArrayList<Bitmap>();

        artList.add(BitmapFactory.decodeResource(getResources(), R.drawable.p_done));
        artList.add(BitmapFactory.decodeResource(getResources(), R.drawable.p_gaa_til_skema));

		/* Initialize the guardian object */
    	guard = Guardian.getInstance(childId, guardianId, getApplicationContext(), artList);
    	guard.backgroundColor = color;

        setContentView(R.layout.main);
        Drawable d = getResources().getDrawable(R.drawable.background);
        d.setColorFilter(color, PorterDuff.Mode.OVERLAY);
        findViewById(R.id.mainLayout).setBackgroundDrawable(d);
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

    @Override
    public void onProfileSelected(int i, Profile profile) {
        //This is a hack to make the new giraf profile choose to work with this cluster fuck of code.
        CustomizeFragment customizeFragment = (CustomizeFragment) getFragmentManager().findFragmentById(R.id.customizeFragment);
        customizeFragment.onProfileSelected(i, profile);
    }
    @Override
    public synchronized void showShowcase() {

        // Targets for the Showcase
        final ViewTarget chooseHourglassAsIconTarget = new ViewTarget(R.id.hourglassButton, this, 1.5f);
        final ViewTarget chooseTimetimerStandardButtonAsIconTarget = new ViewTarget(R.id.timetimerStandardButton, this, 1.5f);
        final ViewTarget chooseProgressbarButtonAsIconTarget = new ViewTarget(R.id.progressbarButton, this, 1.5f);
        final ViewTarget chooseDigitalButtonAsIconTarget = new ViewTarget(R.id.digitalButton, this, 1.5f);
        final ViewTarget chooseCustomizeAttachmentAsIconTarget = new ViewTarget(R.id.customizeAttachment, this, 1.5f);
        final ViewTarget chooseCustomizeDonescreenAsIconTarget = new ViewTarget(R.id.customizeDonescreen, this, 1.5f);
        final ViewTarget chooseCustomizeSaveAsIconTarget = new ViewTarget(R.id.customizeSave, this, 1.5f);
        final ViewTarget chooseCustomizeStartButtonAsIconTarget = new ViewTarget(R.id.customizeStartButton, this, 1.5f);
        final ViewTarget chooseCustomizeStopButtonAsIconTarget = new ViewTarget(R.id.customizeStopButton, this, 1.5f);
        final ViewTarget changeUserTarget = new ViewTarget(R.id.customize_profile_button, this, 1.5f);



        // Create a relative location for the next button
        final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        final int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        // Calculate position for the help text
        final int textX = margin;
        final int textY = (int)(getResources().getDisplayMetrics().heightPixels * 0.8);

        showcaseManager = new ShowcaseManager();

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseHourglassAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.hourglass_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.hourglass_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseTimetimerStandardButtonAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.timetimerStandardButton_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.timetimerStandardButton_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseProgressbarButtonAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.chooseProgressbarButtonAsIconTarget_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.chooseProgressbarButtonAsIconTarget_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });


        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseDigitalButtonAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.chooseDigitalButtonAsIconTarget_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.chooseDigitalButtonAsIconTarget_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });


        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseCustomizeAttachmentAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.chooseCustomizeAttachmentAsIconTarget_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.chooseCustomizeAttachmentAsIconTarget_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });


        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseCustomizeDonescreenAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.chooseCustomizeDonescreenAsIconTarget_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.chooseCustomizeDonescreenAsIconTarget_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });


        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseCustomizeSaveAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.chooseCustomizeSaveAsIconTarget_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.chooseCustomizeSaveAsIconTarget_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });


        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseCustomizeStartButtonAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.chooseCustomizeStartButtonAsIconTarget_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.chooseCustomizeStartButtonAsIconTarget_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });


        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(chooseCustomizeStopButtonAsIconTarget, true);
                showcaseView.setContentTitle(getString(R.string.chooseCustomizeStopButtonAsIconTarget_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.chooseCustomizeStopButtonAsIconTarget_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(changeUserTarget, true);
                showcaseView.setContentTitle(getString(R.string.change_user_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.change_user_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });



        ShowcaseManager.OnDoneListener onDoneCallback = new ShowcaseManager.OnDoneListener() {
            @Override
            public void onDone(ShowcaseView showcaseView) {
                showcaseManager = null;
                isFirstRun = false;
            }
        };
        showcaseManager.setOnDoneListener(onDoneCallback);

        showcaseManager.start(this);
    }

    @Override
    public synchronized void hideShowcase() {

        if (showcaseManager != null) {
            showcaseManager.stop();
            showcaseManager = null;
        }
    }

    @Override
    public synchronized void toggleShowcase() {

        if (showcaseManager != null) {
            hideShowcase();
        } else {
            showShowcase();
        }
    }
}

