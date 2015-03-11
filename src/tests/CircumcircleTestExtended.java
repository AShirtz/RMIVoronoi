package tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import utils.Algorithms;
import utils.Circumcircle;
import utils.Point;
import utils.Triangle;

public class CircumcircleTestExtended {

	static int height = 100;
	static int width = 100;
	
	public static void main (String[] args) {
		Random rand = new Random();
		Triangle t;
		Set<Point> pointList = new HashSet<Point>();
		for (int j = 0; j < 3; j++) {
			pointList.add(new Point(rand.nextInt(width), rand.nextInt(height)));
		}
		t = new Triangle(pointList);
		
		Point p = new Point(rand.nextInt(width), rand.nextInt(height));
		
		System.out.println(t.isInsideCircumcircleDistance(p));
		System.out.println(new Circumcircle(t).isInsideCircumcircle(p));
		
		BufferedImage image = new BufferedImage(width*10, height*10, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		
		t.drawTriangle(g);
		t.drawCircumcircle(g);
		p.drawPoint(g);
		g.dispose();
		
		try {
			ImageIO.write(image, "bmp", new File("TestFile.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
