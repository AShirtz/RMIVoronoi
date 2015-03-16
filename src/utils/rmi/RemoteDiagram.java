package utils.rmi;

import java.rmi.RemoteException;
import java.util.Set;

import utils.Diagram;
import utils.Point;
import utils.Triangle;
import utils.VoronoiCell;

public class RemoteDiagram extends java.rmi.server.UnicastRemoteObject implements RemoteInterface {
	
	private Diagram diag = null;

	public RemoteDiagram(int width, int height) throws RemoteException {
		super();
		this.diag = new Diagram(width, height);
	}

	@Override
	public void addPoints(Set<Point> pointSet) {
		this.diag.addPoints(pointSet);
	}

	@Override
	public Set<VoronoiCell> getCells() {
		return this.diag.getVoronoiCells();
	}

	@Override
	public Set<Triangle> getTriangles() {
		return this.diag.getDelaunayTriangles();
	}

	@Override
	public int getWidth() {
		return this.diag.getWidth();
	}

	@Override
	public int getHeight() {
		return this.diag.getHeight();
	}

}
