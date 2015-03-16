package utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.awt.Graphics;
import java.io.Serializable;

public class Triangle implements Serializable {

	Set<Point> endpoints = null;
	Circumcircle cCircle = null;
	
	public Triangle(Set<Point> points) {
		//Error checking
		if (points.size() != 3) {
			throw new RuntimeException("Triangles must have three points.");
		}
		this.endpoints = points;
		this.cCircle = new Circumcircle(this);
	}
	
	public boolean isInsideCircumcircleDistance (Point d) {
		return this.cCircle.isInsideCircumcircle(d);
	}
	
	public Set<Line> generateLines () {
		Set<Line> result = new HashSet<Line>();
		Iterator <Point> iter = this.endpoints.iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();
		result.add(new Line(A, B));
		result.add(new Line(B, C));
		result.add(new Line(C, A));
		return result;
	}
	
	//Returns true if they share an edge, but not if they only share one vertex
	public boolean isNeighbor (Triangle other) {
		Set<Line> temp = this.generateLines();
		temp.retainAll(other.generateLines());
		return (temp.size() == 1);
	}
	
	public static Set<Line> mapTrianglesToLines (Set<Triangle> tris) {
		Set<Line> result = new HashSet<Line>();
		for (Triangle t : tris) {
			result.addAll(t.generateLines());
		}
		return result;
	}
	
	public static Set<Point> mapTrianglesToPoints (Set<Triangle> tris) {
		Set<Point> result = new HashSet<Point>();
		for (Triangle t : tris) {
			result.addAll(t.endpoints);
		}
		return result;
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
		Triangle other = (Triangle) obj;
		if (endpoints == null) {
			if (other.endpoints != null)
				return false;
		} else if (!endpoints.equals(other.endpoints))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = "";
		Iterator <Point> iter = this.endpoints.iterator();
		Point A = iter.next();
		Point B = iter.next();
		Point C = iter.next();
		result += A.toString() + "\n";
		result += B.toString() + "\n";
		result += C.toString();
		return result;
	}
	
	public void drawTriangle (Graphics g) {
		for (Line l : this.generateLines()) {
			l.drawLine(g);
		}
	}
	
	public void drawCircumcircle (Graphics g) {
		new Circumcircle(this).drawCircumcircle(g);
	}

}
