package tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import utils.Algorithms;
import utils.Diagram;
import utils.Line;
import utils.Point;
import utils.Triangle;
import utils.VoronoiCell;

public class SubSetsTest {

	public static void main(String[] args) {
		Diagram diag = new Diagram (1000, 1000);
		
		Set<Point> subSetA = DataPoints.generateSetPoints(25, diag.getWidth(), 0, diag.getHeight(), 0);
		Set<Point> subSetB = DataPoints.generateSetPoints(25, diag.getWidth(), 0, diag.getHeight(), 0);
		
		Set<Point> pointSet = new HashSet<Point>();
		pointSet.addAll(subSetA);
		pointSet.addAll(subSetB);
		
		diag.addPoints(pointSet);
		
		Set<Line> lines = Triangle.mapTrianglesToLines(diag.getDelaunayTriangles());
		Set<Line> l1 = Line.lineToPointSetIntersectionDouble(lines, subSetA);
		Set<Line> l2 = Line.lineToPointSetIntersectionDouble(lines, subSetB);
		
		BufferedImage image = new BufferedImage(diag.getWidth(), diag.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, diag.getWidth(), diag.getHeight());
		
		g.setColor(Color.green);
		for (Line l : lines) {
			//l.drawLine(g);
		}
		
		g.setColor(Color.red);
		for (Line l : l1) {
			//l.drawLine(g);
		}
		g.setColor(Color.green);
		for (VoronoiCell cell : diag.getVoronoiCells()) {
			cell.drawCell(g);
		}
		
		for (VoronoiCell cell : VoronoiCell.genPointCellIntersection(diag.getVoronoiCells(), subSetA)) {
			g.setColor(Color.blue);
			cell.generatingPoint.drawPoint(g);
			cell.fillCell(g);
			g.setColor(Color.black);
			cell.drawCell(g);
		}
		
		
		for (VoronoiCell cell : VoronoiCell.genPointCellIntersection(diag.getVoronoiCells(), subSetB)) {
			g.setColor(Color.red);
			cell.generatingPoint.drawPoint(g);
			cell.fillCell(g);
			g.setColor(Color.black);
			cell.drawCell(g);
		}
		
		for (Set<Point> points : Algorithms.createGroups(VoronoiCell.genPointCellIntersection(diag.getVoronoiCells(), subSetA), Line.lineToPointSetIntersectionDouble(Triangle.mapTrianglesToLines(diag.getDelaunayTriangles()), subSetA))) {
			g.setColor(Color.white);
			for (Point p : points) {
				p.drawPoint(g);
			}
			g.setColor(Color.yellow);
			Set<Line> groupLines = Line.lineToPointSetIntersectionDouble(Triangle.mapTrianglesToLines(diag.getDelaunayTriangles()), points);
			for (Line l : groupLines) {
				l.drawLine(g);
			}
		}
		
		for (Set<Point> points : Algorithms.createGroups(VoronoiCell.genPointCellIntersection(diag.getVoronoiCells(), subSetB), Line.lineToPointSetIntersectionDouble(Triangle.mapTrianglesToLines(diag.getDelaunayTriangles()), subSetB))) {
			g.setColor(Color.white);
			for (Point p : points) {
				p.drawPoint(g);
			}
			g.setColor(Color.green);
			Set<Line> groupLines = Line.lineToPointSetIntersectionDouble(Triangle.mapTrianglesToLines(diag.getDelaunayTriangles()), points);
			for (Line l : groupLines) {
				l.drawLine(g);
			}
		}
		
		g.dispose();
		try {
			ImageIO.write(image, "bmp", new File("SubSetsTest.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		subSetA.removeAll(subSetB);
		subSetA.size();
	}
}
