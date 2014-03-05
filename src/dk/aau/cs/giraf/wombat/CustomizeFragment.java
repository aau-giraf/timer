package dk.aau.cs.giraf.wombat;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import dk.aau.cs.giraf.TimerLib.Art;
import dk.aau.cs.giraf.TimerLib.Attachment;
import dk.aau.cs.giraf.TimerLib.Child;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.SingleImg;
import dk.aau.cs.giraf.TimerLib.SplitImg;
import dk.aau.cs.giraf.TimerLib.SubProfile;
import dk.aau.cs.giraf.TimerLib.Timer;
import dk.aau.cs.giraf.TimerLib.formFactor;
import dk.aau.cs.giraf.wombat.drawlib.DrawLibActivity;
/**
 * This class is a Fragment which which is used to customize SubProfiles
 * Layer: Layout
 *
 */
public class CustomizeFragment extends Fragment {
	private SubProfile preSubP;
	private SubProfile currSubP;
	private Guardian guard = Guardian.getInstance();

	private Button hourglassButton;
	private Button timetimerButton;
	private Button progressbarButton;
	private Button digitalButton;
	private Button startButton;
    private Button switchLayoutButton;
	private Button saveButton;
	private Button saveAsButton;
	private Button attachmentButton;
	private Button donePictureButton;
	private Button colorGradientButton1;
	private Button colorGradientButton2;
	private Button colorFrameButton;
	private Button colorBackgroundButton;

	private WCheckbox gradientButton;

	private WheelView mins;
	private WheelView secs;

	private TextView timeDescription;
    SharedPreferences pref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Write Tag = Detail and Text = Detail Opened in the LogCat
		Log.e("Customize", "Customize Opened");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Populate the fragment according to the details layout

