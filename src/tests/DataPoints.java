package tests;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import utils.Point;

public class DataPoints {

	static Random setRand = new Random(1234567890l);
	static Random realRand = new Random();
	
	public static Set<Point> generateSetPoints (int num, int xMax, int xMin, int yMax, int yMin) {
		Set<Point> result = new HashSet<Point>(num);
		for (int i = 0; i < num; i++) {
			result.add(new Point(setRand.nextInt(xMax) + xMin, setRand.nextInt(yMax) + yMin));
		}
		return result;
	}
	
	public static Set<Point> generatePoints (int num, int xMax, int xMin, int yMax, int yMin) {
		Set<Point> result = new HashSet<Point>(num);
		for (int i = 0; i < num; i++) {
			result.add(new Point(realRand.nextInt(xMax) + xMin, realRand.nextInt(yMax) + yMin));
		}
		return result;
	}
}
