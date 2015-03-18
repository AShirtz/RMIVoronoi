package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class VoronoiCell implements Serializable {

	Set<Line> edges = new HashSet<Line>();
	Point generatingPoint = null;
	boolean truncated = false;
	
	@Override
	public String toString() {
		return "VoronoiCell [edges=" + edges + ", generatingPoint="
				+ generatingPoint + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result
				+ ((generatingPoint == null) ? 0 : generatingPoint.hashCode());
		return result;
	}
	
	public double getArea () {
		double result = 0.0;
		for (Line edge : this.edges) {
			Set<Point> temp = new HashSet<Point>(edge.endpoints);
			temp.add(this.generatingPoint);
			result += new Triangle(temp).getArea();
		}
		return result;
	}
	
	public void drawCell(Graphics g) {
		for (Line l : this.edges) {
			l.drawLine(g);
		}
	}
	
	public void drawCellWithArea (Graphics g, Color textColor) {
		Color prevColor = g.getColor();
		this.drawCell(g);
		g.setColor(textColor);
		if (this.truncated) { g.setColor(Color.red); }
		g.drawString(Math.floor(this.getArea()) + "", (int) this.generatingPoint.xCoord, (int) this.generatingPoint.yCoord);
		g.setColor(prevColor);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoronoiCell other = (VoronoiCell) obj;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (generatingPoint == null) {
			if (other.generatingPoint != null)
				return false;
		} else if (!generatingPoint.equals(other.generatingPoint))
			return false;
		return true;
	}
	
	public static Set<Line> mapCellsToLines (Set<VoronoiCell> cells) {
		Set<Line> result = new HashSet<Line>();
		for (VoronoiCell c : cells) {
			result.addAll(c.edges);
		}
		return result;
	}
	
	public static Set<Point> mapCellsToGenPoints (Set<VoronoiCell> cells) {
		Set<Point> result = new HashSet<Point>(cells.size());
		for (VoronoiCell c : cells) {
			result.add(c.generatingPoint);
		}
		return result;
	}
	
	public static void truncateCell(VoronoiCell cell, Diagram diag) {
		Set<Line> linesToTruncate = new HashSet<Line>();
		for(Line l : cell.edges) {
			if (diag.crossesDiagramBoundary(l)) { linesToTruncate.add(l); }
		}
		cell.edges.removeAll(linesToTruncate);
		for (Line l : linesToTruncate) {
			Iterator<Point> iter = l.endpoints.iterator();
			Point p1 = iter.next();
			Point p2 = iter.next();
			
			//Which point is inside the diagram, and when creating boundary points, which one is in the same direction as the invalid point
			if (diag.isInsideDiagram(p1)) {
				List<Point> newPoints = diag.createPointsOnBoundary(p1, l.slope());
				p2 = (Math.signum(p1.xCoord - p2.xCoord) == Math.signum(p1.xCoord - newPoints.get(0).xCoord)) ? (newPoints.get(0)) : (newPoints.get(1));
			} else {
				List<Point> newPoints = diag.createPointsOnBoundary(p2, l.slope());
				p2 = (Math.signum(p2.xCoord - p1.xCoord) == Math.signum(p2.xCoord - newPoints.get(0).xCoord)) ? (newPoints.get(0)) : (newPoints.get(1));
			}
			cell.edges.add(new Line(p1, p2));
		}
		
		Set<Line> boundaryLines = new HashSet<Line>();
		for (Line l : cell.edges) {
			if (diag.hasPointOnBoundary(l)) { boundaryLines.add(l); }
		}
		
		Point p1 = null;
		Point p2 = null;
		for (Line l : boundaryLines) {
			Iterator<Point> iter = l.endpoints.iterator();
			Point temp = iter.next();
			if (diag.isOnDiagramBoundary(temp)) {
				if (p1 == null) { p1 = temp; }
				else 			{ p2 = temp; }
			} else {
				if (p1 == null) { p1 = iter.next(); }
				else			{ p2 = iter.next(); }
			}
		}
		
		if (diag.onWhichBoundary(p1) == diag.onWhichBoundary(p2)) { cell.edges.add(new Line(p1, p2)); }
		else {
			Point p3 = diag.cornerBetweenSides(diag.onWhichBoundary(p1), diag.onWhichBoundary(p2));
			cell.edges.add(new Line(p1, p2));
			cell.edges.add(new Line(p2, p3));
		}
	}
}