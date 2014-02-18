package dk.aau.cs.giraf.wombat.drawlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import dk.aau.cs.giraf.TimerLib.SubProfile;
/**
 * This class is used to generate a view for hourglass
 * Layer: Draw
 *
 */
public class DrawHourglass extends View {
	SubProfile sp;

	private int background;
	private int frame;
	private int timeleft;
	private int timeleft2;
	private int timespent;
	private int totalTime;
	private double endTime;

	Paint paint = new Paint();
	Rect rtop, rbot, rleft, rright;
	Rect timeTop, timeBot;
	ColorDrawable col;

	int width, height;
	int top, left;
	int frameHeight, indent, glassLeft, glassRight, windowWidth, windowHeight,
			glassThickness, glassThicknessBot, glassThicknessTop,
			glassBendHeight;
	int sandTopHeight, sandBotHeight;
	int triTopX, triTopY, triBotMidY, triBotX, triBotY, midFallSandY;
	double startTime;

	Bitmap bitmap;

	public DrawHourglass(Context context, SubProfile sub, int frameWidth) {
		super(context);
		this.windowWidth = frameWidth;
		windowHeight = DrawLibActivity.frameHeight;

		sp = sub;

		background = sp.bgcolor;
		frame = sp.frameColor;
		timeleft = sp.timeLeftColor;
		timeleft2 = sp.timeLeftColor;
		timespent = sp.timeSpentColor;
		totalTime = ((sp.get_totalTime() - 1) + 2) * 1000;
		endTime = System.currentTimeMillis() + totalTime;
		startTime = System.currentTimeMillis();

		
		if (sp.gradient) {
			timeleft2 = timespent;
			timespent = timeleft;
		}
		
		width = 300;
		height = 500;
		frameHeight = height / 20;
		indent = frameHeight;
		glassThickness = 3;
		glassThicknessBot = glassThickness - 1;
		glassThicknessTop = glassThickness - 1;
		glassBendHeight = 135;

		bitmap = Bitmap.createBitmap(windowWidth, windowHeight,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);

		c = drawFrame(c);
	}

