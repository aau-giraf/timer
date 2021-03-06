package dk.aau.cs.giraf.timer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dk.aau.cs.giraf.dblib.Helper;
//import dk.aau.cs.giraf.gui.GButton;
//import dk.aau.cs.giraf.gui.GButtonProfileSelect;
import dk.aau.cs.giraf.gui.GCheckBox;
import dk.aau.cs.giraf.gui.GColorPicker;
//import dk.aau.cs.giraf.gui.GProfileSelector;
import dk.aau.cs.giraf.gui.GTextView;
//import dk.aau.cs.giraf.gui.GButtonSettings;
import dk.aau.cs.giraf.gui.GToast;
//import dk.aau.cs.giraf.gui.GToggleButton;
import dk.aau.cs.giraf.dblib.controllers.PictogramController;
import dk.aau.cs.giraf.dblib.models.Pictogram;
import dk.aau.cs.giraf.dblib.models.Profile;
import dk.aau.cs.giraf.gui.GWidgetProfileSelection;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.gui.GirafProfileSelectorDialog;
import dk.aau.cs.giraf.timer.drawlib.DoneScreenActivity;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
//import yuku.ambilwarna.AmbilWarnaDialog;
//import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import dk.aau.cs.giraf.TimerLib.Attachment;
import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.SingleImg;
import dk.aau.cs.giraf.TimerLib.SplitImg;
import dk.aau.cs.giraf.TimerLib.SubProfile;
import dk.aau.cs.giraf.TimerLib.Timer;
import dk.aau.cs.giraf.TimerLib.formFactor;
import dk.aau.cs.giraf.timer.drawlib.DrawLibActivity;
/**
 * This class is a Fragment which which is used to customize SubProfiles
 * Layer: Layout
 *
 */
public class CustomizeFragment extends Fragment {
	private SubProfile preSubP;
	private SubProfile currSubP;
	private Guardian guard = Guardian.getInstance();
    private ArrayList<Child> children = guard.publishList();
    private Child child;

	private GirafButton hourglassButton;
	private GirafButton timetimerButton;
    private GirafButton timetimerStandardButton;
	private GirafButton progressbarButton;
	private GirafButton digitalButton;
	private GirafButton startButton;
    private GirafButton stopButton;
    private GirafButton profileButton;
    private GirafButton SoundButton;
    private GirafButton switchLayoutButton;
	private GirafButton saveButton;
	//private GButton saveAsButton;
	private GirafButton attachmentButton;
	private GirafButton donePictureButton;
	private Button colorGradientButton1;
	private Button colorGradientButton2;
	private Button colorFrameButton;
	private Button colorBackgroundButton;

