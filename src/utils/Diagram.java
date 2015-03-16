package utils;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Diagram implements Serializable {

	private int width;
	private int height;
	
	private Set<Point>			pointSet 			= new HashSet<Point>();
	private Set<Triangle>		delaunayTriangles 	= new HashSet<Triangle>();
	private Set<VoronoiCell>	voronoiCells		= new HashSet<VoronoiCell>();
	
	public Diagram (int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	//	This method can be improved by only updating voronoi cells whose generating points are 
	//	in delaunay triangles that have been affected by new points being added.
	public void addPoints (Set<Point> points) {
		this.pointSet.addAll(points);
		this.delaunayTriangles = Algorithms.BowyerWatson(this.pointSet);
		this.voronoiCells = Algorithms.generateVoronoiFromDelaunay(this.delaunayTriangles, this);
	}
	
	public boolean isInsideDiagram (Point p) {
		return (p.xCoord > 0 && p.xCoord < this.width) && (p.yCoord > 0 && p.yCoord < this.height);
	}
	
	//For a given slope and point, there will be two points made on different boundaries
	public List<Point> createPointsOnBoundary (Point intercept, double slope) {
		List<Point> result = new ArrayList<Point>();
		double slopeToNW = Point.slopeBetween(intercept, new Point(0, 0));
		double slopeToNE = Point.slopeBetween(intercept, new Point(0, this.width));
		double slopeToSE = Point.slopeBetween(intercept, new Point(this.height, this.width));
		double slopeToSW = Point.slopeBetween(intercept, new Point(this.height, 0));
		
		boolean interceptW = (slope < slopeToSW) && (slope > slopeToNW);
		boolean interceptE = (slope < slopeToNE) && (slope > slopeToSE);
		boolean interceptN = (slope > 0) ? (slope > slopeToNE) : (slope < slopeToNW);
		boolean interceptS = (slope > 0) ? (slope > slopeToSW) : (slope < slopeToSE);
		
		if (interceptW) {
			result.add(new Point(0, (slope * (0 - intercept.xCoord))+intercept.yCoord));
		}
		if (interceptN) {
			result.add(new Point(((0 - intercept.yCoord)/slope)+intercept.xCoord, 0));
		}
		if (interceptE) {
			result.add(new Point(this.width, (slope * (this.width - intercept.xCoord))+intercept.yCoord));
		}
		if (interceptS) {
			result.add(new Point(((height - intercept.yCoord)/slope)+intercept.xCoord, this.height));
		}
		
		return result;
	}
	
	public void drawDiagram(Graphics g) {
		
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Set<Point> getPointSet() {
		return pointSet;
	}

	public Set<Triangle> getDelaunayTriangles() {
		return delaunayTriangles;
	}

	public Set<VoronoiCell> getVoronoiCells() {
		return voronoiCells;
	}
	
}
