package utils;

import java.util.HashSet;
import java.util.Set;

public class VoronoiCell {

	Set<Line> edges = new HashSet<Line>();
	Point generatingPoint = null;
	
	@Override
	public String toString() {
		return "VoronoiCell [edges=" + edges + ", generatingPoint="
				+ generatingPoint + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.hashCode());
		result = prime * result
				+ ((generatingPoint == null) ? 0 : generatingPoint.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoronoiCell other = (VoronoiCell) obj;
		if (edges == null) {
			if (other.edges != null)
				return false;
		} else if (!edges.equals(other.edges))
			return false;
		if (generatingPoint == null) {
			if (other.generatingPoint != null)
				return false;
		} else if (!generatingPoint.equals(other.generatingPoint))
			return false;
		return true;
	}
	
	public static Set<Line> mapCellsToLines (Set<VoronoiCell> cells) {
		Set<Line> result = new HashSet<Line>();
		for (VoronoiCell c : cells) {
			result.addAll(c.edges);
		}
		return result;
	}
	
	public static Set<Point> mapCellsToGenPoints (Set<VoronoiCell> cells) {
		Set<Point> result = new HashSet<Point>(cells.size());
		for (VoronoiCell c : cells) {
			result.add(c.generatingPoint);
		}
		return result;
	}
}