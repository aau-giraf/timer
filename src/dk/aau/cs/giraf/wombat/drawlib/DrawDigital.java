package dk.aau.cs.giraf.wombat.drawlib;

import java.util.ArrayList;

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
 * This class is used to generate a view for Digital watch
 * Layer: Draw
 *
 */
public class DrawDigital extends View {
	SubProfile sp;

	private int background;
	private int frame;
	private int timeleft;
	private int timeleft2;
	private int timespent;
	private int totalTime;
	private double endTime;
	private 

	Paint paint = new Paint();
	Rect r;
	ColorDrawable col;

	int frameHeight;
	int frameWidth;

	private int numHeight, numWidth, numSpace, lineSpace, lineWidth, halfLineWidt, halfNumHeight;
	
	Bitmap bitmap;
	
	ArrayList<Path> leadMinPath = new ArrayList<Path>();
	ArrayList<Path> minPath = new ArrayList<Path>();
	ArrayList<Path> leadSecPath = new ArrayList<Path>();
	ArrayList<Path> secPath = new ArrayList<Path>();
	

	public DrawDigital(Context context, SubProfile sub, int frameWidth) {
		super(context);

		sp = sub;
		
		frameHeight = DrawLibActivity.frameHeight;
		this.frameWidth = frameWidth;
		
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
		
		numWidth = frameWidth/8;
		numHeight = numWidth * 2;
		numSpace = numWidth/3;
		lineSpace = 2;
		lineWidth = numHeight / 16;
		
		halfLineWidt = lineWidth/2;
		halfNumHeight = numHeight / 2;

		/* Initialize a bitmap with the "standard" drawings */
		bitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		
		/* Fill the canvas with the background color */
		LinearGradient lg = new LinearGradient(DrawLibActivity.frameWidth/2, 0, DrawLibActivity.frameWidth/2, DrawLibActivity.frameHeight, background, 0xFF000000, Shader.TileMode.CLAMP);
		paint.setShader(lg);
		c.drawPaint(paint);
		paint.setShader(null);

		paint.setColor(timeleft);
		
		int x, y;
		
		y = (frameHeight - numHeight) / 2;
		
		/* Draw first number */
		x = numWidth/2 + numWidth;
		c.drawPath(drawNumberPath(8, x, y), paint);
		
		/* Draw second number */
		x = numWidth/2 + numWidth * 2 + (numSpace);
		c.drawPath(drawNumberPath(8, x, y), paint);
		
		/* Draw third number */
		x =  numWidth/2 + numWidth * 3 + (numSpace * 3);
		c.drawPath(drawNumberPath(8, x, y), paint);
		
		/* Draw second number */
		x = numWidth/2 + numWidth * 4 + (numSpace * 4);
		c.drawPath(drawNumberPath(8, x, y), paint);
		
		paint.setColor(frame);
		c.drawCircle((int) (numWidth * 4.25), (frameHeight / 2) - numSpace, lineWidth / 2, paint);
		c.drawCircle((int) (numWidth * 4.25), (frameHeight / 2) + numSpace, lineWidth / 2, paint);
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		
		double timenow = (endTime - System.currentTimeMillis())/1000;
		c.drawBitmap(bitmap, 0, 0, null);
		
		if(timenow <= 0) timenow = 0;
		
		int num, x, y;
		Paint paint2 = new Paint();
		double percent = (timenow) / (totalTime/1000);
		LinearGradient lg = new LinearGradient(DrawLibActivity.frameWidth/2, 0, DrawLibActivity.frameWidth/2, DrawLibActivity.frameHeight, background, 0xFF000000, Shader.TileMode.CLAMP);
		paint.setShader(lg);

		/* Set the second colors aplha */
		col = new ColorDrawable(timeleft2);
		col.setAlpha((int) (255 * (1-percent)));
		paint2.setColor(col.getColor());
		
		
		y = (frameHeight - numHeight) / 2;
		
		/* Draw first number */
		num = (int) (timenow / 60 / 10);
		x = numWidth/2 + numWidth;
		paint.setColor(background);
		c.drawPath(drawInvertNumberPath(num, x, y), paint);
		c.drawPath(drawNumberPath(num, x, y), paint2);
		
		/* Draw second number */
		num = (int) (timenow / 60) - num * 10;
		x = numWidth/2 + numWidth * 2 + (numSpace);
		c.drawPath(drawInvertNumberPath(num, x, y), paint);
		c.drawPath(drawNumberPath(num, x, y), paint2);
		
		/* Draw third number */
		num = (int) (timenow % 60)/10;
		x =  numWidth/2 + numWidth * 3 + (numSpace * 3);
		c.drawPath(drawInvertNumberPath(num, x, y), paint);
		c.drawPath(drawNumberPath(num, x, y), paint2);
		
		/* Draw second number */
		num = (int) (timenow % 10);
		x = numWidth/2 + numWidth * 4 + (numSpace * 4);
		c.drawPath(drawInvertNumberPath(num, x, y), paint);
		c.drawPath(drawNumberPath(num, x, y), paint2);

		
		/*************** IMPORTANT ***************/
		/* Recalls Draw! */
		if(timenow != 0){
			invalidate();
		} else {
			// Draw 00:00
			num = 0;
			/* Draw first number */
			x = numWidth/2 + numWidth;
			c.drawPath(drawInvertNumberPath(num, x, y), paint);
			
			/* Draw second number */
			x = numWidth/2 + numWidth * 2 + (numSpace);
			c.drawPath(drawInvertNumberPath(num, x, y), paint);
			
			/* Draw third number */
			x =  numWidth/2 + numWidth * 3 + (numSpace * 3);
			c.drawPath(drawInvertNumberPath(num, x, y), paint);
			
			/* Draw second number */
			x = numWidth/2 + numWidth * 4 + (numSpace * 4);
			c.drawPath(drawInvertNumberPath(num, x, y), paint);
		}
		
	}