	private Canvas drawFrame(Canvas c) {
		/* Fill the canvas with the background color */
		LinearGradient lg = new LinearGradient(DrawLibActivity.frameWidth/2, 0, DrawLibActivity.frameWidth/2, DrawLibActivity.frameHeight, background, 0xFF000000, Shader.TileMode.CLAMP);
		paint.setShader(lg);
		c.drawPaint(paint);
		paint.setShader(null);

		/* Draw the frame of the hourglass */
		paint.setAntiAlias(true);
		paint.setColor(frame);

		left = (windowWidth - width) / 2;
		top = (windowHeight - height) / 2;

		rtop = new Rect(left, top, left + width, top + frameHeight);
		rbot = new Rect(left, top + height - frameHeight, left + width, top
				+ height);
		rleft = new Rect(left + indent, top, left - (frameHeight / 2) + indent,
				top + height);
		rright = new Rect(left + width - indent, top, left + width - indent
				+ (frameHeight / 2), top + height);

		c.drawRect(rtop, paint);
		c.drawRect(rbot, paint);
		c.drawRect(rleft, paint);
		c.drawRect(rright, paint);

		/* Draw the "glass" top */
		Path p = new Path();
		paint.setColor(frame);
		p.moveTo(left + (indent / 2) + frameHeight, top + frameHeight);
		p.lineTo(left + width - (indent / 2) - frameHeight, top + frameHeight);
		p.lineTo(left + width - (indent / 2) - frameHeight, top
				+ glassBendHeight);
		p.lineTo(left + width - (indent / 2) - frameHeight - 105, top + 240);
		p.lineTo(left + (indent / 2) + frameHeight + 105, top + 240);
		p.lineTo(left + (indent / 2) + frameHeight, top + glassBendHeight);
		c.drawPath(p, paint);

		/* Color the "glass" top */
		p = new Path();
		p.moveTo(left + (indent / 2) + frameHeight + glassThickness, top
				+ frameHeight);
		p.lineTo(left + width - (indent / 2) - frameHeight - glassThickness,
				top + frameHeight);
		p.lineTo(left + width - (indent / 2) - frameHeight - glassThickness,
				top + glassBendHeight - glassThicknessTop);
		p.lineTo(left + width - (indent / 2) - frameHeight - 105
				- glassThickness, top + 240 - glassThicknessTop);
		p.lineTo(left + (indent / 2) + frameHeight + 105 + glassThickness, top
				+ 240 - glassThicknessTop);
		p.lineTo(left + (indent / 2) + frameHeight + glassThickness, top
				+ glassBendHeight - glassThicknessTop);
		paint.setColor(background);
		c.drawPath(p, paint);

		/* Draw the "glass" bottom */
		p = new Path();
		paint.setColor(frame);
		p.moveTo(left + (indent / 2) + frameHeight, top - frameHeight + height);
		p.lineTo(left + width - (indent / 2) - frameHeight, top - frameHeight
				+ height);
		p.lineTo(left + width - (indent / 2) - frameHeight, top
				- glassBendHeight + height);
		p.lineTo(left + width - (indent / 2) - frameHeight - 105, top - 240
				+ height);
		p.lineTo(left + (indent / 2) + frameHeight + 105, top - 240 + height);
		p.lineTo(left + (indent / 2) + frameHeight, top - glassBendHeight
				+ height);
		c.drawPath(p, paint);

		/* Color the "glass" bottom */
		p = new Path();
		paint.setColor(background);
		p.moveTo(left + (indent / 2) + frameHeight + glassThickness, top
				- frameHeight + height);
		p.lineTo(left + width - (indent / 2) - frameHeight - glassThickness,
				top - frameHeight + height);
		p.lineTo(left + width - (indent / 2) - frameHeight - glassThickness,
				top - glassBendHeight + height + glassThicknessBot);
		p.lineTo(left + width - (indent / 2) - frameHeight - 105
				- glassThickness, top - 240 + height + glassThicknessBot);
		p.lineTo(left + (indent / 2) + frameHeight + 105 + glassThickness, top
				- 240 + height + glassThicknessBot);
		p.lineTo(left + (indent / 2) + frameHeight + glassThickness, top
				- glassBendHeight + height + glassThicknessBot);
		c.drawPath(p, paint);

		/* Draw the "glass" middle piece */
		p = new Path();
		paint.setColor(frame);
		p.moveTo(left + width - (indent / 2) - frameHeight - 105, top + 240);
		p.lineTo(left + width - (indent / 2) - frameHeight - 105, top + 260);
		p.lineTo(left + (indent / 2) + frameHeight + 105, top + 260);
		p.lineTo(left + (indent / 2) + frameHeight + 105, top + 240);
		c.drawPath(p, paint);

		/* Color the "glass" middle piece */
		p = new Path();
		paint.setColor(background);
		p.moveTo(left + width - (indent / 2) - frameHeight - 105
				- glassThickness, top + 240 - glassThickness);
		p.lineTo(left + width - (indent / 2) - frameHeight - 105
				- glassThickness, top + 260 + glassThickness);
		p.lineTo(left + (indent / 2) + frameHeight + 105 + glassThickness, top
				+ 260 + glassThickness);
		p.lineTo(left + (indent / 2) + frameHeight + 105 + glassThickness, top
				+ 240 - glassThickness);
		c.drawPath(p, paint);

		return c;
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);

		/* Function to calculate percentage */
		double timenow = endTime - System.currentTimeMillis();
		double percent = timenow / totalTime;

		if (percent > 0.4) {
			sandTopHeight = (int) (120 * percent) - 40;
			triBotMidY = (frameHeight * 5) + 4 - (int) (130 * percent);
		} else {
			sandTopHeight = 8;
		}

		if (percent < 0.4) {
			sandBotHeight = (int) (86 - frameHeight - (percent * 150));
			triTopX = (int) (111 - ((280 * percent)));
			triTopY = triTopX;
			triBotY = sandBotHeight;
		}

