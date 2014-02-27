package dk.aau.cs.giraf.wombat.drawlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import dk.aau.cs.giraf.TimerLib.SubProfile;
/**
 * This class is used to generate a view for Watch
 * Layer: Draw
 *
 */
public class DrawWatch extends View {
	private SubProfile sp;

	private int background;
	private int frame;
	private int timeleft;
	private int timeleft2;
	private int timespent;
	private int totalTime;
	private double endTime;

	private double rotation;

	private Paint paint = new Paint();
	private Rect r;
	private ColorDrawable col;

	private int frameWidth;
	private int frameHeight;

	private int width;
	private int height;
	private int left;
	private int right;
	private int top;
	private int bottom;

	public DrawWatch(Context context, SubProfile sub,int frameWidth) {
		super(context);

		/* Get the window hight assigned by the draw activity */
		this.frameWidth = frameWidth;
		frameHeight = DrawLibActivity.frameHeight;

		if (frameWidth > frameHeight)
			width = (int) (frameHeight / 1.5);
		else
			width = (int) (frameWidth / 1.5);
		height = width;

		sp = sub;

		background = sp.bgcolor;
		frame = sp.frameColor;
		timeleft = sp.timeLeftColor;
		timeleft2 = sp.timeLeftColor;
		timespent = sp.timeSpentColor;
		totalTime = ((sp.get_totalTime() - 1) + 2) * 1000;
		endTime = System.currentTimeMillis() + totalTime;

		if (sp.gradient) {
			timeleft2 = timespent;
			timespent = background;
		}
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		
		double timenow = (endTime - System.currentTimeMillis());
		
		/* Fill the canvas with the background color */
		LinearGradient lg = new LinearGradient(DrawLibActivity.frameWidth/2, 0, DrawLibActivity.frameWidth/2, DrawLibActivity.frameHeight, background, 0xFF000000, Shader.TileMode.CLAMP);
		paint.setShader(lg);
		c.drawPaint(paint);
		paint.setShader(null);

		/* Draw the frame of the progressbar */
		paint.setAntiAlias(true);
		paint.setColor(frame);

		left = frameWidth / 2;
		top = frameHeight / 2;

		/* Draw the outer circle/border */
		c.drawCircle(left, top, width / 2, paint);

		/* Draw the inner circle */
		paint.setColor(timespent);
		c.drawCircle(left, top, (width / 2) - 3, paint);

		left = ((frameWidth - width) / 2) + 3;
		right = (((frameWidth - width) / 2) + width) - 3;
		top = ((frameHeight - height) / 2) + 3;
		bottom = (((frameHeight - height) / 2) + height) - 3;

        // totalTime is the amount of time the timer needs to run in milliseconds
        // timenow is the time left, in milliseconds
        // percent, double between 0..1, is used to determine the current angle and the color
        // rt is the percent multiplied by 360 to make it fit between 0..360
        // rotation is then negative rt added to 360, because the clock needs to run counter clockwise
		double percent = timenow/totalTime;
        double rt = percent*360.0;
		rotation = -rt+360;

		// Draw the timer
		paint.setColor(timeleft2);
		RectF rf = new RectF(left, top, right, bottom);
		c.drawArc(rf, 270 - (int) rotation, (int) rotation, true, paint);

		// Draw the timer gradient
		col = new ColorDrawable(timeleft);
		col.setAlpha((int) (255 * percent));
		paint.setColor(col.getColor());
		c.drawArc(rf, 270 - (int) rotation, (int) rotation, true, paint);

		/* Draw the center */
		paint.setColor(frame);
		c.drawCircle(frameWidth/2, frameHeight/2, 8, paint);
		
		/* Draw the indicators 0, 3, 6, 9 */
		r = new Rect(frameWidth / 2 - 4, ((frameHeight - height) / 2) + 15,
				frameWidth / 2 + 4, ((frameHeight - height) / 2) + 15 + 40);

		for (int i = 0; i < 4; i++) {
			c.rotate(90, frameWidth / 2, frameHeight / 2);
			c.drawRect(r, paint);
		}

		/* Draw the small indicators */
		r = new Rect(frameWidth / 2 - 2, ((frameHeight - height) / 2) + 17,
				frameWidth / 2 + 2, ((frameHeight - height) / 2) + 15 + 35);

		for (int i = 0; i < 12; i++) {
			c.rotate(30, frameWidth / 2, frameHeight / 2);
			c.drawRect(r, paint);
		}

		if (endTime >= System.currentTimeMillis()) {
			/*************** IMPORTANT ***************/
			/* Recalls Draw! */
			invalidate();
		} else {
			
		}
	}
}
