package server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.Diagram;
import utils.Point;
import utils.rmi.ClientInterface;
import utils.rmi.GameControllerInterface;
import utils.rmi.RemoteDiagram;
import utils.rmi.RemoteDiagramInterface;

public class GameServer {
	
	public static void main(String[] args){
		try{
			System.setSecurityManager(new java.rmi.RMISecurityManager());
			GameControllerInterface GC = new GameController();
			java.rmi.registry.LocateRegistry.createRegistry(8989);
			java.rmi.Naming.rebind("//:8989/game", GC);
		} catch(Exception x) {
			x.printStackTrace();
		}
	}

	public static class GameController extends UnicastRemoteObject implements GameControllerInterface {
		
		protected GameController() throws RemoteException {
			super();
		}

		private static final int height = 1000;
		private static final int width = 1000;
		
		private static final int numTurnsPer = 15;
		
		private List<Point> gameRecord = new ArrayList<Point>();
		
		boolean playerOnesTurn = true;
		int turnsPassed = 0;
		
		ClientInterface playerOne = null;
		ClientInterface playerTwo = null;
		
		Diagram diag = null;
		Set<Point> playerOnesGenPoints = new HashSet<Point>();
		Set<Point> playerTwosGenPoints = new HashSet<Point>();
		
		private void startGame() {
			System.out.println("Game started.");
			diag = new Diagram(width, height);
			try {
				playerOne.acceptGameStart();
				playerTwo.acceptGameStart();
				this.startTurn();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		private void startTurn () throws RemoteException {
			if (turnsPassed >= 2*numTurnsPer) { 
				this.endGame();
			} else {
				turnsPassed++;
				if (playerOnesTurn)		{ playerOne.acceptTurn(); }
				else					{ playerTwo.acceptTurn(); }
			}
		}
		
		private void endGame () throws RemoteException {
			this.recordGame();
			playerOne.acceptGameEnd();
			playerTwo.acceptGameEnd();
			this.resetGame();
		}
		
		private void recordGame () {
			try {
				OutputStream os = new FileOutputStream("logs/" + System.currentTimeMillis() + ".log");
				ObjectOutputStream out = new ObjectOutputStream (os);
				out.writeObject(this.gameRecord);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void registerClient(ClientInterface client)
				throws RemoteException {
			System.out.println("Client registered.");
			if (this.playerOne == null) {
				this.playerOne = client;
			} else {
				this.playerTwo = client;
			}
			if (playerTwo != null) {
				this.startGame();
			}
		}

		@Override
		public void acceptMove (ClientInterface client, Point newPoint)
				throws RemoteException {
			System.out.println("Client moved.");
				if (client.equals(playerOne)) {
					playerOnesGenPoints.add(newPoint);
				} else {
					playerTwosGenPoints.add(newPoint);
				}
				this.diag.addPoint(newPoint);
				this.gameRecord.add(newPoint);
				playerOne.updateBoardView();
				playerTwo.updateBoardView();
				playerOnesTurn = !playerOnesTurn;
				startTurn();
		}

		@Override
		public Diagram getBoard() throws RemoteException {
			return this.diag;
		}

		@Override
		public int getScoreForPlayerOne(ClientInterface client)
				throws RemoteException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getScoreForPlayerTwo(ClientInterface client)
				throws RemoteException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Set<Point> getGenPointsFor(ClientInterface client)
				throws RemoteException {
			if (client.equals(playerOne)) {
				return playerOnesGenPoints;
			} else {
				return playerTwosGenPoints;
			}
		}

		@Override
		public void deRegisterClient(ClientInterface client)
				throws RemoteException {
			this.endGame();
		}

		private void resetGame() {
			this.playerOne = null;
			this.playerTwo = null;
			this.diag = null;
			this.playerOnesGenPoints = new HashSet<Point>();
			this.playerTwosGenPoints = new HashSet<Point>();
			this.gameRecord = new ArrayList<Point>();
			this.turnsPassed = 0;
			this.playerOnesTurn = true;
		}

		
	}
}