	/**
	 * Returns the number (num) as a path object
	 * @param num
	 * @param x
	 * 		x-coordinate of the starting point of the number
	 * @param y
	 * 		y-coordinate of the starting point of the number
	 * @return 
	 * 		A path with the coordinates in the number.
	 */
	private Path drawNumberPath(int num, int x, int y) {
		Path p = new Path();
		
		/* Top left line */
		if(num == 4 || num == 5 || num == 6 || num == 8 || num == 9 || num == 0 ){
			p.moveTo(x - lineSpace + halfLineWidt, y + lineSpace); 							// Top point
			p.lineTo(x - lineSpace, y + halfLineWidt + lineSpace);								// Top left point start
			p.lineTo(x - lineSpace, y + halfNumHeight - halfLineWidt - lineSpace);				// Bottom left point start
			p.lineTo(x - lineSpace + halfLineWidt, y + halfNumHeight - lineSpace);			// Bottom point
			p.lineTo(x - lineSpace + lineWidth, y + halfNumHeight - halfLineWidt - lineSpace); // Bottom right point end
			p.lineTo(x - lineSpace + lineWidth, y + halfLineWidt + lineSpace); 				// Top right point end
		}
		
		/* Bottom left line */
		if(num == 2 || num == 6 || num == 8 || num == 0){
			p.moveTo(x - lineSpace + halfLineWidt, y + halfNumHeight + (lineSpace * 3)); 							// Top point
			p.lineTo(x - lineSpace, y + halfLineWidt + halfNumHeight + (lineSpace * 3));								// Top left point start
			p.lineTo(x - lineSpace, y + halfNumHeight - halfLineWidt + halfNumHeight + (lineSpace * 3));				// Bottom left point start
			p.lineTo(x - lineSpace + halfLineWidt, y + halfNumHeight + halfNumHeight + (lineSpace * 3));			// Bottom point
			p.lineTo(x - lineSpace + lineWidth, y + halfNumHeight - halfLineWidt + halfNumHeight + (lineSpace * 3)); // Bottom right point end
			p.lineTo(x - lineSpace + lineWidth, y + halfLineWidt + halfNumHeight + (lineSpace * 3)); 				// Top right point end
		}
		
		/* Top line */
		if(num == 2 || num == 3 || num == 5 || num == 6 || num == 7 || num == 8 || num == 9 || num == 0){
			p.moveTo(x + lineSpace + halfLineWidt , y - lineSpace);
			p.lineTo(x + lineSpace + lineWidth, y + halfLineWidt - lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight, y + halfLineWidt - lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight + halfLineWidt, y - lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight, y - halfLineWidt - lineSpace);
			p.lineTo(x + lineSpace + lineWidth, y - halfLineWidt - lineSpace);
		}
		
		/* Middle line */
		if(num == 2 || num == 3 || num == 4 || num == 5 || num == 6 || num == 8 || num == 9){
			p.moveTo(x + lineSpace + halfLineWidt, y + halfNumHeight + lineSpace);
			p.lineTo(x + lineSpace + lineWidth, y + halfLineWidt + halfNumHeight + lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight, y + halfLineWidt + halfNumHeight + lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight + halfLineWidt, y + halfNumHeight + lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight, y - halfLineWidt + halfNumHeight + lineSpace);
			p.lineTo(x + lineSpace + lineWidth, y - halfLineWidt + halfNumHeight + lineSpace);
		}
		
		/* Bottom line */
		if(num == 2 || num == 3 || num == 5 || num == 6 || num == 8 || num == 9 || num == 0){
			p.moveTo(x + lineSpace + halfLineWidt, y + numHeight + (lineSpace * 5));
			p.lineTo(x + lineSpace + lineWidth, y + halfLineWidt + numHeight + (lineSpace * 5));
			p.lineTo(x - lineSpace + halfNumHeight, y + halfLineWidt + numHeight + (lineSpace * 5));
			p.lineTo(x - lineSpace + halfNumHeight + halfLineWidt, y + numHeight + (lineSpace * 5));
			p.lineTo(x - lineSpace + halfNumHeight, y - halfLineWidt + numHeight + (lineSpace * 5));
			p.lineTo(x + lineSpace + lineWidth, y - halfLineWidt + numHeight + (lineSpace * 5));
		}
		
		/* Top right line */
		if(num == 1 || num == 2 || num == 3 || num == 4 || num == 7 || num == 8 || num == 9 || num == 0){
			p.moveTo(x + lineSpace + halfNumHeight + halfLineWidt, y + lineSpace); 							// Top point
			p.lineTo(x + lineSpace + halfNumHeight, y + halfLineWidt + lineSpace);								// Top left point start
			p.lineTo(x + lineSpace + halfNumHeight, y + halfNumHeight - halfLineWidt - lineSpace);				// Bottom left point start
			p.lineTo(x + lineSpace + halfNumHeight + halfLineWidt, y + halfNumHeight - lineSpace);			// Bottom point
			p.lineTo(x + lineSpace + halfNumHeight + lineWidth, y + halfNumHeight - halfLineWidt - lineSpace); // Bottom right point end
			p.lineTo(x + lineSpace + halfNumHeight + lineWidth, y + halfLineWidt + lineSpace); 				// Top right point end
		}
		
		/* Bottom right line */
		if(num == 1 || num == 3 || num == 4 || num == 5 || num == 6 || num == 7 || num == 8 || num == 9 || num == 0){
			p.moveTo(x + lineSpace + halfNumHeight + halfLineWidt, y + halfNumHeight + (lineSpace * 3)); 							// Top point
			p.lineTo(x + lineSpace + halfNumHeight, y + halfLineWidt + halfNumHeight + (lineSpace * 3));								// Top left point start
			p.lineTo(x + lineSpace + halfNumHeight, y + halfNumHeight - halfLineWidt + halfNumHeight + (lineSpace * 3));				// Bottom left point start
			p.lineTo(x + lineSpace + halfNumHeight + halfLineWidt, y + halfNumHeight + halfNumHeight + (lineSpace * 3));			// Bottom point
			p.lineTo(x + lineSpace + halfNumHeight + lineWidth, y + halfNumHeight - halfLineWidt + halfNumHeight + (lineSpace * 3)); // Bottom right point end
			p.lineTo(x + lineSpace + halfNumHeight + lineWidth, y + halfLineWidt + halfNumHeight + (lineSpace * 3)); 				// Top right point end
		}
		return p;
	}
	

