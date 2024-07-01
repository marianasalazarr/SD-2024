//implementa la lógica del servidor para un sistema de intercambio de archivos peer-to-peer utilizando RMI. Gestionando la registración y desregistración de pares, así como el seguimiento de seeders y leechers asociados a los archivos. La sincronización de los métodos indica que la clase está diseñada para ser utilizada en un entorno concurrente.

//Se importan las clases necesarias para trabajar con RMI, manejar registros RMI, fechas y estructuras de datos concurrentes.

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerImplementacion extends UnicastRemoteObject implements ServerInterface {  //En esta clase se extiende UnicastRemoteObject e implementa la interfaz, esta clase faciita la exportacion de objetos remotos

	private static final long serialVersionUID = 1L;

    //Declaracion de variables miembro para almacenar la informacion del tracker y los datos de los pares, asi como objetos para formatear fechas y horas
	private volatile ConcurrentMap<String, Torrent> Tracker;
	private volatile ConcurrentMap<Integer,PeerData> peersId;
        
        DateFormat dateFormat;
        Calendar cal;

    //Este constructor inicializa las estructuras de datos y objetos de formato de la fecha
	public ServerImplementacion() throws RemoteException {
		super();
                Tracker = new ConcurrentHashMap<String, Torrent>();
                peersId = new ConcurrentHashMap<Integer,PeerData>();
                dateFormat = new SimpleDateFormat("HH:mm:ss");
                
	}
        
        @Override
	public synchronized boolean registryPeer(PeerData peer) throws RemoteException { //Este metodo registra un nuevo peer en el servidor, gestionando los casos en que el peer ya existe o se trata de una nueva conexion
            PeerData tmp=peersId.get(peer.Id);
            if(tmp == null )  
		{
			peersId.put(peer.Id,peer);
                        cal = Calendar.getInstance();
                        System.out.print(dateFormat.format(cal.getTime())+": ");
                        System.out.println("Se unio el nodo: "+peer.Id);
                        return true;
		}
            else{
                if(peer.equals(tmp) ){
                //Si es peer ya registrado
                    return true;
                }else{ //Conexion con antiguio peer
                    Registry registry = LocateRegistry.getRegistry(peer.host,peer.port);
                    try{
                        ClientInterface peerService =  (ClientInterface) registry.lookup("rmi://"+peer.host+":"+peer.port+"/" + "Peer" + peer.Id);
                        peerService.probe();
                    }catch(NotBoundException e){
                       //Si existe problemas de buscar la informacion del peer, se elimina y se vuelve a generar
                        peersId.put(peer.Id,peer);
                        return true;
                    }
                        return false; //Se conecto exitosamente al anterior peer ya registrado
                }
            }
        }
        //Intenta desregistrar a un par del servidor, verificando primero si la conexion sigue activa
        public synchronized boolean unregistryPeer(PeerData peer) throws RemoteException {
            Registry registry = LocateRegistry.getRegistry(peer.host,peer.port);
            try{
                ClientInterface peerService =  (ClientInterface) registry.lookup("rmi://"+peer.host+":"+peer.port+"/" + "Peer" + peer.Id);
                peerService.probe(); //Confirma si sigue en existencia el stub
                return false;  //Si funciona, retorna falso para que ningun cliente quiere eliminar a alguien mas
            }catch(NotBoundException e){  //Si existe un problema en la busqueda del peer, se elimina de la lista
                
                peersId.remove(peer.Id, peer);
                cal = Calendar.getInstance();
                        System.out.print(dateFormat.format(cal.getTime())+": ");
                System.out.println("Se elimino el nodo: "+peer.Id);
                return true;
            }
            
        }
        
        // Verifica la conexion con los pares y elimina aquellos que no responden
        public synchronized void checkPeers(){
            
            Iterator<Map.Entry<Integer, PeerData>> it = peersId.entrySet().iterator(); // Se elimina con la ayuda de un iterador
            while(it.hasNext()) {
                Map.Entry<Integer, PeerData> entry = it.next();
                Integer key = entry.getKey();
                PeerData peer = entry.getValue();
                try{
                    Registry registry = LocateRegistry.getRegistry(peer.host,peer.port);
                    ClientInterface peerService =  (ClientInterface) registry.lookup("rmi://"+peer.host+":"+peer.port+"/" + "Peer" + peer.Id);
                    peerService.probe();
                }catch(NotBoundException e){  // Si algun peer tiene problema, este se elimina 
                    
                    peersId.remove(key);
                    cal = Calendar.getInstance();
                    System.out.print(dateFormat.format(cal.getTime())+": ");
                    System.out.println("Se elimino el nodo: "+key);
                    
                }catch(RemoteException e){  // Si algun peer tiene algun error, se elimina de la lista
                    //
                    peersId.remove(key);
                    cal = Calendar.getInstance();
                    System.out.print(dateFormat.format(cal.getTime())+": ");
                    System.out.println("Se elimino el nodo: "+key);
                }
            }
        }
        
	@Override
	public synchronized boolean registrySeeder(Integer peer,Torrent filename) throws RemoteException { // Registra un par como seeder para un archhivo especifico
		Torrent tmp=null;  // Se crea un nuevo objeto torrent
        // Verificacion de archivo en existencia
		tmp = Tracker.get(filename.getName());
		//No existe archivo
                if(tmp == null ) 
                {
                        Tracker.put(filename.getName(),filename );
                        cal = Calendar.getInstance();
                        System.out.print(dateFormat.format(cal.getTime())+": ");
                        System.out.println("S["+peer+"] ha a�adido el archivo: "+filename.getName());
                }
                // Existe el archivo
                else 
                {
                    
                    for(Integer id :  tmp.getSeeders()){ //Verificacion de peer y si no existe, se añade
                            if(Objects.equals(id, peer))   //Si esta asociado a un archivo
                            {
                                    return true;
                            }
                    }
                  // El archivo existe pero el peer no
                    tmp.addSeeder(peer);
                    Tracker.put(filename.getName(), tmp);
                    cal = Calendar.getInstance();
                    System.out.print(dateFormat.format(cal.getTime())+": ");
                    System.out.println("S["+peer+"] ha actualizado el archivo: "+filename.getName());

                }
                return true;
	}
        
        public synchronized boolean registryLeecher(Integer peer,Torrent filename) throws RemoteException {  // Registra un par de leecher para un archivo especifico
		Torrent tmp=null; //Nuevo torrent
		//Verificacion de archivo
		tmp = Tracker.get(filename.getName());
                if(tmp == null )   //No existe archvio
                {
                        Tracker.put(filename.getName(),filename );
                        cal = Calendar.getInstance();
                        System.out.print(dateFormat.format(cal.getTime())+": ");
                        System.out.println("L["+peer+"] ha a�adido el archivo: "+filename.getName());
                }
                else  //Si existe archivo
                {
                    //Si el peer no esta en la lista se agrega
                    for(Integer id :  tmp.getLeechers()){
                            if(Objects.equals(id, peer))  //Peer ya tiene archivo asociado
                            {
                                    return true;
                            }
                    }
                     //Archivo existe pero peer no, entonces se agrega
                    tmp.addLeecher(peer);
                    Tracker.put(filename.getName(), tmp);
                    cal = Calendar.getInstance();
                    System.out.print(dateFormat.format(cal.getTime())+": ");
                    System.out.println("L["+peer+"] ha sido a�adido al archivo: "+filename.getName());

                }
		return true;
	}
        
	@Override
	public List<Integer> searchFile(String filename) throws RemoteException {
		// search the data structure for the file names.  return list of peers
		Torrent torrent = null;
		torrent = Tracker.get(filename);
                if(torrent ==null){
                        return null;
                }
                else{
                        return torrent.getSeeders();
                }
	}
        
        @Override
	public List<PeerData> getSeeders(String filename) throws RemoteException {  //Busqueda de archivo en el servidor
		Torrent torrent = null;//Nuevo torrent
                ArrayList<PeerData> peers=null;
		torrent = Tracker.get(filename);
                if(torrent ==null){
                        //System.out.println("Solicitud de archivo no encontrado");
                        return null;
                }
                else{
                    peers = new ArrayList<PeerData>();
                        
                        Iterator it = torrent.getSeeders().iterator();
                        while(it.hasNext()) {
                            Integer i = (Integer)it.next();
                            PeerData p = peersId.get(i);
                            if(p==null)
                                torrent.dropSeeder(i);
                            else{
                                peers.add(p);
                            }
                        }
                }
                return peers;
	}
        
        @Override
	public List<PeerData> getLeechers(String filename) throws RemoteException { //Obtiene lista de leechers asociados a un archhivo
		Torrent torrent = null; //Nuevo torrent
                ArrayList<PeerData> peers = null;
		torrent = Tracker.get(filename);
                if(torrent ==null){
                        //System.out.println("Solicitud de archivo no encontrado");
                        return null;
                }
                else{
                    peers = new ArrayList<PeerData>();
                        for(Integer i: torrent.getLeechers())
                        {
                            PeerData p = peersId.get(i);
                            if(p==null)
                                torrent.dropLeecher(i);
                            else{
                                peers.add(p);
                            }
                        }
                }
                return peers;
	}
	
        public PeerData getPeer(int peerId) throws RemoteException{ //Obtiene los datos de un peer en particular
            PeerData tmp = peersId.get(peerId);
            if(tmp==null)return null;
            return new PeerData(tmp);  //retorna una copia del peer
        }
        
        public Torrent getTorrent(String filename) {  //Obtiene los metadatos de un archivo
            Torrent tmp = null;
            tmp = Tracker.get(filename);
            return tmp;
        }
        
        public String probe() throws RemoteException{return "ok";}  //Prueba la verificacion de la conexion
	
}
