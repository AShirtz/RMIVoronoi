package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.Point;
import utils.Triangle;

//TODO: Implement the Bowyer-Watson Algorithm
/*
 * 
 */

public class CCWSortingAndCircumcirleTest {

	public static void main(String[] args) {
		List<Point> result = new ArrayList<Point>(3);
		result.add(new Point(1, 5));
		result.add(new Point(4,4));
		result.add(new Point(5, 1));
		
		Triangle temp = new Triangle(result);
		System.out.println(temp.toString());
		
		Point p = new Point(2,2);
		System.out.println(temp.isInsideCircumcircleDistance(p));
		System.out.println(temp.isInsideCircumcircle(p));
		
		Point[] superPoints = {
				new Point(-2000, -2000), 
				new Point(2000, -2001), 
				new Point(0, 2000) };
		
		List<Point> ps = Arrays.asList(superPoints);
		
		temp = new Triangle(ps);
		//System.out.println(temp.toString());
		
		List<Point> ordered = Triangle.orderPointsCCW(result);
		//System.out.println(result.get(0).toString() + " , " + result.get(1).toString() + " , " + result.get(2).toString());
		//System.out.println(ordered.get(0).toString() + " , " + ordered.get(1).toString() + " , " + ordered.get(2).toString());
		
		//System.out.println(temp.isInsideCircumcircle(p));
	}

}
