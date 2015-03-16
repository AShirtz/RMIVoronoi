package tests;

import java.util.HashSet;
import java.util.Set;

import utils.Point;

public class FindingThatOneBugTest {
	
	public static void main (String[] args) {
		Set<Point> goodSet = DataPoints.generateSetPoints(99, 1000, 0, 1000, 0);
		DataPoints.resetSetRand();
		Set<Point> badSet = DataPoints.generateSetPoints(100, 1000, 0, 1000, 0);
		
		Set<Point> temp = new HashSet<Point>(badSet);
		temp.removeAll(goodSet);
		Point culprit = temp.iterator().next();
		culprit.equals(null);
	}
	
	private static void AnalyzeSet (Set<Point> points) {
		//Find all collinear points, repeated points, etc.
	}
}
