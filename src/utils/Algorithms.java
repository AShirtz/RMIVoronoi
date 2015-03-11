package utils;

import java.util.HashSet;
import java.util.Iterator;
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
	
	public static Set<VoronoiCell> generateVoronoiFromDelaunay (Set<Triangle> tris) {
		Set<Point> pointSet = Triangle.mapTrianglesToPoints(tris);
		Set<VoronoiCell> result = new HashSet<VoronoiCell>();
		for (Point p : pointSet) {
			VoronoiCell curCell = new VoronoiCell();
			curCell.generatingPoint = p;
			Set<Triangle> adjacentTris = new HashSet<Triangle>();
			for (Triangle t : tris) {
				if (t.endpoints.contains(p)) { adjacentTris.add(t); }
			}
			Triangle curTri = adjacentTris.iterator().next();
			Triangle prevTri = null;
			for (int i = 0; i < adjacentTris.size(); i++) {
				Triangle nextTri = null;
				for (Triangle t : adjacentTris) {
					if (!curTri.equals(t) && !t.equals(prevTri) && curTri.isNeighbor(t)) {
						nextTri = t;
						break;
					}
				}
				if (nextTri == null) {
					result.size();
				}
				curCell.edges.add(new Line(curTri.cCircle.centerPoint, nextTri.cCircle.centerPoint));
				
				prevTri = curTri;
				curTri = nextTri;
			}
			result.add(curCell);
		}
		return null;
	}
}
