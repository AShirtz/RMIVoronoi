package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Algorithms {

	//Based on the pseudocode found at (as of 2/24/2015)
	//http://en.wikipedia.org/wiki/Bowyer%E2%80%93Watson_algorithm#Pseudocode
	public static Set<Triangle> BowyerWatson (Set<Point> pointSet) {
		Set<Triangle> result = new HashSet<Triangle>();
		
		//Add the super triangle, will encompass all added points
		List<Point> superPoints = new ArrayList<Point>(3);
		{
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
			
			superPoints.add(new Point (xmid - 2*dmax, ymid - dmax + 1));	//The Circumcircle stuff cannot deal with horizontal lines...
			superPoints.add(new Point (xmid, ymid + 2*dmax));
			superPoints.add(new Point (xmid + 2*dmax, ymid-dmax));
		}
		result.add(new Triangle(superPoints));
		
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
				List<Point> newTrianglePoints = new ArrayList<Point>();
				Iterator <Point> iter = l.endpoints.iterator();
				newTrianglePoints.add(p);
				newTrianglePoints.add(iter.next());
				newTrianglePoints.add(iter.next());
				result.add(new Triangle(newTrianglePoints));
			}
			result.size();
		}
		
		//If a triangle contains points from the super triangle remove it from the result
		Set<Triangle> trianglesToRemove = new HashSet<Triangle>();
		for (Triangle t : result) {
			if (t.endpoints.contains(superPoints.get(0)) || t.endpoints.contains(superPoints.get(1)) || t.endpoints.contains(superPoints.get(2))) {
				trianglesToRemove.add(t);
			}
		}
		result.removeAll(trianglesToRemove);
		
		return result;
	}
}