        pref = this.getActivity().getSharedPreferences("LayoutPrefFile", 0);
        if (pref.getBoolean("verticalLayout", false)){
            return inflater.inflate(R.layout.customize, container, false);
        } else {
            return inflater.inflate(R.layout.customize_vertical, container, false);

        }

	}

	@Override
	// Start the list empty
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button b = (Button) getActivity()
				.findViewById(R.id.new_template_button);
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				guard.subProfileID = -1;
				SubProfileFragment spf = (SubProfileFragment) getFragmentManager()
						.findFragmentById(R.id.subprofileFragment);
				spf.loadSubProfiles();
				CustomizeFragment cf = (CustomizeFragment) getFragmentManager()
						.findFragmentById(R.id.customizeFragment);
				cf.setDefaultProfile();
				Toast t = Toast.makeText(getActivity(),
						getString(R.string.new_template_button_text),
						Toast.LENGTH_SHORT);
				t.show();
			}
		});

		currSubP = new SubProfile("", "",
                                  getResources().getColor(R.color.DarkerGray2),
                                  getResources().getColor(R.color.Red),
                                  getResources().getColor(R.color.Gray),
                                  getResources().getColor(R.color.Black), 600, false);
		currSubP.save = false;
		currSubP.saveAs = false;

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
	}

	public void setDefaultProfile() {
		currSubP = new SubProfile("", "",
                getResources().getColor(R.color.DarkerGray2),
                getResources().getColor(R.color.Red),
                getResources().getColor(R.color.Gray),
                getResources().getColor(R.color.Black), 600, false);
		currSubP.save = false;
		currSubP.saveAs = false;

		preSubP = null;

		reloadCustomize();
	}

	/**
	 * Initialize the style chooser buttons
	 */
	private void initStyleChoser() {
		hourglassButton = (Button) getActivity().findViewById(R.id.houglassButton);
		hourglassButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.Hourglass);
			}
		});

		timetimerButton = (Button) getActivity().findViewById(
				R.id.timetimerButton);
		timetimerButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.TimeTimer);

			}
		});

		progressbarButton = (Button) getActivity().findViewById(
				R.id.progressbarButton);
		progressbarButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				selectStyle(formFactor.ProgressBar);

			}
		});

		digitalButton = (Button) getActivity().findViewById(R.id.digitalButton);
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
			hourglassButton.setSelected(true);
			setSave();
		} else {
			hourglassButton.setSelected(false);
		}

		if (formType == formFactor.TimeTimer) {
			currSubP = currSubP.toTimeTimer();
			timetimerButton.setSelected(true);
			setSave();
		} else {
			timetimerButton.setSelected(false);
		}
		if (formType == formFactor.ProgressBar) {
			currSubP = currSubP.toProgressBar();
			progressbarButton.setSelected(true);
			setSave();
		} else {
			progressbarButton.setSelected(false);
		}
		if (formType == formFactor.DigitalClock) {
			currSubP = currSubP.toDigitalClock();
			digitalButton.setSelected(true);
			setSave();
		} else {
			digitalButton.setSelected(false);
		}
		genDescription();
		initBottomMenu();
	}

	private boolean setSave() {
		boolean complete = false;
		if (guard.getChild() != null) {
			currSubP.save = true;
		} else {
			currSubP.save = false;
		}
		currSubP.saveAs = true;

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
		timeDescription = (TextView) getActivity().findViewById(R.id.showTime);
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
			}
		});

		secs.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateTime(mins.getCurrentItem(), secs.getCurrentItem());
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
        /*
		LinearLayout mLL = (LinearLayout) getActivity().findViewById(
				R.id.button_gradient_layout);
		gradientButton = new WCheckbox(getActivity());
		gradientButton.setOnClickListener(currSubP.gradient, new OnClickListener() {
			
			public void onClick(View v) {
				currSubP.gradient = gradientButton.changeCheckedState();
			}
		});
		mLL.addView(gradientButton);
		*/
		colorGradientButton1 = (Button) getActivity().findViewById(
				R.id.gradientButton_1);

		setColor(colorGradientButton1.getBackground(), currSubP.timeLeftColor);

		colorGradientButton1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						currSubP.timeLeftColor, new OnAmbilWarnaListener() {
					public void onCancel(AmbilWarnaDialog dialog) {
					}

					public void onOk(AmbilWarnaDialog dialog, int color) {
						currSubP.timeLeftColor = color;
						setColor(colorGradientButton1.getBackground(),
								currSubP.timeLeftColor);
					}
				});
				dialog.show();
			}
		});

		colorGradientButton2 = (Button) getActivity().findViewById(
				R.id.gradientButton_2);
		setColor(colorGradientButton2.getBackground(), currSubP.timeSpentColor);
		colorGradientButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						currSubP.timeSpentColor, new OnAmbilWarnaListener() {
					public void onCancel(AmbilWarnaDialog dialog) {
					}

					public void onOk(AmbilWarnaDialog dialog, int color) {
						currSubP.timeSpentColor = color;
						setColor(colorGradientButton2.getBackground(),
								currSubP.timeSpentColor);
					}
				});
				dialog.show();
			}
		});

		colorFrameButton = (Button) getActivity().findViewById(
				R.id.frameColorButton);
		setColor(colorFrameButton.getBackground(), currSubP.frameColor);
		colorFrameButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						currSubP.frameColor, new OnAmbilWarnaListener() {
					public void onCancel(AmbilWarnaDialog dialog) {
					}

					public void onOk(AmbilWarnaDialog dialog, int color) {
						currSubP.frameColor = color;
						setColor(colorFrameButton.getBackground(),
								currSubP.frameColor);
					}
				});
				dialog.show();
			}
		});

		colorBackgroundButton = (Button) getActivity().findViewById(
				R.id.backgroundColorButton);
		setColor(colorBackgroundButton.getBackground(), currSubP.bgcolor);
		colorBackgroundButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
						currSubP.bgcolor, new OnAmbilWarnaListener() {
					public void onCancel(AmbilWarnaDialog dialog) {
					}

					public void onOk(AmbilWarnaDialog dialog, int color) {
						currSubP.bgcolor = color;
						setColor(colorBackgroundButton.getBackground(),
								currSubP.bgcolor);
					}
				});
				dialog.show();
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
		attachmentButton = (Button) getActivity().findViewById(
				R.id.customize_attachment);
		// Set attachment button onclicklistener
		// 1. window
		attachmentButton.setOnClickListener(new OnClickListener() {

			// First onClick
			public void onClick(final View v) {
				final ArrayList<formFactor> mode = guard.getMode();

				final WDialog attachment1 = new WDialog(getActivity(),
						R.string.attachment_dialog_description);

				ModeAdapter adapter = new ModeAdapter(getActivity(),
						android.R.layout.simple_list_item_1, mode);

				attachment1.setAdapter(adapter);

				attachment1.addButton(R.string.cancel, 1,
						new OnClickListener() {

					public void onClick(View arg0) {
						attachment1.cancel();
					}
				});

				// 2. window
				attachment1.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// List<String> values = new ArrayList<String>();
						final formFactor form = mode.get(position);

						// Cast values to CharSequence and put it in the builder
						final WDialog attachment2 = new WDialog(getActivity());

						// New listview

						ArrayAdapter adapter = null;

						switch (form) {
						case Timer:
							attachment2.setTitle(getString(R.string.attachment_dialog_pick_a_profile));
							ArrayList<Child> child = guard.publishList();
							adapter = new ChildAdapter(getActivity(),
									android.R.layout.simple_list_item_1, child);
							break;
						case SingleImg:
							attachment2.setTitle(getString(R.string.attachment_dialog_pick_a_picture));
							ArrayList<Art> art = guard.ArtList;
							adapter = new ArtAdapter(getActivity(),
									android.R.layout.simple_list_item_1, art);
							break;
						case SplitImg:
							attachment2.setTitle(getString(R.string.attachment_dialog_split_left));
							ArrayList<Art> splitArt = guard.ArtList;
							adapter = new ArtAdapter(getActivity(),
									android.R.layout.simple_list_item_1,
									splitArt);
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

						attachment2.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(
									AdapterView<?> parent, View view,
									int position, long id) {

								// Cast values to CharSequence and put
								// it in the builder
								final WDialog attachment3 = new WDialog(
										getActivity());
								// New listview
								attachment3.addButton(R.string.go_back, 1,
										new OnClickListener() {

									public void onClick(
											View arg0) {
										attachment3.cancel();

									}
								});

								attachment3.addButton(R.string.cancel,
										2, new OnClickListener() {

									public void onClick(
											View arg0) {
										attachment1.cancel();
										attachment2.cancel();
										attachment3.cancel();
									}
								});
								ArrayAdapter adapter = null;
								switch (form) {
								case Timer:
									attachment3.setTitle(getString(R.string.attachment_dialog_description));
									final ArrayList<SubProfile> sp = guard
											.publishList().get(position).SubProfiles();
									adapter = new SubProfileAdapter(
											getActivity(),
											android.R.layout.simple_list_item_1,
											sp);

									attachment3.setAdapter(adapter);

									attachment3.setOnItemClickListener(new OnItemClickListener() {
                                        public void onItemClick(
                                                AdapterView<?> parent,
                                                View view,
                                                int position,
                                                long id) {

                                            Attachment attTimer = new Timer(
                                                    sp.get(position));
                                            setAttachment(attTimer);

                                            Toast t = Toast.makeText(getActivity(),
                                                    getString(R.string.attached_timer_toast),
                                                    Toast.LENGTH_SHORT);
                                            t.show();

                                            attachment1.dismiss();
                                            attachment2.dismiss();
                                            attachment3.dismiss();

                                        }
                                    });

									attachment3.show();

									break;
								case SingleImg:
									Attachment att = new SingleImg(guard.ArtList.get(position));
									setAttachment(att);

                                    Toast t = Toast.makeText(getActivity(),
                                            getString(R.string.attached_pictogram_toast),
                                            Toast.LENGTH_SHORT);
                                    t.show();

									attachment1.dismiss();
									attachment2.dismiss();
									break;
								case SplitImg:
									attachment3.setTitle(getString(R.string.attachment_dialog_split_right));
									ArrayList<Art> splitArt = guard.ArtList;
									final Art art1 = guard.ArtList
											.get(position);
									adapter = new ArtAdapter(
											getActivity(),
											android.R.layout.simple_list_item_1,
											splitArt);

									attachment3.setAdapter(adapter);

									attachment3.setOnItemClickListener(new OnItemClickListener() {
                                        public void onItemClick(
                                                AdapterView<?> parent,
                                                View view,
                                                int position,
                                                long id) {
                                            final Art art2 = guard.ArtList.get(position);
                                            Attachment attSplit = new SplitImg(art1, art2);
                                            setAttachment(attSplit);

                                            Toast t = Toast.makeText(getActivity(),
                                                    getString(R.string.attached_pictograms_toast),
                                                    Toast.LENGTH_SHORT);
                                            t.show();

                                            attachment1.dismiss();
                                            attachment2.dismiss();
                                            attachment3.dismiss();

                                        }
                                    });
									attachment3.show();

									break;
								}
							}
						});

						attachment2.show();

					}
				});
				attachment1.show();
			}
		});

		// Long click to remove
		attachmentButton.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {
				setAttachment(null);
				return true;
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

		TextView attView = (TextView) getActivity().findViewById(
				R.id.customize_attachment_text);

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

		attachmentButton.setCompoundDrawablesWithIntrinsicBounds(null, ld,null, null);
		
		attachmentButton.setText(textRes);
		
		attView.setText(attachText);
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
		
		donePictureButton.setCompoundDrawablesWithIntrinsicBounds(null, ld,	null, null);
	}

	private void initDonePictureButton() {
		final ArrayList<formFactor> modeArray = new ArrayList<formFactor>();
		modeArray.add(formFactor.SingleImg);
		modeArray.add(formFactor.SplitImg);
		donePictureButton = (Button) getActivity().findViewById(
				R.id.customize_donescreen);
		donePictureButton.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				final WDialog doneDialog = new WDialog(getActivity(), R.string.donescreen_dialog_title);
				ArrayAdapter adapter = new ModeAdapter(getActivity(),android.R.layout.simple_list_item_1, modeArray);	
				doneDialog.setAdapter(adapter);
				doneDialog.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						final formFactor mode = modeArray.get(position);

						final ArrayAdapter artList = new ArtAdapter(getActivity(),
                                android.R.layout.simple_list_item_1, guard.ArtList);
						switch(mode){
						case SingleImg:
							final WDialog singleDialog = new WDialog(getActivity(),R.string.donescreen_dialog_single);
							singleDialog.setAdapter(artList);
							singleDialog.setOnItemClickListener(new OnItemClickListener() {

								public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
									Attachment atta1 = new SingleImg(guard.ArtList.get(position));
									setDonePicture(atta1);
									currSubP.setDoneArt(atta1);
									doneDialog.dismiss();
									singleDialog.dismiss();

                                    Toast t = Toast.makeText(getActivity(),
                                            getString(R.string.attached_pictogram_toast),
                                            Toast.LENGTH_SHORT);
                                    t.show();

								}
							});
							singleDialog.addButton(R.string.cancel, 1, new OnClickListener() {

								public void onClick(View arg0) {
									doneDialog.cancel();
									singleDialog.cancel();
								}
							});
							singleDialog.show();
							break;
						case SplitImg:
							final WDialog dualDialog = new WDialog(getActivity(),R.string.donescreen_dialog_left);
							dualDialog.setAdapter(artList);
							dualDialog.setOnItemClickListener(new OnItemClickListener() {

								public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
									final Art art1 = guard.ArtList.get(position);
									final WDialog dialog1 = new WDialog(getActivity(),R.string.donescreen_dialog_right);
									dialog1.setAdapter(artList);
									dialog1.setOnItemClickListener(new OnItemClickListener() {

										public void onItemClick(AdapterView<?> parent, View view,
												int position, long id) {
											final Art art2 = guard.ArtList.get(position);
											Attachment atta = new SplitImg(art1,art2);
											currSubP.setDoneArt(atta);
											setDonePicture(atta);
											doneDialog.dismiss();
											dualDialog.dismiss();
											dialog1.dismiss();

                                            Toast t = Toast.makeText(getActivity(),
                                                    getString(R.string.attached_pictograms_toast),
                                                    Toast.LENGTH_SHORT);
                                            t.show();
										}
									});
									dialog1.addButton(R.string.cancel, 1, new OnClickListener() {

										public void onClick(View arg0) {
											doneDialog.cancel();
											dualDialog.cancel();
											dialog1.cancel();
										}
									});
									dialog1.show();	
								}
							});
							dualDialog.addButton(R.string.cancel, 1, new OnClickListener() {

								public void onClick(View arg0) {
									doneDialog.cancel();
									dualDialog.cancel();
								}
							});
							dualDialog.show();
							break;
						}
					}
				});
				doneDialog.addButton(R.string.cancel, 1, new OnClickListener() {

					public void onClick(View arg0) {
						doneDialog.cancel();
					}
				});
				doneDialog.show();
			}
		});
		donePictureButton.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {
				currSubP.setDoneArt(null);
				setDonePicture(null);
				return true;
			}
		});
	}

	private void initBottomMenu() {
		initDonePictureButton();
		initSaveButton();
		initSaveAsButton();
        initSwitchButton();
		initStartButton();
	}

	/**
	 * Initialize the save button
	 */
	 private void initSaveButton() {
		 saveButton = (Button) getActivity().findViewById(R.id.customize_save);
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
								 guard.publishList()
								 .get(guard.profilePosition).select();

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
                                 /* Fjernet profile delen
								 ChildFragment cf = (ChildFragment) getFragmentManager()
										 .findFragmentById(R.id.childFragment);
							     */
								 SubProfileFragment spf = (SubProfileFragment) getFragmentManager()
										 .findFragmentById(R.id.subprofileFragment);
								 guard.profileID = guard.getChild()
										 .getProfileId();
								 // cf.loadChildren(); Fjernet profile delen
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
						 guard.publishList().get(guard.profilePosition).select();

						 SubProfile m_savedSubprofile;
						 if (preSubP != null) {
							 m_savedSubprofile = currSubP.save(preSubP, true);
							 preSubP = null;
						 } else {
							 m_savedSubprofile = guard.getChild().save(currSubP, false);
						 }
						 guard.subProfileID = m_savedSubprofile.getId();
						 loadSettings(m_savedSubprofile);
                        /* Fjernet profile delen
						 ChildFragment cf = (ChildFragment) getFragmentManager()
								 .findFragmentById(R.id.childFragment);
						*/
						 SubProfileFragment spf = (SubProfileFragment) getFragmentManager()
								 .findFragmentById(R.id.subprofileFragment);
						 guard.profileID = guard.getChild().getProfileId();
						 // cf.loadChildren(); Fjernet profile delen
						 spf.loadSubProfiles();
					 }
				 }
			 });
		 } else {
			 d = getResources().getDrawable(R.drawable.thumbnail_save_gray);
			 saveButton.setOnClickListener(new OnClickListener() {

				 public void onClick(View v) {
					 Toast t = Toast.makeText(getActivity(),
							 getString(R.string.cant_save),
                             Toast.LENGTH_SHORT);
					 t.show();
				 }
			 });
		 }

		 saveButton.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
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
	 private void initSaveAsButton() {
		 Drawable d;

		 saveAsButton = (Button) getActivity().findViewById(
				 R.id.customize_save_as);
		 // If this is a profile which is "saveable", enable the save
		 // functionality
		 if (currSubP.saveAs) {
			 d = getResources().getDrawable(R.drawable.thumbnail_saveas);
			 saveAsButton.setOnClickListener(new OnClickListener() {

				 public void onClick(View v) {
					 ArrayList<Child> child = guard.Children();
					 ArrayAdapter adapter = new ChildAdapter(getActivity(),
							 android.R.layout.simple_list_item_1, child);
					 // Profile and pictogram loader
					 final WDialog saveAs1 = new WDialog(getActivity(),
							 R.string.choose_profile);
					 saveAs1.setAdapter(adapter);
					 saveAs1.addButton(R.string.cancel, 1,
							 new OnClickListener() {
						 public void onClick(View v) {
							 saveAs1.cancel();
						 }
					 });

					 saveAs1.setOnItemClickListener(new OnItemClickListener() {

						 public void onItemClick(AdapterView<?> arg0, View arg1,
								 final int position, long arg3) {
							 // Name picker
							 final WDialog saveAs2 = new WDialog(getActivity(),
									 R.string.save_button);

							 saveAs2.addEditText(getName(), 1);
							 saveAs2.addButton(R.string.ok, 2,
									 new OnClickListener() {

								 public void onClick(View arg0) {
									 currSubP.name = saveAs2.getEditTextText(1);
									 guard.publishList().get(guard.profilePosition).select();

									 Child c = guard.Children().get(position);
									 getName();
									 c.save(currSubP, false);
									 guard.saveChild(c, currSubP);
									 SubProfileFragment df = (SubProfileFragment) getFragmentManager()
											 .findFragmentById(R.id.subprofileFragment);
									 df.loadSubProfiles();

									 String toastText = currSubP.name;
									 toastText += " "
											 + getActivity()
											 .getApplicationContext()
                                             .getText(
                                                     R.string.was_saved_toast);
									 toastText += " " + c.name;

									 Toast toast = Toast.makeText(
											 getActivity(), toastText,
											 Toast.LENGTH_LONG);
									 toast.show();
                                    /* Fjernet profile delen
									 ChildFragment cf = (ChildFragment) getFragmentManager()
											 .findFragmentById(R.id.childFragment);
									*/
									 SubProfileFragment spf = (SubProfileFragment) getFragmentManager()
											 .findFragmentById(R.id.subprofileFragment);
									 guard.profileID = guard.getChild().getProfileId();
									 // cf.loadChildren(); Fjernet profile delen
									 spf.loadSubProfiles();
									 saveAs2.dismiss();
									 saveAs1.dismiss();
								 }
							 });
							 saveAs2.addButton(R.string.cancel, 3,
									 new OnClickListener() {

								 public void onClick(View arg0) {
									 saveAs2.cancel();
									 saveAs1.cancel();

								 }
							 });
							 saveAs2.show();
						 }
					 });
					 saveAs1.show();
				 }
			 });
		 } else {
			 d = getResources().getDrawable(R.drawable.thumbnail_saveas_gray);
			 saveAsButton.setOnClickListener(new OnClickListener() {

				 public void onClick(View v) {
					 Toast t = Toast.makeText(getActivity(),
							 getString(R.string.cant_save), Toast.LENGTH_SHORT);
					 t.show();
				 }
			 });
		 }

		 saveAsButton.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);

	 }

    /**
     * Initialize the switch button
     */
    private void initSwitchButton() {
        switchLayoutButton = (Button) getActivity().findViewById(
                R.id.switch_activity_button);

                    switchLayoutButton.setOnClickListener(new OnClickListener() {
                @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = pref.edit();
                if (pref.getBoolean("verticalLayout", false)){
                    editor.clear();
                    editor.putBoolean("verticalLayout", false);
                } else {
                    editor.clear();
                    editor.putBoolean("verticalLayout", true);
                }
                editor.commit();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                getActivity().startActivity(intent);
            }
        });
        /*
        Drawable d;
        if (currSubP.saveAs) {
            d = getResources().getDrawable(R.drawable.thumbnail_start);
            startButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    currSubP.addLastUsed(preSubP);
                    guard.saveGuardian(currSubP);
                    currSubP.select();
                    Intent i = new Intent(
                            getActivity().getApplicationContext(),
                            DrawLibActivity.class);
                    startActivity(i);
                }
            });
        } else {
            d = getResources().getDrawable(R.drawable.thumbnail_start_gray);
            startButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Toast t = Toast.makeText(getActivity(),
                            getString(R.string.cant_start), Toast.LENGTH_SHORT);
                    t.show();
                }
            });
            */
        }

	 /**
	  * Initialize the start button
	  */
	 private void initStartButton() {
		 startButton = (Button) getActivity().findViewById(
				 R.id.customize_start_button);
		 Drawable d;
		 if (currSubP.saveAs) {
			 d = getResources().getDrawable(R.drawable.thumbnail_start);
			 startButton.setOnClickListener(new OnClickListener() {

				 public void onClick(View v) {
					 currSubP.addLastUsed(preSubP);
					 guard.saveGuardian(currSubP);
					 currSubP.select();
					 Intent i = new Intent(
							 getActivity().getApplicationContext(),
							 DrawLibActivity.class);
					 startActivity(i);
				 }
			 });
		 } else {
			 d = getResources().getDrawable(R.drawable.thumbnail_start_gray);
			 startButton.setOnClickListener(new OnClickListener() {

				 public void onClick(View v) {
					 Toast t = Toast.makeText(getActivity(),
							 getString(R.string.cant_start), Toast.LENGTH_SHORT);
					 t.show();
				 }
			 });
		 }

		 startButton.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
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
		 gradientButton.setChecked(currSubP.gradient);
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
		 gradientButton.setChecked(currSubP.gradient);
		 setColor(colorGradientButton1.getBackground(), currSubP.timeLeftColor);
		 setColor(colorGradientButton2.getBackground(), currSubP.timeSpentColor);
		 setColor(colorFrameButton.getBackground(), currSubP.frameColor);
		 setColor(colorBackgroundButton.getBackground(), currSubP.bgcolor);

		 /* Set Attachment */
		 setAttachment(currSubP.getAttachment());
		 
		 /* Set Done picture */
		 setDonePicture(currSubP.getDoneArt());
	 }
}
/* GOING FOR LEET */
/* GOING FOR LEET */
/* GOING FOR LEET */
/* GOING FOR LEET */