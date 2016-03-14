/////////////////////////////////////////////
// Andrew Zundel  >> CS401 >> Assignment 3 //
/////////////////////////////////////////////

// Creates an object to be referenced by shape interface to be selected and placed onto the //
// the GUI as a snowman.                                                                    //


import java.awt.*;
import java.awt.geom.*;
import java.util.*;


public class Snowman implements MyShape{
		
		private Ellipse2D.Double circle1, circle2, circle3;
		private int X,Y,size;
		private boolean isHighlighted;
	
	
	public Snowman(int startX, int startY, int sz){
		
		X = startX;
		Y = startY;
		size = sz;
		
		// creates three similar circles 
		circle1 = new Ellipse2D.Double(X-size/4, Y-size, size, size);
		circle2 = new Ellipse2D.Double(X-2*size/5, Y-2*size/9, 20+size, 20+size);
		circle3 = new Ellipse2D.Double(X-4*size/7, Y-40*size/1000, 40+size, 40+size);
		
	}
	public void draw(Graphics2D g)
	{
		// colors it accordingly both selected and unselected states
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
		}
	}
	public void move(int x, int y)
	{
		
		X = x;
		Y = y;
		
			// sets the frame of the circles for a new location when moved
			circle1.setFrame(X-size/4, Y-size, size, size);
			circle2.setFrame(X-2*size/5, Y-2*size/9, 20+size, 20+size);
			circle3.setFrame(X-4*size/7, Y-40*size/1000, 40+size, 40+size);
			
		
		
	}
	public void highlight(boolean b)
	{
		isHighlighted = b;
	}
	public boolean contains(double x, double y)
	{
		// tests if the circles contain current x and y location
		if (circle1.contains(x,y)){ return true; }
		if (circle2.contains(x,y)){ return true; }
		if (circle3.contains(x,y)){ return true; }
	
		return false;
	}
	public void resize(int newsize)
	{
		size = newsize;
		move(X,Y);
		
	}
	public String saveData()
	{
		return ("Snowman:" + X + ":" + Y + ":" + size);
	}
}