// La clase torrent encapsula la información relacionada con un archivo en un sistema peer-to-peer, incluyendo detalles como el nombre del archivo, longitud, tamaño de las piezas, seeders y leechers asociados. La implementación de la interfaz Serializable permite que los objetos de esta clase sean transmitidos a través de la red utilizando RMI.

//Clases
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Torrent implements Serializable,Remote{
    //Declaracion de variables
    private String name;
    private final int pieceLength=524288; //Longitud de cada pieza en Bytes (512KB)
    private int pieces;  //Numero de piezas
    private long length;  //Longitud total del archivo en bytes
    private ArrayList<Integer> seeders;
    private ArrayList<Integer> leechers; 
    
    
    private static final long serialVersionUID = 1L;
//Constructores -->Definicion de varios constructores que permiten inicializar instancias de la clase Torrent con diferentes parámetros, como nombre del archivo, longitud
    public Torrent()throws RemoteException{}

    public Torrent(String name, int pieceLength, int pieces, int length) throws RemoteException{
        this.name = name;
        //this.pieceLength = pieceLength;
        this.pieces = (int)Math.ceil((double)length/pieceLength);
        this.length = length;
        this.seeders = new ArrayList<>();
        this.leechers = new ArrayList<>();
    }
    
    public Torrent(String name, long length) throws RemoteException{
        this.name = name;
        this.length = length;
        this.pieces =  (int)Math.ceil((double)length/pieceLength);
        this.seeders = new ArrayList<>();
        this.leechers = new ArrayList<>();
    }
    //Metodos para agregar y eliminar seeders
    public void addSeeder(Integer peerId)throws RemoteException{
        this.leechers.remove(peerId);
        this.seeders.remove(peerId);
        this.seeders.add(peerId);
    }

    public void dropSeeder(Integer peerId )throws RemoteException{
        this.seeders.remove(peerId);
    }

    public ArrayList<Integer> getSeeders() throws RemoteException{
        return (ArrayList<Integer>) seeders.clone();
    }
    //Metodos para agregar y eliminar leechers
    public void addLeecher(Integer peerId)throws RemoteException{
        this.leechers.remove(peerId);
        this.leechers.add(peerId);
    }

    public void dropLeecher(Integer peerId )throws RemoteException{
        this.leechers.remove(peerId);
    }

    public ArrayList<Integer> getLeechers() throws RemoteException{
        return (ArrayList<Integer>) leechers.clone();
    }
    //Metodos para obtener y establecer seeders y leechers
    public void setSeeders(ArrayList<Integer> seeders) throws RemoteException {
        this.seeders =seeders;
    }

    public void setLeechers(ArrayList<Integer> leechers) throws RemoteException {
        this.leechers = leechers;
    }
    //Metodo para obtener propiedades del torrent
    public String getName() throws RemoteException{
        return name;
    }

    public int getPieceLength() throws RemoteException{
        return pieceLength;
    }

    public int getPieces() throws RemoteException{
        return pieces;
    }

    public long getLength() throws RemoteException{
        return length;
    }
    //Metodo para realizar una copia del objeto torrent
    public Torrent copy() throws RemoteException{
        Torrent to = new Torrent(this.name,this.length);
        to.setLeechers(this.getLeechers());
        to.setSeeders(this.getSeeders());
        return to;
    }
  
  
    
    

}
