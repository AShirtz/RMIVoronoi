package utils;

public class Line {
	
	Point a = null;
	Point b = null;

	public Line(Point a, Point b) {
		this.a = a;
		this.b = b;
	}

	public boolean equals (Object obj) {
		if (obj.getClass().equals(Line.class)) {
			Point A = ((Line)obj).a;
			Point B = ((Line)obj).b;
			if ((this.a.equals(A) || this.a.equals(B)) && (this.b.equals(A) || this.b.equals(B))) {
				return true;
			}
		}
		return false;
	}
}
