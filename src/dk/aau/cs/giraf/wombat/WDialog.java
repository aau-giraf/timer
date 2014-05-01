package dk.aau.cs.giraf.wombat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.gui.GDialog;

/**
 * This class is a custom dialog which is used for all dialogs in WOMBAT
 * Layer: Layout
 *
 */
public class WDialog extends GDialog {
	
	private int layoutId = 1337;
	private int editTextLayoutId = 1338;
	private int textLayoutId = 1339;
	Guardian guard = Guardian.getInstance();
	
	/**
	 * Creates a new Wombat Dialog, with a list view in its body by default
	 * Color is set to the default color from the Guardian.backgroundColor
	 * @param context
	 */
	public WDialog(Context context) {
		// Set the style to the dialog specified in WombatStyle
		super(context);
		
		// Remove the title
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.setContentView(R.layout.profile_list_dialog);

        View layout = LayoutInflater.from(this.getContext()).inflate(R.layout.profile_list_dialog, null);
        this.SetView(layout);


		// Sets the background color to the color from the Guardian (which is set in the MainActivity)
//		Drawable d = context.getResources().getDrawable(R.drawable.wdialog_background);
//		d.setColorFilter(guard.backgroundColor, PorterDuff.Mode.OVERLAY);
//		this.getWindow().setBackgroundDrawable(d);
	}
	
	/**
	 * Creates a new Wombat Dialog, with a list view in its body by default
	 * Color is set to the default color from the Guardian.backgroundColor
	 * @param context
	 * @param title
	 * 		Top title of the dialog
	 */
	public WDialog(Context context, String title){
		this(context);
		this.setTitle(title);

	}
	
	/**
	 * Creates a new Wombat Dialog, with a list view in its body by default
	 * Color is set to the default color from the Guardian.backgroundColor
	 * @param context
	 * @param title
	 * 		Top title of the dialog
	 */
	public WDialog(Context context, int title){
		this(context, context.getResources().getString(title));

        
	}
	
	/**
	 * Sets the top title of the dialog
	 * @param title
	 * 		Top title of the dialog
	 */
	public void setTitle(String title){
		TextView tv = (TextView)this.findViewById(R.id.profile_list_dialog_title);
		tv.setText(title);
		
		// Moves the title a bit in, it looks weird when it is all to the left
		tv.setPadding(30, 20, 30, 0);
	}
	
	/**
	 * Sets the adapter of the list view
	 * @param adapter
	 * 		Any adapter which is valid for a list view
	 */
	public void setAdapter(ArrayAdapter<?> adapter) {
		ListView lv = (ListView) this.findViewById(R.id.profile_list_dialog_listview);
		Drawable d = getContext().getResources().getDrawable(android.R.drawable.dark_header);
		d.setAlpha(0);
		lv.setDivider(d);
		lv.setDividerHeight(1);
		lv.setAdapter(adapter);
	}
	
