package client;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Set;

import utils.Diagram;
import utils.Line;
import utils.Point;
import utils.Triangle;
import utils.VoronoiCell;
import utils.rmi.ClientInterface;
import utils.rmi.GameControllerInterface;

public class Client extends Applet implements ClientInterface, MouseListener {
	
	public static Color backgroundColor = Color.white;
	public static Color tileColor = Color.gray;
	public static Color lineColor = Color.darkGray;
	public static Color pointColor = Color.black;
	
	public long UID = new Random().nextLong();
	private GameControllerInterface GC = null;
	Diagram diag = null;
	Set<Point> myPoints = null;
	boolean isMyTurn = false;
	boolean hasGameStarted = false;
	boolean isGameOver = false;
	
	public void init () {
		addMouseListener(this);
		try {
			int portNum = new Random().nextInt(1000) + 1024;
			UnicastRemoteObject.exportObject(this, portNum);
			GC = (GameControllerInterface) java.rmi.Naming.lookup("rmi://euclid.nmu.edu:8989/game");
			GC.registerClient(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (UID != other.UID)
			return false;
		return true;
	}
	
	public void paint (Graphics g) {
		if (!hasGameStarted) {
			g.setColor(Color.black);
			g.drawString("~~~ Waiting for Opponent ~~~", 100, 100);
		} else {
			if (isGameOver) {
				g.setColor(Color.red);
				g.drawString("~~~ GAME OVER ~~~", this.diag.getWidth()/2, this.diag.getHeight()/2);
				
				g.setColor(backgroundColor);
				g.fillRect(0, 0, diag.getWidth(), diag.getHeight());
				
				for (VoronoiCell cell : diag.getVoronoiCells()) {
					g.setColor(tileColor);
					cell.fillCell(g);
					g.setColor(backgroundColor);
					cell.drawCell(g);
				}
			} else {
				g.setColor(backgroundColor);
				g.fillRect(0, 0, diag.getWidth(), diag.getHeight());
				
				for (VoronoiCell cell : VoronoiCell.genPointCellIntersection(diag.getVoronoiCells(), myPoints)) {
					g.setColor(tileColor);
					cell.fillCell(g);
					g.setColor(backgroundColor);
					cell.drawCell(g);
				}
				g.setColor(lineColor);
				for (Line l : Line.lineToPointSetIntersectionDouble(Triangle.mapTrianglesToLines(diag.getDelaunayTriangles()), myPoints)) {
					l.drawLine(g);
				}
				g.setColor(pointColor);
				for (Point p : myPoints) {
					p.drawPoint(g);
				}
				if (isMyTurn) {
					g.setColor(Color.green);
					g.fillOval(0, 0, 10, 10);
				}
			}
			
		}
	}

	@Override
	public void updateBoardView() throws RemoteException {
		System.out.println("Got Board.");
		this.diag = GC.getBoard();
		this.myPoints = GC.getGenPointsFor(this);
		repaint();
	}

	@Override
	public void acceptTurn() throws RemoteException {
		System.out.println("It's my turn.");
		this.isMyTurn = true;
		repaint();
	}

	@Override
	public void acceptGameStart() throws RemoteException {
		this.hasGameStarted = true;
		this.updateBoardView();
		repaint();
	}
	
	@Override
	public void acceptGameEnd() throws RemoteException {
		this.isGameOver = true;
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (isMyTurn) {
			try {
				GC.acceptMove(this, new Point(arg0.getX(), arg0.getY()));
				isMyTurn = false;
				repaint();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void destroy () {
	/*	try {
			//GC.deRegisterClient(this);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} */
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}