//Este codigo define la interfaz remota para un servidor de indice en un sistema peer to peer, proporcionando metodos para registrar pares y archivos, buscar archivos, obtener informacion sobre seeders y leechers y realiza otras operaciones relacionadas con el sistema p2p.

import java.rmi.*;
import java.util.List;

//Interface para el servidor
public interface ServerInterface extends java.rmi.Remote {
		//Registra datos del un peer
        public boolean registryPeer(PeerData peer) 
		throws RemoteException; 
        //Desregistra los datps de un peer
        public boolean unregistryPeer(PeerData peer) 
		throws RemoteException; 
        
       //Registra un archivo y el peer asociado a este como seeder
	public  boolean registrySeeder(Integer peer,Torrent filename) 
		throws RemoteException; 
        //Registra un archivo y el peer asociado a este como leecher
        public  boolean registryLeecher(Integer peer,Torrent filename) 
		throws RemoteException; 

	// Busca el archivo y retorna una lista de los ID de los peers que lo tienen
	public  List<Integer> searchFile(String filename)
		throws RemoteException;
        //Obtiene una lista de los peers que tienen el archivo como seeder
        public  List<PeerData> getSeeders(String filename)
		throws RemoteException;
        //Obtiene una lista de los peers que tienen el archivo como leecher
        public  List<PeerData> getLeechers(String filename)
		throws RemoteException;
        
		// Retorna los datos de un peer
        public  PeerData getPeer(int peerId)
		throws RemoteException;
        //Obtiene los metadatos del archivo (Torrent)
        public Torrent getTorrent(String filename)
		throws RemoteException;
        //Realiza una operación de prueba (puede ser utilizada para verificar la conexión)
        public String probe() throws RemoteException;
	
}