	/**
	 * Draws the spaces of a number which is not supposed to be drawn
	 * @param num
	 * @param x
	 * 		x-coordinate of the starting point of the number
	 * @param y
	 * 		y-coordinate of the starting point of the number
	 * @return 
	 * 		A path with the coordinates not in the number.
	 */
	private Path drawInvertNumberPath(int num, int x, int y) {
		Path p = new Path();
		
		/* Top left line */
		if(num == 1 || num == 2 || num == 3 || num == 7){
			p.moveTo(x - lineSpace + halfLineWidt, y + lineSpace); 							// Top point
			p.lineTo(x - lineSpace, y + halfLineWidt + lineSpace);								// Top left point start
			p.lineTo(x - lineSpace, y + halfNumHeight - halfLineWidt - lineSpace);				// Bottom left point start
			p.lineTo(x - lineSpace + halfLineWidt, y + halfNumHeight - lineSpace);			// Bottom point
			p.lineTo(x - lineSpace + lineWidth, y + halfNumHeight - halfLineWidt - lineSpace); // Bottom right point end
			p.lineTo(x - lineSpace + lineWidth, y + halfLineWidt + lineSpace); 				// Top right point end
		}
		
		/* Bottom left line */
		if(num == 1 || num == 3 || num == 4 || num == 5 || num == 7 || num == 9){
			p.moveTo(x - lineSpace + halfLineWidt, y + halfNumHeight + (lineSpace * 3)); 							// Top point
			p.lineTo(x - lineSpace, y + halfLineWidt + halfNumHeight + (lineSpace * 3));								// Top left point start
			p.lineTo(x - lineSpace, y + halfNumHeight - halfLineWidt + halfNumHeight + (lineSpace * 3));				// Bottom left point start
			p.lineTo(x - lineSpace + halfLineWidt, y + halfNumHeight + halfNumHeight + (lineSpace * 3));			// Bottom point
			p.lineTo(x - lineSpace + lineWidth, y + halfNumHeight - halfLineWidt + halfNumHeight + (lineSpace * 3)); // Bottom right point end
			p.lineTo(x - lineSpace + lineWidth, y + halfLineWidt + halfNumHeight + (lineSpace * 3)); 				// Top right point end
		}
		
		/* Top line */
		if(num == 1 || num == 4){
			p.moveTo(x + lineSpace + halfLineWidt , y - lineSpace);
			p.lineTo(x + lineSpace + lineWidth, y + halfLineWidt - lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight, y + halfLineWidt - lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight + halfLineWidt, y - lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight, y - halfLineWidt - lineSpace);
			p.lineTo(x + lineSpace + lineWidth, y - halfLineWidt - lineSpace);
		}
		
		/* Middle line */
		if(num == 1 || num == 7 || num == 0){
			p.moveTo(x + lineSpace + halfLineWidt, y + halfNumHeight + lineSpace);
			p.lineTo(x + lineSpace + lineWidth, y + halfLineWidt + halfNumHeight + lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight, y + halfLineWidt + halfNumHeight + lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight + halfLineWidt, y + halfNumHeight + lineSpace);
			p.lineTo(x - lineSpace + halfNumHeight, y - halfLineWidt + halfNumHeight + lineSpace);
			p.lineTo(x + lineSpace + lineWidth, y - halfLineWidt + halfNumHeight + lineSpace);
		}
		
		/* Bottom line */
		if(num == 1 || num == 4 || num == 7){
			p.moveTo(x + lineSpace + halfLineWidt, y + numHeight + (lineSpace * 5));
			p.lineTo(x + lineSpace + lineWidth, y + halfLineWidt + numHeight + (lineSpace * 5));
			p.lineTo(x - lineSpace + halfNumHeight, y + halfLineWidt + numHeight + (lineSpace * 5));
			p.lineTo(x - lineSpace + halfNumHeight + halfLineWidt, y + numHeight + (lineSpace * 5));
			p.lineTo(x - lineSpace + halfNumHeight, y - halfLineWidt + numHeight + (lineSpace * 5));
			p.lineTo(x + lineSpace + lineWidth, y - halfLineWidt + numHeight + (lineSpace * 5));
		}
		
		/* Top right line */
		if(num == 5 || num == 6){
			p.moveTo(x + lineSpace + halfNumHeight + halfLineWidt, y + lineSpace); 							// Top point
			p.lineTo(x + lineSpace + halfNumHeight, y + halfLineWidt + lineSpace);								// Top left point start
			p.lineTo(x + lineSpace + halfNumHeight, y + halfNumHeight - halfLineWidt - lineSpace);				// Bottom left point start
			p.lineTo(x + lineSpace + halfNumHeight + halfLineWidt, y + halfNumHeight - lineSpace);			// Bottom point
			p.lineTo(x + lineSpace + halfNumHeight + lineWidth, y + halfNumHeight - halfLineWidt - lineSpace); // Bottom right point end
			p.lineTo(x + lineSpace + halfNumHeight + lineWidth, y + halfLineWidt + lineSpace); 				// Top right point end
		}
		
		/* Bottom right line */
		if(num == 2){
			p.moveTo(x + lineSpace + halfNumHeight + halfLineWidt, y + halfNumHeight + (lineSpace * 3)); 							// Top point
			p.lineTo(x + lineSpace + halfNumHeight, y + halfLineWidt + halfNumHeight + (lineSpace * 3));								// Top left point start
			p.lineTo(x + lineSpace + halfNumHeight, y + halfNumHeight - halfLineWidt + halfNumHeight + (lineSpace * 3));				// Bottom left point start
			p.lineTo(x + lineSpace + halfNumHeight + halfLineWidt, y + halfNumHeight + halfNumHeight + (lineSpace * 3));			// Bottom point
			p.lineTo(x + lineSpace + halfNumHeight + lineWidth, y + halfNumHeight - halfLineWidt + halfNumHeight + (lineSpace * 3)); // Bottom right point end
			p.lineTo(x + lineSpace + halfNumHeight + lineWidth, y + halfLineWidt + halfNumHeight + (lineSpace * 3)); 				// Top right point end
		}
		return p;
	}
}
