package utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Algorithms {
	
	//Based on the pseudocode found at (as of 2/24/2015)
	//http://en.wikipedia.org/wiki/Bowyer%E2%80%93Watson_algorithm#Pseudocode
	public static Set<Triangle> BowyerWatson (Set<Point> pointSet) {
		Set<Triangle> result = new HashSet<Triangle>();
		
		//Add the super triangle, will encompass all added points
		Triangle superTriangle = generateSuperTriangle(pointSet);
		result.add(superTriangle);

		result = BowyerWatsonInternal(result, pointSet);
		
		//If a triangle contains points from the super triangle remove it from the result
		Set<Triangle> trianglesToRemove = new HashSet<Triangle>();
		for (Triangle t : result) {
			Set<Point> temp = new HashSet<Point>(t.endpoints);
			temp.retainAll(superTriangle.endpoints);
			if (!temp.isEmpty()) { trianglesToRemove.add(t); } 
		}
		result.removeAll(trianglesToRemove);
		
		return result;
	}
	
	private static Set<Triangle> BowyerWatsonInternal (Set<Triangle> tris, Set<Point> pointSet) {
		Set<Triangle> result = new HashSet<Triangle>(tris);
		for (Point p : pointSet) {
			if (p.xCoord > 160 && p.xCoord < 170 && p.yCoord > 790 && p.yCoord < 810) {
				result.size();
			}
			//Find all triangles that have been made invalid by the new point
			Set<Triangle> invalidTriangles = new HashSet<Triangle>();
			for (Triangle t : result) {
				if (t.isInsideCircumcircleDistance(p)) {
					invalidTriangles.add(t);
				}
			}
			
			Set<Line> polygon = new HashSet<Line>();
			
			//Find the boundary of the hole made by removing invalid triangles
			for (Triangle t : invalidTriangles) {
				for (Line l : t.generateLines()) {
					boolean isShared = false;
					for (Triangle o : invalidTriangles) {
						if (!t.equals(o) && o.generateLines().contains(l)) {
							isShared = true;
							continue;
						} 
					}
					if (!isShared) {
						polygon.add(l);
					}
				}
			}
			
			//Remove invalid triangles from result
			result.removeAll(invalidTriangles);
			
			for (Line l : polygon) {
				Set<Point> newTrianglePoints = new HashSet<Point>(3);
				newTrianglePoints.add(p);
				newTrianglePoints.addAll(l.endpoints);
				result.add(new Triangle(newTrianglePoints));
			}
		}
		return result;
	}
	
	private static Triangle generateSuperTriangle (Set<Point> pointSet) {
		Set<Point> result = new HashSet<Point>(3);
		double xmin = pointSet.iterator().next().xCoord;
		double ymin = pointSet.iterator().next().yCoord;
		double xmax = xmin;
		double ymax = ymin;
		
		for (Point p : pointSet) {
			xmin = (xmin < p.xCoord) ? (xmin) : (p.xCoord);
			ymin = (ymin < p.yCoord) ? (ymin) : (p.yCoord);
			xmax = (xmax > p.xCoord) ? (xmax) : (p.xCoord);
			ymax = (ymax > p.yCoord) ? (ymax) : (p.yCoord);
		}
		
		double dx = xmax - xmin;
		double dy = ymax - ymin;
		double dmax = (dx > dy) ? (dx) : (dy);
		double xmid = (xmax + xmin) / 2.0;
		double ymid = (ymax + ymin) / 2.0;
		
		result.add(new Point (xmid - 2*dmax, ymid - dmax + 1));	//The Circumcircle stuff cannot deal with horizontal lines...
		result.add(new Point (xmid, ymid + 2*dmax));
		result.add(new Point (xmid + 2*dmax, ymid-dmax));
		return new Triangle(result);
	}
	
	public static Set<VoronoiCell> generateVoronoiFromDelaunay (Set<Triangle> tris, Diagram diag) {
		Set<Point> pointSet = Triangle.mapTrianglesToPoints(tris);
		Set<VoronoiCell> result = new HashSet<VoronoiCell>();
		for (Point p : pointSet) {
			Set<Triangle> adjacentTris = new HashSet<Triangle>();
			for (Triangle t : tris) {
				if (t.endpoints.contains(p)) { adjacentTris.add(t); }
			}
			result.add(generateVoronoiCell(p, adjacentTris, diag));
		}
		return result;
	}
	
	private static VoronoiCell generateVoronoiCell (Point generator, Set<Triangle> adjTris, Diagram diag) {
		VoronoiCell result = new VoronoiCell();
		result.generatingPoint = generator;
		Set<Line> adjTriLines = new HashSet<Line>();
		for (Line l : Triangle.mapTrianglesToLines(adjTris)) {
			if (l.endpoints.contains(result.generatingPoint)) { adjTriLines.add(l); }
		}
		
		for (Line l : adjTriLines) {
			Triangle t1 = null;
			Triangle t2 = null;
			for (Triangle t : adjTris) {
				if (t.generateLines().contains(l)) {
					if (t1 == null) { t1 = t; }
					else			{ t2 = t; }
				}
			}
			if (t2 == null) { result.truncated = true; }
			else {
				Line newLine = new Line(t1.cCircle.centerPoint, t2.cCircle.centerPoint);
				//if (diag.crossesDiagramBoundary(newLine)) { result.truncated = true; }
				result.edges.add(newLine);
			}
		}
		
		return result;
	}
	
	public static Set<Set<Point>> createGroups (Set<VoronoiCell> cells, Set<Line> delaunayLines) {
		Set<Set<Point>> result = new HashSet<Set<Point>>();
		Set<Point> copy = new HashSet<Point>(VoronoiCell.mapCellsToGenPoints(cells));
		while (!copy.isEmpty()) {
			Set<Point> seenPoints = new HashSet<Point>();
			seenPoints.add(copy.iterator().next());
			boolean running = true;
			while (running) {
				Set<Line> singleIntersect = Line.lineToPointSetIntersectionSingle(delaunayLines, seenPoints);
				singleIntersect.removeAll(Line.lineToPointSetIntersectionDouble(delaunayLines, seenPoints));
				if (singleIntersect.isEmpty()) {
					Set<Point> truncated = VoronoiCell.mapCellsToGenPoints(VoronoiCell.getTruncatedCells(cells));
					copy.removeAll(seenPoints);
					seenPoints.removeAll(truncated);
					result.add(seenPoints);
					running = false;
				} else {
					seenPoints.addAll(Line.mapLinesToPoints(singleIntersect));
				}
			}
		}
		return result;
	}
	
	//This method has issues...
	private static VoronoiCell generateVoronoiCellWithTruncation (Point generator, Set<Triangle> adjTris, Diagram diag) {
		VoronoiCell result = new VoronoiCell();
		result.generatingPoint = generator;
		Set<Line> adjTriLines = new HashSet<Line>();
		for (Line l : Triangle.mapTrianglesToLines(adjTris)) {
			if (l.endpoints.contains(generator)) { adjTriLines.add(l); }
		}
		
		//Find triangles that have this line
		for (Line l : adjTriLines) {
			Triangle t1 = null;
			Triangle t2 = null;
			for (Triangle t : adjTris) {
				if (t.generateLines().contains(l)) {
					if (t1 == null) {
						t1 = t;
						continue;
					} else {
						t2 = t;
						break;
					}
				}
			}
			
			//Found 2 triangles that share this line
			//Constructing line between their Circumcircle centers
			if (t2 != null) {
				if (!diag.isInsideDiagram(t1.cCircle.centerPoint) || !diag.isInsideDiagram(t2.cCircle.centerPoint)) { result.truncated = true; }
				result.edges.add(new Line(t1.cCircle.centerPoint, t2.cCircle.centerPoint));
			} else {		//This voronoi cell is on the outside of the tessellation, must make other point for line to connect to outside diagram boundary
				result.truncated = true;
				if (!diag.isInsideDiagram(t1.cCircle.centerPoint)) { continue; }
				Set<Point> temp = new HashSet<Point>(t1.endpoints);
				temp.removeAll(l.endpoints);
				Point farPoint = temp.iterator().next();
				
				double slope = (-1) / l.slope(); 
				List<Point> boundaryPoints = diag.createPointsOnBoundary(t1.cCircle.centerPoint, slope);

				if (l.onSameSide(boundaryPoints.get(0), farPoint)) {
					result.edges.add(new Line(t1.cCircle.centerPoint, boundaryPoints.get(1)));
				} else {
					result.edges.add(new Line(t1.cCircle.centerPoint, boundaryPoints.get(0)));
				}
			}
		}
		if (result.truncated) { VoronoiCell.truncateCell(result, diag); }		//Also has issues
		return result;
	}
	
	
}
