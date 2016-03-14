/////////////////////////////////////////////
// Andrew Zundel  >> CS401 >> Assignment 3 //
/////////////////////////////////////////////

// Creates an object to be referenced by shape interface to be selected and placed onto the //
// the GUI as a cloud.   																	//

import java.awt.*;
import java.awt.geom.*;
import java.util.*;


public class Cloud implements MyShape{
		
		private Ellipse2D.Double circle1, circle2, circle3, circle4;
		private int X,Y,size;
		private boolean isHighlighted;
	
	
	public Cloud(int startX, int startY, int sz){
		
		X = startX;
		Y = startY;
		size = sz;
		
		// creates initial 4 circles that make up the cloud
		circle1 = new Ellipse2D.Double(X-size/4, Y-size, size+15, size);
		circle2 = new Ellipse2D.Double(X-4*size/4, Y-size, size+15, size);
		circle3 = new Ellipse2D.Double(X-size/4, Y-3*size/4, size+20, size);
		circle4 = new Ellipse2D.Double(X-4*size/4, Y-3*size/2, size+20, size+8);
		
		
	}
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		if (isHighlighted){
			g.draw(circle1);
		}else{
			g.fill(circle1);
		g.setColor(Color.WHITE);
		}if (isHighlighted){
			g.draw(circle2);
		}else{
			g.fill(circle2);
			g.setColor(Color.WHITE);
		}if (isHighlighted){
			g.draw(circle3);
		}else{
			g.fill(circle3);
			g.setColor(Color.WHITE);
		}if (isHighlighted){
			g.draw(circle4);
		}else{
			g.fill(circle4);
			g.setColor(Color.WHITE);
		}
	}
	public void move(int x, int y)
	{
		X = x;
		Y = y;
		
			circle1.setFrame(X-size/4, Y-size, size+15, size);
			circle2.setFrame(X-4*size/4, Y-size, size+15, size);
			circle3.setFrame(X-size/4, Y-3*size/4, size+20, size);
			circle4.setFrame(X-4*size/4, Y-3*size/2, size+20, size+8);
		
		
	}
	public void highlight(boolean b)
	{
		isHighlighted = b;
	}
	public boolean contains(double x, double y)
	{
		if (circle1.contains(x,y)){ return true; }
		if (circle2.contains(x,y)){ return true; }
		if (circle3.contains(x,y)){ return true; }
		if (circle4.contains(x,y)){ return true; }
	
		return false;
	}
	public void resize(int newsize)
	{
		
		size = newsize;
		move(X,Y);
		
	}
	public String saveData()
	{
		return ("Cloud:" + X + ":" + Y + ":" + size);
	}
}
	
	

