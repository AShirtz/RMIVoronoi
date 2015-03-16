package server;

import utils.rmi.RemoteDiagram;
import utils.rmi.RemoteInterface;

public class Server {
	public static void main(String[] args){
		try{
			System.setSecurityManager(new java.rmi.RMISecurityManager());
			RemoteInterface v=new RemoteDiagram(1000,1000);
			java.rmi.registry.LocateRegistry.createRegistry(Integer.valueOf(args[1]));
			java.rmi.Naming.rebind("//:"+args[1]+"/voronoi", v);
		} catch(Exception x) {
			x.printStackTrace();
		}
	}
}