	private WheelView mins;
	private WheelView secs;
    private int TempChange = 8;
    private GCheckBox gradientCheckBox;
    //private WCheckbox gradientButton;
	private GTextView timeDescription;
    SharedPreferences pref;
    private ArrayList<formFactor> _soundlist = null; // list of sounds
    private MediaPlayer mediaPlayer; //soundplayer
	private GTextView SoundButtonText;
	private static final int CHANGE_USER_SELECTOR_DIALOG = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Write Tag = Detail and Text = Detail Opened in the LogCat
		Log.e("Customize", "Customize Opened");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
 			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.customize, container, false);
	}

	@Override
	// Start the list empty
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		GirafButton b = (GirafButton) getActivity()
				.findViewById(R.id.new_template_button);
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				guard.subProfileID = -1;
				SubProfileFragment spf = new SubProfileFragment();
                spf = (SubProfileFragment) getFragmentManager()
						.findFragmentById(R.id.subprofileFragment);
				spf.loadSubProfiles();
				CustomizeFragment cf = (CustomizeFragment) getFragmentManager()
						.findFragmentById(R.id.customizeFragment);
				cf.setDefaultProfile();
				GToast t = GToast.makeText(getActivity(),
						getString(R.string.new_template_button_text),
						Toast.LENGTH_SHORT);
				t.show();
			}
		});

		currSubP = new SubProfile("", "",
                                  getResources().getColor(R.color.DarkerGray2),
                                  getResources().getColor(R.color.Gray),
                                  getResources().getColor(R.color.White),
                                  getResources().getColor(R.color.Black), 600, false);
		currSubP.save = false;
		currSubP.saveAs = false;

        if(children == null || !children.isEmpty()) {
            for (Child _child : children) {
                if(_child.getProfileId() == guard.profileID) {
                    children.get(children.indexOf(_child)).select();
                    child = _child;
                    break;
                }
            }
        }
        GTextView tv = (GTextView) getActivity().findViewById(R.id.customizeHeader);
        CharSequence cs;
        if (child != null) {
            cs = child.name;
        }
        else {
            cs = "Ingen Valgt Profil";
        }
        tv.setText(cs);

		/********* TIME CHOSER *********/
		initStyleChoser();

		/********* TIMEPICKER *********/
		initTimePicker();

		/********* COLORPICKER *********/
		initColorButtons();

		/********* ATTACHMENT PICKER *********/
		initAttachmentButton();

		/******** BOTTOM MENU ***********/
		initBottomMenu();
        Log.v("guardChildId", "" + guard.profileID);


	}

	public void setDefaultProfile() {
		currSubP = new SubProfile("", "",
                getResources().getColor(R.color.DarkerGray2),
                getResources().getColor(R.color.Gray),
                getResources().getColor(R.color.White),
                getResources().getColor(R.color.Black), 600, false);
		currSubP.save = false;
		currSubP.saveAs = false;

		preSubP = null;

//        SoundButton = (GButton) getActivity().findViewById(R.id.sound_button);
        DoneScreenActivity.soundindex = R.raw.song;
        //SoundButton.setText("Vælg lyd");

		reloadCustomize();
	}

	/**
	 * Initialize the style chooser buttons
	 */
	private void initStyleChoser() {
		hourglassButton = (GirafButton) getActivity().findViewById(R.id.hourglassButton);
		hourglassButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.Hourglass);
			}
		});

		timetimerButton = (GirafButton) getActivity().findViewById(
				R.id.timetimerButton);
		timetimerButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.TimeTimer);

			}
		});

        timetimerStandardButton = (GirafButton) getActivity().findViewById(R.id.timetimerStandardButton);
        timetimerStandardButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                selectStyle(formFactor.TimeTimerStandard);

            }
        });

		progressbarButton = (GirafButton) getActivity().findViewById(
				R.id.progressbarButton);
		progressbarButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.ProgressBar);

			}
		});

		digitalButton = (GirafButton) getActivity().findViewById(R.id.digitalButton);
		digitalButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.DigitalClock);

			}
		});
	}

	/**
	 * Change style on currSubP according to formFactor type
	 *
	 * @param formType
	 *            formFactor to change to
	 */
	private void selectStyle(formFactor formType) {
		if (formType == formFactor.Hourglass) {
			currSubP = currSubP.toHourglass();
			hourglassButton.setChecked(true);
			setSave();
		} else {
            hourglassButton.setChecked(false);
		}

		if (formType == formFactor.TimeTimer) {
			currSubP = currSubP.toTimeTimer();
            timetimerButton.setChecked(true);
			setSave();
		} else {
            timetimerButton.setChecked(false);
		}
        if (formType == formFactor.TimeTimerStandard) {
            currSubP = currSubP.toTimeTimerStandard();
            timetimerStandardButton.setChecked(true);
            setSave();
        } else {
            timetimerStandardButton.setChecked(false);
        }
		if (formType == formFactor.ProgressBar) {
			currSubP = currSubP.toProgressBar();
            progressbarButton.setChecked(true);
			setSave();
		} else {
            progressbarButton.setChecked(false);
		}
		if (formType == formFactor.DigitalClock) {
			currSubP = currSubP.toDigitalClock();
            digitalButton.setChecked(true);
			setSave();
		} else {
            digitalButton.setChecked(false);
		}
		genDescription();
		initBottomMenu();
	}

    /**
     * Determine if the current timer configuration is saveable.
     *
     * */
	private boolean setSave() {
		boolean complete = false;
        if(currSubP.get_totalTime() > 0 &&
                (currSubP.formType() == formFactor.Hourglass ||
                 currSubP.formType() == formFactor.TimeTimer ||
                 currSubP.formType() == formFactor.TimeTimerStandard ||
                 currSubP.formType() == formFactor.ProgressBar ||
                 currSubP.formType() == formFactor.DigitalClock)) {

            if (guard.getChild() != null) {
                currSubP.save = true;
            } else {
                currSubP.save = false;
            }
            currSubP.saveAs = true;
        }
        else {
            currSubP.save = false;
            currSubP.saveAs = false;
        }

		return complete;
	}

	private int previousMins;
	private int previousSecs;

	/**
	 * Initialize the time picker wheel
	 */
	private void initTimePicker() {
		/* Create minute Wheel */
		mins = (WheelView) getActivity().findViewById(R.id.minPicker);
		mins.setViewAdapter(new NumericWheelAdapter(getActivity()
				.getApplicationContext(), 0, 60));
		mins.setCyclic(true);

		/* Create Seconds wheel */
		secs = (WheelView) getActivity().findViewById(R.id.secPicker);
		secs.setViewAdapter(new NumericWheelAdapter(getActivity()
				.getApplicationContext(), 0, 59));
		secs.setCyclic(true);

		/* Create description of time chosen */
		timeDescription = (GTextView) getActivity().findViewById(R.id.showTime);
		setTime(currSubP.get_totalTime());

		/* Add on change listeners for both wheels */
		mins.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateTime(mins.getCurrentItem(), secs.getCurrentItem());

				if (mins.getCurrentItem() == 60) {
					previousMins = 60;
					previousSecs = secs.getCurrentItem();

					secs.setCurrentItem(0);
					secs.setViewAdapter(new NumericWheelAdapter(getActivity()
							.getApplicationContext(), 0, 0));
					secs.setCyclic(false);
				} else if (previousMins == 60) {
					secs.setViewAdapter(new NumericWheelAdapter(getActivity()
							.getApplicationContext(), 0, 60));

					secs.setCurrentItem(previousSecs);
					secs.setCyclic(true);
					previousMins = 0;
				}

                setSave();
                initBottomMenu();
			}
		});

		secs.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateTime(mins.getCurrentItem(), secs.getCurrentItem());
                setSave();
                initBottomMenu();
            }
        });
	}

	/**
	 * Sets the time on the time picker wheels
	 *
	 * @param _totalTime
	 *            Total time in seconds
	 */
	private void setTime(int _totalTime) {
		int minutes, seconds;
		Log.e("Time", "" + _totalTime);
		if (_totalTime == 60) {
			minutes = 1;
			seconds = 0;
		} else if (_totalTime >= 60 * 60) {
			minutes = 60;
			seconds = 0;
		} else {
			minutes = _totalTime / 60;
			seconds = _totalTime % 60;
		}

		mins.setCurrentItem(minutes);
		secs.setCurrentItem(seconds);

		if (minutes == 60) {
			previousMins = 60;
			previousSecs = seconds;

			secs.setCurrentItem(0);
			secs.setViewAdapter(new NumericWheelAdapter(getActivity()
					.getApplicationContext(), 0, 0));
			secs.setCyclic(false);
		} else if (previousMins == 60) {
			secs.setViewAdapter(new NumericWheelAdapter(getActivity()
					.getApplicationContext(), 0, 60));

			secs.setCurrentItem(previousSecs);
			secs.setCyclic(true);
			previousMins = 0;
		}

		updateTime(minutes, seconds);
	}

	/**
	 * Update time on currSubP and updates the time text
	 *
	 * @param m_minutes
	 *            Time in minutes
     * @param m_seconds
     *            Time in seconds
	 */
	private void updateTime(int m_minutes, int m_seconds) {
		currSubP.set_totalTime((m_minutes * 60) + m_seconds);

		String timeText = "";
		/* Første del med mellemrum */
		if (m_minutes == 1) {
			timeText = "1 ";
			timeText += this.getString(R.string.minut);

		} else if (m_minutes != 0) {
			timeText = m_minutes + " ";
			timeText += this.getString(R.string.minutes);
		}

		/* Insert the devider if its needed */
		if (m_minutes != 0 && m_seconds != 0) {
			timeText += " " + this.getString(R.string.and_divider) + " ";
		}

		if (m_seconds == 1) {
			timeText += "1 ";
			timeText += this.getString(R.string.second);
		} else if (m_seconds != 0) {
			timeText += m_seconds + " ";
			timeText += this.getString(R.string.seconds);
		}

		timeDescription.setText(timeText);
		genDescription();
	}

	/**
	 * Initialize the color picker buttons, change colors here etc.
	 */
	private void initColorButtons() {
        gradientCheckBox = (GCheckBox)getActivity().findViewById(R.id.gCheckBox);
        gradientCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currSubP.gradient = isChecked;
            }
        });

		colorGradientButton1 = (Button) getActivity().findViewById(
				R.id.gradientButton_1);

		setColor(colorGradientButton1.getBackground(), currSubP.timeLeftColor);

		colorGradientButton1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                GColorPicker diag = new GColorPicker(v.getContext(), new GColorPicker.OnOkListener() {
                    @Override
                    public void OnOkClick(GColorPicker diag, int color) {
                        currSubP.timeLeftColor = color;
                        setColor(colorGradientButton1.getBackground(),
								currSubP.timeLeftColor);
                    }
                });
                diag.SetCurrColor(currSubP.timeLeftColor);
                diag.show();
			}
		});

		colorGradientButton2 = (Button) getActivity().findViewById(
				R.id.gradientButton_2);
		setColor(colorGradientButton2.getBackground(), currSubP.timeSpentColor);
		colorGradientButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                GColorPicker diag = new GColorPicker(v.getContext(), new GColorPicker.OnOkListener() {
                    @Override
                    public void OnOkClick(GColorPicker diag, int color) {
                        currSubP.timeSpentColor = color;
                        setColor(colorGradientButton2.getBackground(),
                                currSubP.timeSpentColor);
                    }
                });
                diag.SetCurrColor(currSubP.timeSpentColor);
                diag.show();
			}
		});

		colorFrameButton = (Button) getActivity().findViewById(
				R.id.frameColorButton);
		setColor(colorFrameButton.getBackground(), currSubP.frameColor);
		colorFrameButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                GColorPicker diag = new GColorPicker(v.getContext(), new GColorPicker.OnOkListener() {
                    @Override
                    public void OnOkClick(GColorPicker diag, int color) {
                        currSubP.frameColor = color;
                        setColor(colorFrameButton.getBackground(),
                                currSubP.frameColor);
                    }
                });
                diag.SetCurrColor(currSubP.frameColor);
                diag.show();
			}
		});

		colorBackgroundButton = (Button) getActivity().findViewById(
				R.id.backgroundColorButton);
		setColor(colorBackgroundButton.getBackground(), currSubP.bgcolor);
		colorBackgroundButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
                GColorPicker diag = new GColorPicker(v.getContext(), new GColorPicker.OnOkListener() {
                    @Override
                    public void OnOkClick(GColorPicker diag, int color) {
                        currSubP.bgcolor = color;
                        setColor(colorBackgroundButton.getBackground(),
                                currSubP.bgcolor);
                    }
                });
                diag.SetCurrColor(currSubP.bgcolor);
                diag.show();
			}

		});
	}

	private void setColor(Drawable d, int color) {
		PorterDuffColorFilter filter = new PorterDuffColorFilter(color,
				PorterDuff.Mode.SRC_ATOP);
		d.setColorFilter(filter);
	}

	/**
	 * Initialize the attachment button
	 */
    private void initAttachmentButton() {

        // Button for attachment
        attachmentButton = (GirafButton) getActivity().findViewById(
                R.id.customizeAttachment);
        // Set attachment button onclicklistener
        // 1. window
        attachmentButton.setOnClickListener(new OnClickListener() {

            // First onClick
            public void onClick(final View v) {
                if(guard.profileID == -1) {
                    GToast w = new GToast(getActivity().getApplicationContext(), "Du skal have valgt en profil for at kunne vedhæfte billeder", 2);
                    w.show();
                }
                else {
                    final ArrayList<formFactor> mode = guard.getMode();

                    final WDialog attachment1 = new WDialog(getActivity(),
                            R.string.select_attachment_dialog);


                    ModeAdapter adapter = new ModeAdapter(getActivity(),
                            android.R.layout.simple_list_item_1, mode);


                    if(child.SubProfiles().isEmpty()) {
                        adapter.remove(formFactor.Timer);
                    }
                    else if(!child.SubProfiles().isEmpty() && !mode.contains(formFactor.Timer)) {
                        adapter.add(formFactor.Timer);
                    }

                    attachment1.setAdapter(adapter);


                    attachment1.addButton(R.string.cancel, 1,
                            new OnClickListener() {

                                public void onClick(View arg0) {
                                    attachment1.cancel();
                                }
                            });
                    if (currSubP.getAttachment() != null) {
                        attachment1.addButton(R.string.clear, 2, new OnClickListener() {
                            public void onClick(View arg0) {
                                setAttachment(null);
                                attachment1.cancel();
                            }

                        });
                    }

                    // 2. window
                    attachment1.setOnItemClickListener(new OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // List<String> values = new ArrayList<String>();
                            final formFactor form = mode.get(position);

                            // Cast values to CharSequence and put it in the builder
                            final WDialog attachment2 = new WDialog(getActivity());

                            Intent i = new Intent();

                            attachment2.SetShade(false);
                            // New listview

                            ArrayAdapter adapter = null;

                            switch (form) {
                                case Timer:
                                    attachment2.setTitle(getString(R.string.attachment_dialog_pick_a_timer));
                                    final ArrayList<SubProfile> sp = child.SubProfiles();
                                    adapter = new SubProfileAttachmentAdapter(
                                            getActivity(),
                                            android.R.layout.simple_list_item_1,
                                            sp);
                                    break;
                                case SingleImg:
                                    i.setComponent(new ComponentName("dk.aau.cs.giraf.pictosearch",
                                            "dk.aau.cs.giraf.pictosearch.PictoAdminMain"));
                                    i.putExtra("purpose", "single");
                                    i.putExtra("currentChildID", guard.profileID);
                                    i.putExtra("currentGuardianID", guard.guardianId);

                                    startActivityForResult(i, 1);

                                    attachment1.cancel();

                                    break;
                                case SplitImg:
                                    i.setComponent(new ComponentName("dk.aau.cs.giraf.pictosearch",
                                            "dk.aau.cs.giraf.pictosearch.PictoAdminMain"));
                                    i.putExtra("purpose", "multi");
                                    i.putExtra("currentChildID", guard.profileID);
                                    i.putExtra("currentGuardianID", guard.guardianId);

                                    startActivityForResult(i, 2);

                                    attachment1.cancel();

                                    break;
                            }

                            attachment2.addButton(R.string.go_back, 1,
                                    new OnClickListener() {

                                        public void onClick(View arg0) {
                                            attachment2.cancel();
                                        }
                                    });

                            attachment2.addButton(R.string.cancel, 2,
                                    new OnClickListener() {

                                        public void onClick(View arg0) {
                                            attachment1.cancel();
                                            attachment2.cancel();
                                        }
                                    });
                            attachment2.setAdapter(adapter);
                            // 3. window

//                            attachment2.setOnItemClickListener(new OnItemClickListener() {
//                                public void onItemClick(
//                                        AdapterView<?> parent, View view,
//                                        int position, long id) {
//
//                                    // Cast values to CharSequence and put
//                                    // it in the builder
//                                    final WDialog attachment3 = new WDialog(
//                                            getActivity());
//                                    attachment3.SetShade(false);
//                                    // New listview
//                                    attachment3.addButton(R.string.go_back, 1,
//                                            new OnClickListener() {
//
//                                                public void onClick(
//                                                        View arg0) {
//                                                    attachment3.cancel();
//
//                                                }
//                                            });
//
//                                    attachment3.addButton(R.string.cancel,
//                                            2, new OnClickListener() {
//
//                                        public void onClick(
//                                                View arg0) {
//                                            attachment1.cancel();
//                                            attachment2.cancel();
//                                            attachment3.cancel();
//                                        }
//                                    });
//                                    ArrayAdapter adapter = null;
//                                    switch (form) {
//                                        case Timer:
//                                            Attachment attTimer = new Timer(child.SubProfiles().get(position));
//                                            setAttachment(attTimer);
//
//                                            GToast tAttachedTimer = GToast.makeText(getActivity(),
//                                                    getString(R.string.attached_timer_toast),
//                                                    Toast.LENGTH_SHORT);
//                                            tAttachedTimer.show();
//
//                                            attachment1.dismiss();
//                                            attachment2.dismiss();
//                                            break;
//                                        case SingleImg:
//                                            Attachment att = new SingleImg(guard.ArtList.get(position));
//                                            setAttachment(att);
//
//                                            GToast tAttachedPictogram = GToast.makeText(getActivity(),
//                                                    getString(R.string.attached_pictogram_toast),
//                                                    Toast.LENGTH_SHORT);
//                                            tAttachedPictogram.show();
//
//                                            attachment1.dismiss();
//                                            attachment2.dismiss();
//                                            break;
//                                        case SplitImg:
//                                            attachment3.setTitle(getString(R.string.attachment_dialog_split_right));
//                                            ArrayList<Art> splitArt = guard.ArtList;
//                                            final Art art1 = guard.ArtList
//                                                    .get(position);
//                                            adapter = new ArtAdapter(
//                                                    getActivity(),
//                                                    android.R.layout.simple_list_item_1,
//                                                    splitArt);
//
//                                            attachment3.setAdapter(adapter);
//
//                                            attachment3.setOnItemClickListener(new OnItemClickListener() {
//                                                public void onItemClick(
//                                                        AdapterView<?> parent,
//                                                        View view,
//                                                        int position,
//                                                        long id) {
//                                                    final Art art2 = guard.ArtList.get(position);
//                                                    Attachment attSplit = new SplitImg(art1, art2);
//                                                    setAttachment(attSplit);
//
//                                                    GToast tAttachedPictograms = GToast.makeText(getActivity(),
//                                                            getString(R.string.attached_pictograms_toast),
//                                                            Toast.LENGTH_SHORT);
//                                                    tAttachedPictograms.show();
//
//                                                    attachment1.dismiss();
//                                                    attachment2.dismiss();
//                                                    attachment3.dismiss();
//
//                                                }
//                                            });
//                                            attachment3.show();
//
//                                            break;
//                                    }
//                                }
//                            });
//
//                            attachment2.show();

                        }
                    });
                    attachment1.show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            int[] checkoutIds = data.getExtras().getIntArray("checkoutIds");
            if (checkoutIds.length == 0) {
                GToast t = GToast.makeText(MainActivity.context, "Ingen pictogrammer valgt.", Toast.LENGTH_LONG);
                t.show();
            }
            else {
                PictogramController pichelp = new PictogramController(getActivity());
                Pictogram pictogram = pichelp.getPictogramById(checkoutIds[0]);
                pictogram.getImage();

                Attachment att = new SingleImg(pictogram.getImage());
                setAttachment(att);
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            int[] checkoutIds = data.getExtras().getIntArray("checkoutIds");
            if (checkoutIds.length < 2) {
                GToast t = GToast.makeText(MainActivity.context, "Ingen pictogrammer valgt.", Toast.LENGTH_LONG);
                t.show();
            }
            else {
                PictogramController pichelp = new PictogramController(getActivity());
                Pictogram pictogram1 = pichelp.getPictogramById(checkoutIds[0]);
                pictogram1.getImage();
                Pictogram pictogram2 = pichelp.getPictogramById(checkoutIds[1]);
                pictogram2.getImage();

                Attachment att = new SplitImg(pictogram1.getImage(), pictogram2.getImage());
                setAttachment(att);
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 3) {
            int[] checkoutIds = data.getExtras().getIntArray("checkoutIds");
            if (checkoutIds.length == 0) {
                GToast t = GToast.makeText(MainActivity.context, "Ingen pictogrammer valgt.", Toast.LENGTH_LONG);
                t.show();
            }
            else {
                PictogramController pichelp = new PictogramController(getActivity());
                Pictogram pictogram = pichelp.getPictogramById(checkoutIds[0]);
                pictogram.getImage();

                Attachment att = new SingleImg(pictogram.getImage());
                setDonePicture(att);
                currSubP.setDoneArt(att);
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == 4) {
            int[] checkoutIds = data.getExtras().getIntArray("checkoutIds");
            if (checkoutIds.length < 2) {
                GToast t = GToast.makeText(MainActivity.context, "Ingen pictogrammer valgt.", Toast.LENGTH_LONG);
                t.show();
            }
            else {
                PictogramController pichelp = new PictogramController(getActivity());
                Pictogram pictogram1 = pichelp.getPictogramById(checkoutIds[0]);
                pictogram1.getImage();
                Pictogram pictogram2 = pichelp.getPictogramById(checkoutIds[1]);
                pictogram2.getImage();

                Attachment att = new SplitImg(pictogram1.getImage(), pictogram2.getImage());
                setDonePicture(att);
                currSubP.setDoneArt(att);
            }
        }
    }
    /**
     * Initializes the sound buttons forms in an array
     */
    public ArrayList<formFactor> getMode(){
        if(_soundlist == null){
            _soundlist = new ArrayList<formFactor>();
            _soundlist.add(formFactor.Sound1);
            _soundlist.add(formFactor.Sound2);
            _soundlist.add(formFactor.Sound3);
            _soundlist.add(formFactor.Sound4);
            _soundlist.add(formFactor.Sound5);
            _soundlist.add(formFactor.Sound6);
            return _soundlist;
        }  else {
            return _soundlist;
        }
    }
    /**
     * Initializes the sound buttons
     */
    private void initSoundButton(){
        String attachText = getString(R.string.SoundLoaded);
        SoundButton = (GirafButton) getActivity().findViewById(R.id.sound_button);
		SoundButtonText = (GTextView) getActivity().findViewById(R.id.sound_button_text);

        SoundButton.setOnClickListener(new OnClickListener() {

            // First onClick
            public void onClick(final View v) {
                int sound = 0;
                final ArrayList<formFactor> soundlist = getMode();

                final WDialog SoundDialogBox = new WDialog(getActivity(),
                        R.string.setting_dialog_description);

                final ModeAdapter adapter = new ModeAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, soundlist);

                SoundDialogBox.setAdapter(adapter);

                SoundDialogBox .addButton(R.string.close, 1,
                        new OnClickListener() {

                            public void onClick(View arg0) {
                                SoundDialogBox.cancel();
                            }
                        });

                sound = _soundlist.indexOf(adapter.getCount());

                SoundDialogBox .show();
                SoundDialogBox.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int sound2 = -1;
                        sound2 = _soundlist.indexOf(position);


                        switch (position){
                            case 0:
                                DoneScreenActivity.soundindex = R.raw.song;
                                SoundButtonText.setText("Standard Biip");
                                break;
                            case 1:
                                DoneScreenActivity.soundindex = R.raw.cow;
								SoundButtonText.setText("Ko");
                                break;
                            case 2:
                                DoneScreenActivity.soundindex = R.raw.bike;
								SoundButtonText.setText("Cykel horn");
                                break;
                            case 3:
                                DoneScreenActivity.soundindex = R.raw.bike2;
								SoundButtonText.setText("Cykel horn to");
                                break;
                            case 4:
                                DoneScreenActivity.soundindex = R.raw.waterdrop;
								SoundButtonText.setText("Dråbe");
                                break;
                            case 5:
                                DoneScreenActivity.soundindex = R.raw.chicken;
								SoundButtonText.setText("Høne");
                                break;
                            default:
                                DoneScreenActivity.soundindex = R.raw.song;
								SoundButtonText.setText("Vælg lyd");
                                break;
                        }
                        SoundDialogBox.cancel(); //lukker dialog når lyd er valgt
                    }
                });
            }
        });

    }


	/**
	 * Sets the attachment to subProfile, resets if subProfile == null
	 *
	 * @param att
	 *            Attachment to be attached to subProfile
	 */
	private void setAttachment(Attachment att) {
		int pictureRes = 0;
		int textRes = 0;
		int color = 0x00000000;
		int alpha = 255;
		String attachText = getString(R.string.attached);

		if (att != null) {
			currSubP.setAttachment(att);
			color = att.getColor();
			GETOUT: switch (att.getForm()) {
			case Timer:
				Timer tempT = (Timer) att;
				switch (tempT.formType()) {
				case Hourglass:
					pictureRes = R.drawable.thumbnail_hourglass;
					textRes = R.string.customize_hourglass_description;
					break GETOUT;
				case DigitalClock:
					pictureRes = R.drawable.thumbnail_digital;
					textRes = R.string.customize_digital_description;
					break GETOUT;
				case ProgressBar:
					pictureRes = R.drawable.thumbnail_progressbar;
					textRes = R.string.customize_progressbar_description;
					break GETOUT;
				case TimeTimer:
					pictureRes = R.drawable.thumbnail_timetimer;
					textRes = R.string.customize_timetimer_description;
					break GETOUT;
                case TimeTimerStandard:
                    pictureRes = R.drawable.thumbnail_timetimer;
                    textRes = R.string.customize_timetimer_standard_description;
                    break GETOUT;
				default:
					pictureRes = R.drawable.thumbnail_attachment;
					textRes = R.string.attachment_button_description;
					attachText = "";
					alpha = 0;
					break GETOUT;
				}
			case SingleImg:
				pictureRes = R.drawable.thumbnail_single_pic;
				textRes = R.string.customize_single_img_description;
				alpha = 0;
				break;
			case SplitImg:
				pictureRes = R.drawable.thumbnail_dual_pic;
				textRes = R.string.customize_split_img_description;
				alpha = 0;
				break;
			default:
				pictureRes = R.drawable.thumbnail_attachment;
				textRes = R.string.attachment_button_description;
				attachText = "";
				alpha = 0;
				break;
			}
		} else {
			currSubP.setAttachment(null);
			pictureRes = R.drawable.thumbnail_attachment;
			textRes = R.string.attachment_button_description;
			attachText = "";
			alpha = 0;
		}

		LayerDrawable ld = (LayerDrawable) getResources().getDrawable(R.drawable.attachment_layer);

		PorterDuffColorFilter filter = new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_ATOP);

		Drawable d = getResources().getDrawable(R.drawable.attachment_background);

		d.setAlpha(alpha);

		d.setColorFilter(filter);

		ld.setDrawableByLayerId(R.id.first_attachment_layer, d);

		ld.setDrawableByLayerId(R.id.second_attachment_layer, getResources().getDrawable(pictureRes));

		/*attachmentButton.setCompoundDrawablesWithIntrinsicBounds(null, getSingleDrawable(ld), null, null);

		attachmentButton.setText(textRes);*/
	}

    public Drawable getSingleDrawable(LayerDrawable layerDrawable){

        int resourceBitmapHeight = layerDrawable.getIntrinsicHeight(), resourceBitmapWidth = layerDrawable.getIntrinsicWidth();

        float widthInInches = 0.9f;

        int widthInPixels = (int)(widthInInches * getResources().getDisplayMetrics().densityDpi);
        int heightInPixels = (int)(widthInPixels * resourceBitmapHeight / resourceBitmapWidth);

        layerDrawable.setLayerInset(1, 0, 0, 0, 0);

        Bitmap bitmap = Bitmap.createBitmap(widthInPixels, heightInPixels, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, widthInPixels, heightInPixels);
        layerDrawable.draw(canvas);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        bitmapDrawable.setBounds(0, 0, widthInPixels, heightInPixels);

        return bitmapDrawable;
    }

	private void setDonePicture(Attachment att){
		int pictureRes = 0;
		if(att != null){
			switch(att.getForm()){
			case SingleImg:
				pictureRes = R.drawable.thumbnail_single_pic;
				break;
			case SplitImg:
				pictureRes = R.drawable.thumbnail_dual_pic;
				break;
			}
		} else {
			pictureRes = R.drawable.thumbnail_attachment;
		}

		LayerDrawable ld = (LayerDrawable) getResources().getDrawable(R.drawable.attachment_layer);

		PorterDuffColorFilter filter = new PorterDuffColorFilter(0xFFF,	PorterDuff.Mode.SRC_ATOP);

		Drawable d = getResources().getDrawable(R.drawable.attachment_background);

		d.setAlpha(0);
		d.setColorFilter(filter);

		ld.setDrawableByLayerId(R.id.first_attachment_layer, d);
		ld.setDrawableByLayerId(R.id.second_attachment_layer, getResources().getDrawable(pictureRes));

//		donePictureButton.setCompoundDrawablesWithIntrinsicBounds(null, ld,	null, null);
	}

	private void initDonePictureButton() {
		final ArrayList<formFactor> modeArray = new ArrayList<formFactor>();
		modeArray.add(formFactor.SingleImg);
		modeArray.add(formFactor.SplitImg);
		donePictureButton = (GirafButton) getActivity().findViewById(
				R.id.customizeDonescreen);
		donePictureButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                final WDialog doneDialog = new WDialog(getActivity(), R.string.donescreen_dialog_title);
                ArrayAdapter adapter = new ModeAdapter(getActivity(), android.R.layout.simple_list_item_1, modeArray);
                doneDialog.setAdapter(adapter);
                doneDialog.setOnItemClickListener(new OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        final formFactor mode = modeArray.get(position);

                        Intent i = new Intent();

                        switch (mode) {
                            case SingleImg:

                                i.setComponent(new ComponentName("dk.aau.cs.giraf.pictosearch",
                                        "dk.aau.cs.giraf.pictosearch.PictoAdminMain"));
                                i.putExtra("purpose", "single");
                                i.putExtra("currentChildID", guard.profileID);
                                i.putExtra("currentGuardianID", guard.guardianId);

                                startActivityForResult(i, 3);

                                doneDialog.cancel();

                                break;
                            case SplitImg:
                                i.setComponent(new ComponentName("dk.aau.cs.giraf.pictosearch",
                                        "dk.aau.cs.giraf.pictosearch.PictoAdminMain"));
                                i.putExtra("purpose", "multi");
                                i.putExtra("currentChildID", guard.profileID);
                                i.putExtra("currentGuardianID", guard.guardianId);

                                startActivityForResult(i, 4);

                                doneDialog.cancel();

                                break;
                        }
                    }
                });
                doneDialog.addButton(R.string.cancel, 1, new OnClickListener() {

                    public void onClick(View arg0) {
                        doneDialog.cancel();
                    }
                });
                if(currSubP.getDoneArt() != null) {
                    doneDialog.addButton(R.string.clear, 2, new OnClickListener() {

                        public void onClick(View arg0) {
                            currSubP.setDoneArt(null);
                            setDonePicture(null);
                            doneDialog.cancel();
                        }
                    });
                }
                doneDialog.show();
            }
        });
	}

	private void initBottomMenu() {
		initDonePictureButton();
		initSaveButton();
		//initSaveAsButton();
		initStartButton();
        initStopButton();
        //initSettingButton();
        initSoundButton();
        initFullScreenCheckBox();
        initProfileButton();

	}

	/**
	 * Initialize the save button
	 */
	 private void initSaveButton() {
		 saveButton = (GirafButton) getActivity().findViewById(R.id.customizeSave);
		 Drawable d;
		 if (currSubP.save && !guard.getChild().getLock()
				 && guard.getChild() != null) {
			 d = getResources().getDrawable(R.drawable.thumbnail_save);
			 saveButton.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 if (preSubP == null) {
						 final WDialog save1 = new WDialog(getActivity(),
								 R.string.save_button);

						 save1.addEditText(getName(), 1);
						 save1.addButton(R.string.ok, 2, new OnClickListener() {

							 public void onClick(View arg0) {
								 currSubP.name = save1.getEditTextText(1);

								 SubProfile m_savedSubprofile;
								 if (preSubP != null) {
									 m_savedSubprofile = currSubP.save(preSubP, true);
									 preSubP = null;
								 } else {
									 m_savedSubprofile = guard.getChild().save(
											 currSubP, false);
								 }
								 guard.subProfileID = m_savedSubprofile
										 .getId();
								 loadSettings(m_savedSubprofile);
                                 /* Fjernet profile delen*/
								 /*ChildFragment cf = (ChildFragment) getFragmentManager()
										 .findFragmentById(R.id.childFragment);*/

								 SubProfileFragment spf = new SubProfileFragment();
                                 spf = (SubProfileFragment) getFragmentManager()
										 .findFragmentById(R.id.subprofileFragment);
								 guard.profileID = guard.getChild()
										 .getProfileId();
								 //cf.loadChildren(); //Fjernet profile delen
								 spf.loadSubProfiles();
								 save1.dismiss();
							 }
						 });
						 save1.addButton(R.string.cancel, 3,
								 new OnClickListener() {

							 public void onClick(View arg0) {
								 save1.cancel();

							 }
						 });
						 save1.show();
					 } else {

						 SubProfile m_savedSubprofile;
						 if (preSubP != null && guard.getChild().SubProfiles().contains(preSubP)) {
							 m_savedSubprofile = currSubP.save(preSubP, true);
							 preSubP = null;
						 } else {
							 m_savedSubprofile = guard.getChild().save(currSubP, false);
						 }
						 guard.subProfileID = m_savedSubprofile.getId();
						 loadSettings(m_savedSubprofile);
                        /* Fjernet profile delen*/
						 /*ChildFragment cf = (ChildFragment) getFragmentManager()
								 .findFragmentById(R.id.childFragment);*/

						 SubProfileFragment spf = new SubProfileFragment();
                         spf = (SubProfileFragment) getFragmentManager()
								 .findFragmentById(R.id.subprofileFragment);
						 guard.profileID = guard.getChild().getProfileId();
						 //cf.loadChildren(); //Fjernet profile delen
						 spf.loadSubProfiles();
					 }
				 }
			 });
		 } else {
			 d = getResources().getDrawable(R.drawable.thumbnail_save_gray);
			 saveButton.setOnClickListener(new OnClickListener() {

				 public void onClick(View v) {
					 GToast t = GToast.makeText(getActivity(),
							 getString(R.string.cant_save),
                             Toast.LENGTH_SHORT);
					 t.show();
				 }
			 });
		 }

         /*saveButton.setText("Gem");//tjaa, godt spørgsmål :) det virker okay..
		 saveButton.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);*/
	 }

	 /**
	  * Sets the name of the profile if there is non And asks the user which name
	  * to save as
	  */
	 protected String getName() {
		 String name = "";
		 if (currSubP.name.isEmpty()) {

			 switch (currSubP.formType()) {
			 case Hourglass:
				 name += getString(R.string.customize_hourglass_description);
				 break;

			 case DigitalClock:
				 name += getString(R.string.customize_digital_description);
				 break;

			 case ProgressBar:
				 name += getString(R.string.customize_progressbar_description);
				 break;

			 case TimeTimer:
				 name += getString(R.string.customize_timetimer_description);
				 break;

             case TimeTimerStandard:
                 name += getString(R.string.customize_timetimer_standard_description);
                 break;
			 default:
				 name += "";
				 break;
			 }
		 } else {
			 name = currSubP.name;
		 }
		 return name;
	 }

    /**
     * Converts seconds to a timestamp of the form MM:SS
     *
     * @param seconds
     *        Total amount of seconds to be converted into timestamp
     * */
    private String toTimestamp(int seconds) {
        int millis = seconds*1000;
        // Converts milliseconds to minutes,
        // then to seconds which we need to subtract the amount of minutes from
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

     /**
      * Generates the description of the current timer configuration
      *
      * */
	 private void genDescription() {
		 String name = "";
		 String desc = "";

		 switch (currSubP.formType()) {
		 case Hourglass:
			 name += getString(R.string.customize_hourglass_description);
			 break;

		 case DigitalClock:
			 name += getString(R.string.customize_digital_description);
			 break;

		 case ProgressBar:
			 name += getString(R.string.customize_progressbar_description);
			 break;

		 case TimeTimer:
			 name += getString(R.string.customize_timetimer_description);
			 break;

         case TimeTimerStandard:
             name += getString(R.string.customize_timetimer_standard_description);
             break;

		 default:
			 name += "";
			 break;
		 }

         desc += name + " - (" + toTimestamp(currSubP.get_totalTime()) + ")";

         currSubP.desc = desc;
	 }

	 /**
	  * Initialize the Save As button
      *
	  */
//	 private void initSaveAsButton() {
//		 Drawable d;
//        /* Har også fjernet initSaveAsButton for samlet init gruppe
//		 saveAsButton = (Button) getActivity().findViewById(
//				 R.id.customize_save_as);
//         */
//		 // If this is a profile which is "saveable", enable the save
//		 // functionality
//		 if (currSubP.saveAs) {
//			 d = getResources().getDrawable(R.drawable.thumbnail_saveas);
//			 saveAsButton.setOnClickListener(new OnClickListener() {
//
//				 public void onClick(View v) {
//					 ArrayList<Child> child = guard.Children();
//					 ArrayAdapter adapter = new ChildAdapter(getActivity(),
//							 android.R.layout.simple_list_item_1, child);
//					 // Profile and pictogram loader
//					 final WDialog saveAs1 = new WDialog(getActivity(),
//							 R.string.choose_profile);
//					 saveAs1.setAdapter(adapter);
//					 saveAs1.addButton(R.string.cancel, 1,
//							 new OnClickListener() {
//						 public void onClick(View v) {
//							 saveAs1.cancel();
//						 }
//					 });
//
//					 saveAs1.setOnItemClickListener(new OnItemClickListener() {
//
//						 public void onItemClick(AdapterView<?> arg0, View arg1,
//								 final int position, long arg3) {
//							 // Name picker
//							 final WDialog saveAs2 = new WDialog(getActivity(),
//									 R.string.save_button);
//
//							 saveAs2.addEditText(getName(), 1);
//							 saveAs2.addButton(R.string.ok, 2,
//									 new OnClickListener() {
//
//								 public void onClick(View arg0) {
//									 currSubP.name = saveAs2.getEditTextText(1);
//
//									 Child c = guard.Children().get(position);
//									 getName();
//									 c.save(currSubP, false);
//									 guard.saveChild(c, currSubP);
//									 SubProfileFragment df  = new SubProfileFragment();
//                                     df = (SubProfileFragment) getFragmentManager()
//											 .findFragmentById(R.id.subprofileFragment);
//									 df.loadSubProfiles();
//
//									 String toastText = currSubP.name;
//									 toastText += " "
//											 + getActivity()
//											 .getApplicationContext()
//                                             .getText(
//                                                     R.string.was_saved_toast);
//									 toastText += " " + c.name;
//
//									 GToast toast = GToast.makeText(
//											 getActivity(), toastText,
//											 Toast.LENGTH_LONG);
//									 toast.show();
//                                    /* Fjernet profile delen*/
//									 /*ChildFragment cf = (ChildFragment) getFragmentManager()
//											 .findFragmentById(R.id.childFragment);*/
//
//									 SubProfileFragment spf  = new SubProfileFragment();
//                                     spf = (SubProfileFragment) getFragmentManager()
//											 .findFragmentById(R.id.subprofileFragment);
//									 guard.profileID = guard.getChild().getProfileId();
//									 //cf.loadChildren(); //Fjernet profile delen
//									 spf.loadSubProfiles();
//									 saveAs2.dismiss();
//									 saveAs1.dismiss();
//								 }
//							 });
//							 saveAs2.addButton(R.string.cancel, 3,
//									 new OnClickListener() {
//
//								 public void onClick(View arg0) {
//									 saveAs2.cancel();
//									 saveAs1.cancel();
//
//								 }
//							 });
//							 saveAs2.show();
//						 }
//					 });
//					 saveAs1.show();
//				 }
//			 });
//		 } else {
//			 d = getResources().getDrawable(R.drawable.thumbnail_saveas_gray);
//			 saveAsButton.setOnClickListener(new OnClickListener() {
//
//				 public void onClick(View v) {
//					 GToast t = GToast.makeText(getActivity(),
//                             getString(R.string.cant_save), Toast.LENGTH_SHORT);
//					 t.show();
//				 }
//			 });
//		 }
//
//		 saveAsButton.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
//
//	 }

	 /**
	  * Initialize the start button
	  */
     private void initStartButton() {
		 startButton = (GirafButton) getActivity().findViewById(
				 R.id.customizeStartButton);

		 if (currSubP.saveAs) {
             if (MainActivity.svc != null){
                 /*startButton.setText(R.string.restart_button);
                 startButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.thumbnail_restart), null, null);*/
             }
             else{
                 /*startButton.setText(R.string.start_button);
                 startButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.thumbnail_start), null, null);*/
             }

			 startButton.setOnClickListener(new OnClickListener() {

				 public void onClick(View v){
                     currSubP.addLastUsed(preSubP);
                     //guard.saveGuardian(currSubP);
                     currSubP.select();

                         DrawLibActivity.scale = TempChange;



                     if (DrawLibActivity.scale == 1){

                         if (MainActivity.svc != null){
                             getActivity().stopService(MainActivity.svc);
                             MainActivity.svc = new Intent(getActivity().getApplicationContext(), DrawLibActivity.class);
                             startActivity(MainActivity.svc);
                             Log.d("Overlay", "Restarted");
                         }
                         else{
                             MainActivity.svc = new Intent(getActivity().getApplicationContext(), DrawLibActivity.class);
                             startActivity(MainActivity.svc);
                             Log.d("Overlay", "Started");
                         }
                     }
                     else{
                         if (MainActivity.svc != null){
                             getActivity().stopService(MainActivity.svc);
                             MainActivity.svc = new Intent(getActivity(), Overlay.class);
                             getActivity().startService(MainActivity.svc);
                             Log.d("Overlay", "Restarted");
                         }
                         else{
                             MainActivity.svc = new Intent(getActivity(), Overlay.class);
                             getActivity().startService(MainActivity.svc);
                             Log.d("Overlay", "Started");
                         }
                     }
                     initBottomMenu();
				 }
			 });
		 }
         else {
             /*startButton.setText(R.string.start_button);
             startButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.thumbnail_start_gray), null, null);*/

             startButton.setOnClickListener(new OnClickListener() {

                 public void onClick(View v) {
                     GToast t = GToast.makeText(getActivity(),
                             getString(R.string.cant_start), Toast.LENGTH_SHORT);
                     t.show();
                 }
             });
         }
	 }

    /*
    *Initialize the stop button
    */
    private void initStopButton() {
        stopButton = (GirafButton)getActivity().findViewById(
                R.id.customizeStopButton);
        stopButton.refreshDrawableState();
        if (MainActivity.svc != null) {
            /*stopButton.setText(R.string.stop_button);
            stopButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.thumbnail_stop), null, null);*/

            stopButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v){
                    if (MainActivity.svc != null){
                        getActivity().stopService(MainActivity.svc);
                        MainActivity.svc = null;
                        Log.d("Overlay", "Stopped");
                        initBottomMenu();
                    }
                }
            });
        }
        else {
            //stopButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.thumbnail_stop_gray), null, null);
                stopButton.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        GToast t = GToast.makeText(getActivity(),
                                getString(R.string.cant_stop), Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
        }
    }

    /*
    *Initialize the FullScreen checkbox
    */
    private void initFullScreenCheckBox(){
        GCheckBox FullScreenCheckBox = (GCheckBox)getActivity().findViewById(R.id.FullScreenCheckBox);
        FullScreenCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                TempChange = isChecked == true ? 1 : 8;
            }
        });
    }

    /*
        * Initialize the setting button
        */
    /*private void initSettingButton(){
        settingButton = (GButton) getActivity().findViewById(
                R.id.customize_setting_button);

        settingButton.setOnClickListener(new OnClickListener() {

            // First onClick
            public void onClick(final View v) {
                final ArrayList<formFactor> mode = guard.getMode();

                final WDialog setting1 = new WDialog(getActivity(),
                        R.string.setting_dialog_description);

                setting1.addButton(R.string.close, 1,
                        new OnClickListener() {

                            public void onClick(View arg0) {
                                setting1.cancel();
                            }
                        });

                setting1.show();
            }
        });
    }
*/
        /*
    * Initialize the Profile button
    */
    private void initProfileButton(){


		profileButton = (GirafButton) getActivity().findViewById(
				R.id.customize_profile_button);

		// Set the change user button to open the change user dialog
		profileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity mainActivity = (MainActivity) getActivity();
				GirafProfileSelectorDialog changeUser = GirafProfileSelectorDialog.newInstance(mainActivity, guard.m_oGuard.getId(), false, false, "Vælg den borger du vil skifte til.", CHANGE_USER_SELECTOR_DIALOG);
				changeUser.show(mainActivity.getSupportFragmentManager(), "" + CHANGE_USER_SELECTOR_DIALOG);

			}
		});

		//profileButton = (GButtonProfileSelect) getActivity().findViewById(
        //        R.id.customize_profile_button);

        //Call the method setup with a Profile guardian, no currentProfile (which means that the guardian is the current Profile) and the onCloseListener


        /*profileButton.setup(guard.m_oGuard, null, new GButtonProfileSelect.onCloseListener() {
            @Override
            public void onClose(Profile guardianProfile, Profile currentProfile) {
                //If the guardian is the selected profile create GToast displaying the name
                if (currentProfile == null) {
               //If another current Profile is the selected profile create GToast displaying the name
                }
                else {

                    if (currentProfile.getRole() == Profile.Roles.CHILD) {
                        guard.profileID = currentProfile.getId();
                        if (children == null || !children.isEmpty()) {
                            for (Child _child : children) {
                                if (_child.getProfileId() == guard.profileID) {
                                    children.get(children.indexOf(_child)).select();
                                    child = _child;
                                    break;
                                }
                            }
                        }

                        GTextView tv = (GTextView) getActivity().findViewById(R.id.customizeHeader);
                        CharSequence cs;
                        if (child != null) {
                            cs = child.name;
                        } else {
                            cs = "Ingen Valgt Profil";
                        }
                        tv.setText(cs);
                        SubProfileFragment spf = new SubProfileFragment();
                        spf = (SubProfileFragment) getFragmentManager()
                                .findFragmentById(R.id.subprofileFragment);
                        spf.loadSubProfiles();
                        setDefaultProfile();
                    }
                }


            }
        });*/
    }

	 /**
	  * Sets the predefined settings of the chosen subprofile
	  *
	  * @param subProfile
	  *            The Subprofile chosen
	  */

	 public void loadSettings(SubProfile subProfile) {
		 currSubP = subProfile.copy();
		 preSubP = subProfile;

		 /* Set Style */
		 selectStyle(currSubP.formType());

		 /* Set Time */
		 setTime(currSubP.get_totalTime());

		 /* Set Colors */
		 setColor(colorGradientButton1.getBackground(), currSubP.timeLeftColor);
		 setColor(colorGradientButton2.getBackground(), currSubP.timeSpentColor);
		 setColor(colorFrameButton.getBackground(), currSubP.frameColor);
		 setColor(colorBackgroundButton.getBackground(), currSubP.bgcolor);

		 /* Set Attachment */
		 setAttachment(currSubP.getAttachment());

		 /* Set Done picture */
		 setDonePicture(currSubP.getDoneArt());
	 }

	 public void reloadCustomize() {
		 /* Set Style */
		 selectStyle(currSubP.formType());

		 /* Set Time */
		 setTime(currSubP.get_totalTime());

		 /* Set Colors */
		 setColor(colorGradientButton1.getBackground(), currSubP.timeLeftColor);
		 setColor(colorGradientButton2.getBackground(), currSubP.timeSpentColor);
		 setColor(colorFrameButton.getBackground(), currSubP.frameColor);
		 setColor(colorBackgroundButton.getBackground(), currSubP.bgcolor);

		 /* Set Attachment */
		 setAttachment(currSubP.getAttachment());

		 /* Set Done picture */
		 setDonePicture(currSubP.getDoneArt());
	 }

	public void onProfileSelected(int i, Profile profile) {

		final GWidgetProfileSelection widgetProfileSelection = (GWidgetProfileSelection) getActivity().findViewById(R.id.profile_widget);
		Helper h = new Helper(getActivity());
		// Fetch the profile picture
		Helper helper = new Helper(null);

		Bitmap profilePicture = h.profilesHelper.getById(profile.getId()).getImage();
				//
				//helper.profilesHelper.getImage(profile);
		// If there were no profile picture use the default template
		if (profilePicture == null) {
			// Fetch the default template
			profilePicture = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.no_profile_pic)).getBitmap();
		}

		// Set the profile picture
		widgetProfileSelection.setImageBitmap(profilePicture);

		guard.profileID = profile.getId();
		if (children == null || !children.isEmpty()) {
			for (Child _child : children) {
				if (_child.getProfileId() == guard.profileID) {
					children.get(children.indexOf(_child)).select();
					child = _child;
					break;
				}
			}
		}

		GTextView tv = (GTextView) getActivity().findViewById(R.id.customizeHeader);
		CharSequence cs;
		if (child != null) {
			cs = child.name;
		} else {
			cs = "Ingen Valgt Profil";
		}
		tv.setText(cs);
		SubProfileFragment spf = new SubProfileFragment();
		spf = (SubProfileFragment) getFragmentManager()
				.findFragmentById(R.id.subprofileFragment);
		spf.loadSubProfiles();
		setDefaultProfile();
	}
}
/* GOING FOR LEET */
/* GOING FOR LEET */
/* GOING FOR LEET */
/* GOING FOR LEET */