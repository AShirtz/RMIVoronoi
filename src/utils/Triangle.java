package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Graphics;

public class Triangle {

	List<Point> endpoints = null;
	Circumcircle cCircle = null;
	
	public Triangle(List<Point> points) {
		//Error checking
		if (points.size() != 3) {
			throw new RuntimeException("Triangles must have three points.");
		} else if (points.get(0).equals(points.get(1)) || points.get(0).equals(points.get(2)) || points.get(1).equals(points.get(2))) {
			throw new RuntimeException("A triangle's corners must be distinct.");
		}
		this.endpoints = points;
		this.cCircle = new Circumcircle(this);
	}
	
	public static List<Point> orderPointsCCW (List<Point> points) {
		List<Point> result = new ArrayList<Point>(3);

		Point lowest = null;
		for (Point p : points) {
			if (lowest == null || (lowest.yCoord > p.yCoord) || ((lowest.yCoord == p.yCoord) && (lowest.xCoord > p.xCoord))) {
				lowest = p;
			}
		}
		
		Point p1 = points.get((points.indexOf(lowest)+1)%points.size());
		Point p2 = points.get((points.indexOf(lowest)+2)%points.size());
		
		double crossZ = (p1.xCoord - lowest.xCoord)*(p2.yCoord - lowest.yCoord) - (p1.yCoord - lowest.yCoord)*(p2.xCoord - lowest.xCoord);
		
		result.add(lowest);
		if (crossZ > 0) {
			result.add(p1);
			result.add(p2);
		} else if (crossZ < 0){
			result.add(p2);
			result.add(p1);
		} else {
			throw new RuntimeException("Collinear points don't make triangles.");
		}
		
		return result;
	}
	
	public boolean isInsideCircumcircleDistance (Point d) {
		return this.cCircle.isInsideCircumcircle(d);
	}
	
	
	public boolean isInsideCircumcircle (Point p) {
		//This ugly method is for computing the determinant of a specific matrix found at: (as of 2/23/2015)
		//http://en.wikipedia.org/wiki/Delaunay_triangulation#Algorithms
		double a = 
				((this.endpoints.get(0).xCoord - p.xCoord)
						*(this.endpoints.get(1).yCoord - p.yCoord)
						*(((Math.pow(this.endpoints.get(2).xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints.get(2).yCoord, 2))-(Math.pow(p.yCoord, 2))))
				+ ((this.endpoints.get(0).yCoord - p.yCoord)
						*(this.endpoints.get(2).xCoord - p.xCoord)
						*(((Math.pow(this.endpoints.get(1).xCoord, 2))-(Math.pow(p.xCoord, 2))))+((Math.pow(this.endpoints.get(1).yCoord, 2))-(Math.pow(p.yCoord, 2))))
				+ ((this.endpoints.get(1).xCoord - p.xCoord)
						*(this.endpoints.get(2).yCoord - p.yCoord)
						*(((Math.pow(this.endpoints.get(0).xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints.get(0).yCoord, 2))-(Math.pow(p.yCoord, 2))))));
		
		double b = 
				((this.endpoints.get(1).yCoord - p.yCoord)
						*(this.endpoints.get(2).xCoord - p.xCoord)
						*(((Math.pow(this.endpoints.get(0).xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints.get(0).yCoord, 2))-(Math.pow(p.yCoord, 2)))))
				+((this.endpoints.get(0).yCoord - p.yCoord)
						*(this.endpoints.get(1).xCoord - p.xCoord)
						*(((Math.pow(this.endpoints.get(2).xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints.get(2).yCoord, 2))-(Math.pow(p.yCoord, 2)))))
				+((this.endpoints.get(0).xCoord - p.xCoord)
						*(this.endpoints.get(2).yCoord - p.yCoord)
						*(((Math.pow(this.endpoints.get(1).xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints.get(1).yCoord, 2))-(Math.pow(p.yCoord, 2)))));
				
		return a > b;
	}
	
	public Set<Line> generateLines () {
		Set<Line> result = new HashSet<Line>();
		result.add(new Line(this.endpoints.get(0), this.endpoints.get(1)));
		result.add(new Line(this.endpoints.get(1), this.endpoints.get(2)));
		result.add(new Line(this.endpoints.get(2), this.endpoints.get(0)));
		return result;
	}
	
	public static Set<Line> mapTrianglesToLines (Set<Triangle> tris) {
		Set<Line> result = new HashSet<Line>();
		for (Triangle t : tris) {
			result.addAll(t.generateLines());
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
		result += this.endpoints.get(0).toString() + "\n";
		result += this.endpoints.get(1).toString() + "\n";
		result += this.endpoints.get(2).toString();
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