		if (percent < 0.01) {
			midFallSandY = (int) startTime * 100;
		} else {
			midFallSandY = 0;
		}

		c.drawBitmap(bitmap, 0, 0, null);

		//
		/* Draw the "sand" in the hourglass */
		//	

		/* Draw the "sand" in the top and bottom rectangles of the hourglass */
		timeTop = new Rect(left + (indent / 2) + frameHeight + glassThickness,
				top + glassBendHeight - glassThicknessTop, left + width
						- (indent / 2) - frameHeight - glassThickness, top
						+ glassBendHeight - glassThicknessTop - sandTopHeight
						+ 8); // +8 to get it to fit
		timeBot = new Rect(left + (indent / 2) + frameHeight + glassThickness,
				top + height - frameHeight - sandBotHeight, left + width
						- (indent / 2) - frameHeight - glassThickness, top
						+ height - frameHeight);

		/* setting paint2 to gradient color on the sand */	
		Paint paint2 = new Paint();
		
		if (sp.gradient) {
			col = new ColorDrawable(timeleft2);
			col.setAlpha((int) (255 * (1 - percent)));
			paint2.setColor(col.getColor());
			
			c.drawRect(timeTop, paint);
			c.drawRect(timeBot, paint);
			c.drawRect(timeTop, paint2);
			c.drawRect(timeBot, paint2);
		}else {
			paint2 = paint;
		
			paint.setColor(timeleft);
			c.drawRect(timeTop, paint);
			paint.setColor(timespent);
			c.drawRect(timeBot, paint);
			paint2.setColor(timeleft);
			c.drawRect(timeTop, paint2);
			paint2.setColor(timespent);
			c.drawRect(timeBot, paint2);
		}
		

		/* Draw the the sand in the triangle in the top of the glass */
		Path tp = new Path();
			paint.setColor(timeleft);
		
		tp.moveTo((left + (indent / 2) + frameHeight + glassThickness + triTopX),
				top + glassBendHeight - glassThicknessTop + triTopY); // change as time progress + on both x & y
		tp.lineTo(left + (indent / 2) + frameHeight + 110 + glassThickness, top
				+ 245 - glassThicknessTop);
		tp.lineTo((left + width - (indent / 2) - frameHeight - glassThickness - triTopX),
				top + glassBendHeight - glassThicknessTop + triTopY); // change as time progress - on x, + on y

		c.drawPath(tp, paint);
		c.drawPath(tp, paint2);
		
		/* Draw the falling sand in the middle */
		tp = new Path();
		paint.setColor(timeleft);
		tp.moveTo(left + (indent / 2) + frameHeight + 109 + glassThickness, top
				- glassThicknessTop + 245 - midFallSandY);
		tp.lineTo(left + (indent / 2) + frameHeight + 111 + glassThickness, top
				- glassThicknessTop + 245 - midFallSandY);
		tp.lineTo(left + (indent / 2) + frameHeight + 111 + glassThickness, top
				+ height - frameHeight - triBotMidY);
		tp.lineTo(left + (indent / 2) + frameHeight + 109 + glassThickness, top
				+ height - frameHeight - triBotMidY);
		c.drawPath(tp, paint);
		c.drawPath(tp, paint2);

		/* Draw the the sand in the triangle in the bottom of the glass */
		tp = new Path();
		paint.setColor(timespent);
		tp.moveTo(left + (indent / 2) + frameHeight + glassThickness, top
				+ height - frameHeight - sandBotHeight);
		tp.lineTo(left + width - (indent / 2) - frameHeight - glassThickness,
				top + height - frameHeight - sandBotHeight);
		tp.lineTo(left + (width / 2), top + height - frameHeight
				- sandBotHeight - triBotMidY);
		c.drawPath(tp, paint);
		c.drawPath(tp, paint2);


		if (endTime >= System.currentTimeMillis()) {
			invalidate();
		}
	}
}
