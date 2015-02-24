package utils;

public class Triangle {

	Point endpoints[] = new Point[3];
	
	public Triangle(Point points[]) {
		//The following code is to put the points into the endpoints[] sorted in Counterclockwise order
		//This is required for the isInsideCircumcircle method
		
		//Error checking
		if (points.length != 3) {
			//Error out
		} else if (points[0].equals(points[1]) || points[0].equals(points[2]) || points[1].equals(points[2])) {
			//Error out
		}
		
		//Find point with lowest yCoord
		int lowestPointIndex = 0;
		if (points[lowestPointIndex].yCoord > points[1].yCoord) { lowestPointIndex = 1; }
		if (points[lowestPointIndex].yCoord > points[2].yCoord) { lowestPointIndex = 2; }
		
		//Find the slopes between the lowest point and the other two points
		double m1 = (points[lowestPointIndex].yCoord - points[(lowestPointIndex+1)%3].yCoord) / (points[lowestPointIndex].xCoord - points[(lowestPointIndex+1)%3].xCoord);
		double m2 = (points[lowestPointIndex].yCoord - points[(lowestPointIndex+2)%3].yCoord) / (points[lowestPointIndex].xCoord - points[(lowestPointIndex+2)%3].xCoord);
		
		int nextPointIndex;
		int finalPointIndex;
		
		//If: the signs of slopes differ, the positive slope's endpoint is the next point 
		//Else: the signs of the slopes are the same; if positive, nextPoint is lower slope; if negative, nextPoint is higher slope
		if (m1 > 0 ^ m2 > 0) {
			nextPointIndex = (m1 > 0) ? (lowestPointIndex + 1) % 3 : (lowestPointIndex + 2) % 3;
			finalPointIndex = (m1 > 0) ? (lowestPointIndex + 2) % 3 : (lowestPointIndex + 1) % 3;
		} else {
			if (m1 > 0 && m2 > 0) {
				nextPointIndex = (m1 > m2) ? (lowestPointIndex + 2) % 3 : (lowestPointIndex + 1) % 3;
				finalPointIndex = (m1 > m2) ? (lowestPointIndex + 1) % 3 : (lowestPointIndex + 2) % 3;
			} else {
				nextPointIndex = (m1 < m2) ? (lowestPointIndex + 1) % 3 : (lowestPointIndex + 2) % 3;
				finalPointIndex = (m1 < m2) ? (lowestPointIndex + 2) % 3 : (lowestPointIndex + 1) % 3;
			}
		}
		
		this.endpoints[0] = points[lowestPointIndex];
		this.endpoints[1] = points[nextPointIndex];
		this.endpoints[2] = points[finalPointIndex];
	}
	
	public boolean isInsideCircumcircle (Point p) {
		//This ugly method is for computing the determinant of a specific matrix found at: (as of 2/23/2015)
		//http://en.wikipedia.org/wiki/Delaunay_triangulation#Algorithms
		double a = 
				((this.endpoints[0].xCoord - p.xCoord)
						*(this.endpoints[1].yCoord - p.yCoord)
						*(((Math.pow(this.endpoints[2].xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints[2].yCoord, 2))-(Math.pow(p.yCoord, 2))))
				+ ((this.endpoints[0].yCoord - p.yCoord)
						*(this.endpoints[2].xCoord - p.xCoord)
						*(((Math.pow(this.endpoints[1].xCoord, 2))-(Math.pow(p.xCoord, 2))))+((Math.pow(this.endpoints[1].yCoord, 2))-(Math.pow(p.yCoord, 2))))
				+ ((this.endpoints[1].xCoord - p.xCoord)
						*(this.endpoints[2].yCoord - p.yCoord)
						*(((Math.pow(this.endpoints[2].xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints[2].yCoord, 2))-(Math.pow(p.yCoord, 2))))));
		
		double b = 
				((this.endpoints[1].yCoord - p.yCoord)
						*(this.endpoints[2].xCoord - p.xCoord)
						*(((Math.pow(this.endpoints[0].xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints[0].yCoord, 2))-(Math.pow(p.yCoord, 2)))))
				+((this.endpoints[0].yCoord - p.yCoord)
						*(this.endpoints[1].xCoord - p.xCoord)
						*(((Math.pow(this.endpoints[2].xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints[2].yCoord, 2))-(Math.pow(p.yCoord, 2)))))
				+((this.endpoints[0].xCoord - p.xCoord)
						*(this.endpoints[2].yCoord - p.yCoord)
						*(((Math.pow(this.endpoints[1].xCoord, 2))-(Math.pow(p.xCoord, 2)))+((Math.pow(this.endpoints[1].yCoord, 2))-(Math.pow(p.yCoord, 2)))));
				
		return a > b;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += this.endpoints[0].toString() + "\n";
		result += this.endpoints[1].toString() + "\n";
		result += this.endpoints[2].toString() + "\n";
		return result;
	}

}
