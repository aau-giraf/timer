package dk.aau.cs.giraf.wombat.drawlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import dk.aau.cs.giraf.TimerLib.SubProfile;
/**
 * This class is used to generate a view for progressbar
 * Layer: Draw
 *
 */
public class DrawProgressBar extends View {
	SubProfile sp;

	private int background;
	private int frame;
	private int timeleft;
	private int timeleft2;
	private int timespent;
	private int totalTime;
	private double endTime;
	private int frameWidth;

	Paint paint = new Paint();
	Rect r;
	ColorDrawable col;

	int width;
	int height;
	int left;
	int top;

	public DrawProgressBar(Context context, SubProfile sub, int frameWidth) {
		super(context);
		this.frameWidth = frameWidth;
		width = (DrawLibActivity.frameHeight / 8) * 5;
		height = (int) (width * 0.2);

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
		timenow = (endTime - System.currentTimeMillis());

		paint.setColor(background);
		c.drawPaint(paint);
		/* Fill the canvas with the background color */
		LinearGradient lg = new LinearGradient(DrawLibActivity.frameWidth/2, 0, DrawLibActivity.frameWidth/2, DrawLibActivity.frameHeight, background, 0xFF000000, Shader.TileMode.CLAMP);
		paint.setShader(lg);
		c.drawPaint(paint);
		paint.setShader(null);
		
		/* Draw the frame of the progressbar */
		paint.setAntiAlias(true);
		paint.setColor(frame);

		left = (frameWidth - width) / 2;
		top = (DrawLibActivity.frameHeight - height) / 2;

		r = new Rect(left, top, left + width, top + height);
		c.drawRect(r, paint);

		/* Draw the backgroundcolor inside the frame */
		paint.setColor(background);
		r.set(r.left + 2, r.top + 2, r.right - 2, r.bottom - 2);
		c.drawRect(r, paint);

		/* Draw the timespent color (on the right) on top of the timeleft */
		paint.setColor(timespent);
		r.set(left + 3, top + 3, left + width - 3, top + height - 3);
		c.drawRect(r, paint);

		if (endTime >= System.currentTimeMillis()) {
			timenow = endTime - System.currentTimeMillis();
			double percent = (timenow) / totalTime;

			paint.setColor(timeleft2);
			r.set((int) ((left + 3) + ((width - 5) * (1-percent))), top + 3, (left + 3) + width - 5, top
					+ height - 3);
			c.drawRect(r, paint);

			/* Draw the timeleft color (on the left side) */
			col = new ColorDrawable(timeleft);
			col.setAlpha((int) (255 * percent));
			paint.setColor(col.getColor());
			c.drawRect(r, paint);

			/*************** IMPORTANT ***************/
			/* Recalls Draw! */
			invalidate();
		} else {
			paint.setColor(timespent);
			r.set(left + 3, top + 3, left + width - 3, top + height - 3);
			c.drawRect(r, paint);
		}
	}
}