	/**
	 * Set the on item click of the list view
	 * @param onItemClickListener
	 * 		Any OnItemClickListener supposed to handle clicks in the list view
	 */
	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		ListView lv = (ListView) this.findViewById(R.id.profile_list_dialog_listview);
		lv.setOnItemClickListener(onItemClickListener);
	}

	/**
	 * Adds a new button to the dialog
	 * @param textResource
	 * 		The text on the button, resource id of any string
	 * @param buttonId
	 * 		Id the button is supposed to be set to, the identifier does not have to be unique in this view's hierarchy. The identifier should be a positive number.
	 * @param listener
	 * 		An OnClickListener, which handles the click events on the button, this can also be changed by using the setButtonClickListener method
	 */
	public void addButton(int textResource, int buttonId, View.OnClickListener listener){
		this.addButton(this.getContext().getResources().getString(textResource), buttonId, listener);
	}
	
	/**
	 * Adds a new button to the dialog
	 * @param textResource
	 * 		The text on the button, resource id of any string
	 * @param buttonId
	 * 		Id the button is supposed to be set to, the identifier does not have to be unique in this view's hierarchy. The identifier should be a positive number.
	 */
	public void addButton(int textResource, int buttonId){
		this.addButton(this.getContext().getResources().getString(textResource), buttonId);
	}
	
	/**
	 * Adds a new button to the dialog
	 * @param text
	 * 		The text on the button
	 * @param buttonId
	 * 		Id the button is supposed to be set to, the identifier does not have to be unique in this view's hierarchy. The identifier should be a positive number.
	 * @param listener
	 * 		An OnClickListener, which handles the click events on the button, this can also be changed by using the setButtonClickListener method
	 */
	public void addButton(String text, int buttonId, View.OnClickListener listener){
		this.addButton(text, buttonId);
		this.setButtonOnClickListener(buttonId, listener);
	}
	
	/**
	 * Adds a new button to the dialog
	 * @param text
	 * 		The text on the button
	 * @param buttonId
	 * 		Id the button is supposed to be set to, the identifier does not have to be unique in this view's hierarchy. The identifier should be a positive number.
	 */
	public void addButton(String text, int buttonId){
		LinearLayout buttonLayout = (LinearLayout) this.findViewById(layoutId);
		// Create a button LinearLayout if it doesn't exist already
		if(buttonLayout == null){
			LinearLayout ll = (LinearLayout) this.findViewById(R.id.profile_list_dialog);
			
			buttonLayout = new LinearLayout(this.getContext());
			buttonLayout.setId(layoutId);
			buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
			buttonLayout.setGravity(Gravity.CENTER);
			buttonLayout.setPadding(0, 0, 0, 20);
			
			ll.addView(buttonLayout);
		}
		this.mAddButton(text, buttonId, buttonLayout);
	}
	
	/**
	 * Adds a button to the buttonLayout 
	 * @param text
	 * 		The text on the button
	 * @param buttonId
	 * 		Id the button is supposed to be set to, the identifier does not have to be unique in this view's hierarchy. The identifier should be a positive number.
	 * @param buttonLayout
	 * 		The layout where the button should be added
	 */
	private void mAddButton(String text, int buttonId, LinearLayout buttonLayout){
		Button b = new Button(this.getContext());
		b.setId(buttonId);
		
		// Set the style to the list button style
		b.setBackgroundResource(R.drawable.list);
		
		// Sets the internal padding of the button
		b.setPadding(30, 20, 30, 20);
		b.setText(text);
		
		// Set the margin to other objects
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		params.setMargins(20, 20, 20, 20);
//		b.setLayoutParams(params);
		
		// Add the button to the view
		buttonLayout.addView(b);
	}
	
	/**
	 * Adds an on click listener to the button with the specified id
	 * @param buttonId
	 * 		Id of the button which the listener is attached to
	 * @param listener
	 * 		The listener which listens on click events on the button
	 */
	public void setButtonOnClickListener(int buttonId, View.OnClickListener listener){
		Button b = (Button)this.findViewById(buttonId);
		b.setOnClickListener(listener);
	}

	/**
	 * Substitutes the linear layout with an edit text field
	 * @param text
	 * 		The start text of the edit text field
	 * @param editTextId
	 * 		The id of the edit text field, the identifier does not have to be unique in this view's hierarchy. The identifier should be a positive number.
	 */
	public void addEditText(String text, int editTextId) {
		LinearLayout editTextLayout = (LinearLayout) this.findViewById(editTextLayoutId);
		// Create a new editTextLayout if it doesn't exist already
		if(editTextLayout == null){
			LinearLayout ll = (LinearLayout) this.findViewById(R.id.profile_list_dialog);
			
			// Shrink the list view, we don't want both a list view and an edit text field
			ListView lv = (ListView) this.findViewById(R.id.profile_list_dialog_listview);
			lv.setLayoutParams(new LinearLayout.LayoutParams(0,0));
			
			editTextLayout = new LinearLayout(this.getContext());
			editTextLayout.setId(editTextLayoutId);
			editTextLayout.setOrientation(LinearLayout.VERTICAL);
			editTextLayout.setGravity(Gravity.CENTER);
			editTextLayout.setPadding(0, 0, 0, 20);
			
			ll.addView(editTextLayout);
		}
		
		EditText et = new EditText(this.getContext());
		et.setId(editTextId);
		et.setText(text);
		
		// Set the margin of the edit text field
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 20, 20, 20);
		et.setLayoutParams(params);
		
		// Add the edit text field to its layout
		editTextLayout.addView(et);
		editTextLayout.setLayoutParams(new LinearLayout.LayoutParams(300, LayoutParams.WRAP_CONTENT));
		
	}
	
	/**
	 * Get the text currently in the edit text field
	 * @param editTextId
	 * 		Id of the edit text field added with the addEditText method
	 * @return
	 * 		Returns the text of the field if the id was valid, else returns an empty string
	 */
	public String getEditTextText(int editTextId){
		EditText et = (EditText) this.findViewById(editTextId);
		
		// If the edit text field exists, return the content
		if(et != null)
			return et.getText().toString();
		else
			return "";
	}
	
	/**
	 * Substitutes the linear layout with an text field
	 * @param text
	 * 		The start text of the text field
	 * @param textViewId
	 * 		The id of the text field, the identifier does not have to be unique in this view's hierarchy. The identifier should be a positive number.
	 */
	public void addTextView(String text, int textViewId) {
		LinearLayout textLayout = (LinearLayout) this.findViewById(textLayoutId );
		// Create a new editTextLayout if it doesn't exist already
		if(textLayout == null){
			LinearLayout ll = (LinearLayout) this.findViewById(R.id.profile_list_dialog);
			
			// Shrink the list view, we don't want both a list view and an edit text field
			ListView lv = (ListView) this.findViewById(R.id.profile_list_dialog_listview);
			lv.setLayoutParams(new LinearLayout.LayoutParams(0,0));
			
			textLayout = new LinearLayout(this.getContext());
			textLayout.setId(textLayoutId);
			textLayout.setOrientation(LinearLayout.VERTICAL);
			textLayout.setGravity(Gravity.CENTER);
			textLayout.setPadding(0, 0, 0, 20);
			
			ll.addView(textLayout);
		}
		
		TextView tv = new TextView(this.getContext());
		tv.setId(textViewId);
		tv.setText(text);
		
		// Set the margin of the edit text field
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(20, 20, 20, 20);
		tv.setLayoutParams(params);
		
		// Add the edit text field to its layout
		textLayout.addView(tv);
		textLayout.setLayoutParams(new LinearLayout.LayoutParams(400, 100));
		
	}
}
