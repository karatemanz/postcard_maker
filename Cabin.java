/////////////////////////////////////////////
// Andrew Zundel  >> CS401 >> Assignment 3 //
/////////////////////////////////////////////

// Creates an object to be referenced by shape interface to be selected and placed onto the //
// the GUI as a cabin.                                                                      //

import java.awt.*;
import java.awt.geom.*;
import java.util.*;


public class Cabin implements MyShape{
		
		private Polygon roof, cBody;
		private int X,Y,size;
		private boolean isHighlighted;
	
	
	public Cabin(int startX, int startY, int sz){
		
		X = startX;
		Y = startY;
		size = sz;
		
		// calls the set up to create polygons
		setUp();
		
	}
	public void setUp(){
		
		// creates the roof and body polygons 
		roof = new Polygon();
			roof.addPoint(X,Y);
			roof.addPoint(X-size,Y);
			roof.addPoint(X-size/2,Y-2*size/4);
		cBody = new Polygon();
			cBody.addPoint(X-2*size/100,Y);
			cBody.addPoint(X-10*size/10,Y);
			cBody.addPoint(X-10*size/10,Y+size/2);
			cBody.addPoint(X-2*size/100,Y+size/2);
		
			
	}
	public void draw(Graphics2D g)
	{
		// initializes color of selected and unselected states
		g.setColor(Color.BLACK);
		if (isHighlighted){
			g.draw(roof);
		}else{
			g.fill(roof);
		g.setColor(Color.RED);
		}if (isHighlighted){
			g.draw(cBody);
		}else{
			g.fill(cBody);
		g.setColor(Color.RED);
		}
	}
	public void move(int x, int y)
	{
		
		int deltaX = x - X;
		int deltaY = y - Y;
		
		// translates the object to new location
		roof.translate(deltaX, deltaY);
		cBody.translate(deltaX, deltaY);
		
		X = x;
		Y = y;
		
	}
	public void highlight(boolean b)
	{
		isHighlighted = b;
	}
	public boolean contains(double x, double y)
	{
		// tells if object contains current x and y
		if (roof.contains(x,y)){ return true; }
		if (cBody.contains(x,y)){ return true; }

		return false;
	}
	public void resize(int newsize)
	{
		
		size = newsize;
		setUp();
		
	}
	public String saveData()
	{
		return ("Cabin:" + X + ":" + Y + ":" + size);
	}
}