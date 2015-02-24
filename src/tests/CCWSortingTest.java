package tests;

import utils.Point;
import utils.Triangle;

public class CCWSortingTest {

	public static void main(String[] args) {
		Point points[] = new Point[3];
		points[0] = new Point(0, 0);
		points[1] = new Point(0, 2);
		points[2] = new Point(1, 1);
		
		Triangle temp = new Triangle(points);
		System.out.println(temp.toString());
		
		Point p = new Point(0.5, 1);
		System.out.println(temp.isInsideCircumcircle(p));
	}

}
