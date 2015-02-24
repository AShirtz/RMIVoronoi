package utils;

public class Point {

	double xCoord;
	double yCoord;
	
	public Point(double x, double y) {
		this.xCoord = x;
		this.yCoord = y;
	}
	
	public double distance (Point p) {
		double result = (this.xCoord - p.xCoord) * (this.xCoord - p.xCoord) + (this.yCoord - p.yCoord) * (this.yCoord - p.yCoord);
		result = Math.sqrt(result);
		return result;
	}
	
	public boolean equals(Object obj) {
		if (obj.getClass() != Point.class) { return false; }
		if (this.xCoord == ((Point)obj).xCoord && this.yCoord == ((Point)obj).yCoord) { return true; }
		return false;
	}
	
	@Override
	public String toString() {
		return "(" + this.xCoord + " , " + this.yCoord + ")";
	}

}
