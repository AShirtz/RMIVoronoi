package utils;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Line {

	public Set<Point> endpoints = null;

	public Line(Point a, Point b) {
		if (a.equals(b)) {
			throw new RuntimeException("Cannot create a line between equivalent points.");
		}
		this.endpoints = new HashSet<Point>(2);
		this.endpoints.add(a);
		this.endpoints.add(b);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endpoints == null) ? 0 : endpoints.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (endpoints == null) {
			if (other.endpoints != null)
				return false;
		} else if (!endpoints.equals(other.endpoints))
			return false;
		return true;
	}

	public double length () {
		Iterator<Point> iter = this.endpoints.iterator();
		return iter.next().distance(iter.next());
	}
	
	public boolean onSameSide (Point A, Point B) {
		Iterator <Point> iter = this.endpoints.iterator();
		Point C = iter.next();
		Point D = iter.next();
		
		double result = 
				((C.yCoord - D.yCoord)*(A.xCoord - C.xCoord)+(D.xCoord - C.xCoord)*(A.yCoord - C.yCoord)) *
				((C.yCoord - D.yCoord)*(B.xCoord - C.xCoord)+(D.xCoord - C.xCoord)*(B.yCoord - C.yCoord));
		return result > 0;
	}

	public void drawLine(Graphics g) {
		Iterator <Point> iter = this.endpoints.iterator();
		Point a = iter.next();
		Point b = iter.next();
		g.drawLine((int) a.xCoord, (int) a.yCoord, (int) b.xCoord, (int) b.yCoord); 
	}
	
	public String toString () {
		Iterator <Point> iter = this.endpoints.iterator();
		Point a = iter.next();
		Point b = iter.next();
		return "(" + a.toString() + ", " + b.toString() + ")";
	}
	
	public static Set<Point> mapLinesToPoints (Set<Line> lines) {
		Set<Point> result = new HashSet<Point>();
		for (Line l : lines) {
			Iterator <Point> iter = l.endpoints.iterator();
			result.add(iter.next());
			result.add(iter.next());
		}
		return result;
	}
}
