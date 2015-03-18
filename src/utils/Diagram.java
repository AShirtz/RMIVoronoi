package utils;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Diagram implements Serializable {
	
	public static enum diagramSide { N, S, E, W };

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
		if (this.pointSet.size() < 3) { return; }
		this.delaunayTriangles = Algorithms.BowyerWatson(this.pointSet);
		this.voronoiCells = Algorithms.generateVoronoiFromDelaunay(this.delaunayTriangles, this);
	}
	
	public boolean isInsideDiagram (Point p) {
		return (p.xCoord >= 0 && p.xCoord <= this.width) && (p.yCoord >= 0 && p.yCoord <= this.height);
	}
	
	public boolean crossesDiagramBoundary (Line l) {
		Iterator<Point> iter = l.endpoints.iterator();
		return (!this.isInsideDiagram(iter.next()) || !this.isInsideDiagram(iter.next()));
	}
	
	public boolean hasPointOnBoundary (Line l) {
		Iterator<Point> iter = l.endpoints.iterator();
		return this.isOnDiagramBoundary(iter.next()) || this.isOnDiagramBoundary(iter.next());
	}
	
	public boolean isOnDiagramBoundary (Point p) {
		return (p.xCoord == 0) || (p.xCoord == this.width) || (p.yCoord == 0) || (p.yCoord == this.height);
	}
	
	//Should have already checked if onBoundary
	public Diagram.diagramSide onWhichBoundary (Point p) {
		if (p.xCoord == 0) 			{ return Diagram.diagramSide.W; }
		if (p.xCoord == this.width)	{ return Diagram.diagramSide.E; }
		if (p.yCoord == 0)			{ return Diagram.diagramSide.N; }
		else 						{ return Diagram.diagramSide.S; }
	}
	
	public Point cornerBetweenSides (Diagram.diagramSide a, Diagram.diagramSide b) {
		if ((a == Diagram.diagramSide.N && b == Diagram.diagramSide.S) ||
			(a == Diagram.diagramSide.S && b == Diagram.diagramSide.N) ||
			(a == Diagram.diagramSide.E && b == Diagram.diagramSide.W) ||
			(a == Diagram.diagramSide.W && b == Diagram.diagramSide.E))
			{ throw new RuntimeException ("Two corners exist between these two edge"); }	//This behavior should be refactored into a method that uses slopes 
		double x = 0;
		double y = 0;
		if (a == Diagram.diagramSide.N) { y = 0; }
		if (a == Diagram.diagramSide.E) { x = this.width; }
		if (a == Diagram.diagramSide.S) { y = this.height; }
		if (a == Diagram.diagramSide.W) { x = 0; }
		
		return new Point(x,y);
	}
	
	//For a given slope and point, there will be two points made on different boundaries
	public List<Point> createPointsOnBoundary (Point intercept, double slope) {
		List<Point> result = new ArrayList<Point>();
		double slopeToNW = Point.slopeBetween(intercept, new Point(0, 0));
		double slopeToNE = Point.slopeBetween(intercept, new Point(this.width, 0));
		double slopeToSE = Point.slopeBetween(intercept, new Point(this.width, this.height));
		double slopeToSW = Point.slopeBetween(intercept, new Point(0, this.height));
		
		boolean interceptW = (slope < slopeToNW) && (slope > slopeToSW);
		boolean interceptE = (slope < slopeToSE) && (slope > slopeToNE);
		boolean interceptN = (slope > 0) ? (slope > slopeToNW) : (slope < slopeToNE);
		boolean interceptS = (slope > 0) ? (slope > slopeToSE) : (slope < slopeToSW);
		
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
