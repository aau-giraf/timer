package dk.aau.cs.giraf.wombat;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
/**
 * This class is a custom button which is used for the gradient button
 * Layer: Layout
 *
 */
public class WCheckbox extends Button{

	private boolean mChecked;
	
	/**
	 * Constructor for the checkbox, sets the background and size
	 * @param c
	 * 		Context of the application, used to initialize the super constructor
	 */
	public WCheckbox(Context c) {
		super(c);
		
		this.setBackgroundResource(R.drawable.btn);

		this.setLayoutParams(
				new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 
						ViewGroup.LayoutParams.MATCH_PARENT));
	}

	/**
	 * Add or remove the checkmark according to mChecked
	 */
	private void changePicture(){
		if(mChecked){
			this.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.checkmark, 0, 0);
		} else {
			this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
		
	}
	
	/**
	 * Standard onClickListener, including a checked state
	 * @param checked
	 * 		Value of the checkbox at initialization
	 * @param onClickListener
	 * 		Any click listener that calls changeCheckedState()
	 */
	public void setOnClickListener(boolean checked,
			OnClickListener onClickListener) {
		setChecked(checked);
		super.setOnClickListener(onClickListener);
		
	}

	/**
	 * Call this when the button is clicked, to change the checked state
	 * @return
	 * 		The value of the checkbox
	 */
	public boolean changeCheckedState() {
		mChecked = !mChecked;
		changePicture();
		return mChecked;
	}

	/**
	 * Set the checkstate of the checkbox
	 * @param gradient
 * 			The value that the checkbox is set to
	 */
	public void setChecked(boolean gradient) {
		mChecked = gradient;
		changePicture();
	}

}
