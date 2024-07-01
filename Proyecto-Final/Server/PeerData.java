//Esta clase representa datos de peers en el sistema peer to peer y esta diseñada para ser serializable
//Los metodos proporcionan funcionalidades comunes para trabajar con instancias de clases

//Clases

import java.io.Serializable; //Serializacion
import java.rmi.Remote; //Interfaz remota
import java.rmi.RemoteException; //Manejo de excepciones en llamadas remotas
import java.util.Objects; //Manejo de objetos

public class PeerData implements Serializable,Remote{ //Inicio de la clase
    //Variables de instancia
    int Id; //ID
    int port; //Puerto
    String host; //Host
    
    private static final long serialVersionUID = 1L;  //Implementacion de serializacion

    public PeerData(PeerData p)throws RemoteException{  //Se define un constructor que toma como parametro un objeto PeerData y se crea una nueva instancia copiando los valores del objeto
        //Valores del constructor que copia valores
        this.Id = p.Id;
        this.host = p.host;
        this.port = p.port;
    }
    
    public PeerData(int Id, String host,int port)throws RemoteException{ //Otro constructor que toma valores individuales para el identificador, el host y e puerto
        this.Id = Id;
        this.host = host;
        this.port = port;
    } //Se crea una instancia con esos valores y se lanza una excepcion

    @Override  //Sobre escribe
    public int hashCode() {  //Se devuelve un codigo hash basado en los valores
        //Este metodo se utiliza comunmente en operaciones de busqueda en estructuras de datos basadas en hash
        int hash = 3;
        hash = 41 * hash + this.Id;
        hash = 41 * hash + this.port;
        hash = 41 * hash + Objects.hashCode(this.host);
        return hash;
    }

    @Override  //Se sobre escribe el metodo
    public boolean equals(Object obj) {   //Compara dos objetos 
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PeerData other = (PeerData) obj; //Comparacion de (ID)
        if (this.Id != other.Id) {
            return false;
        }
        if (this.port != other.port) { //Comparacion de (Puerto)
            return false;
        }
        if (!Objects.equals(this.host, other.host)) { //Comparacion de (Host)
            return false;
        }
        return true;
    }

    @Override //Se sobre escribe el metodo
    public String toString(){  //Devuelve una representacion de cadena del objeto en el formato Peer(Id,host,port)
        return "Peer:[ "  + Id + " , " + host + " , " + port + " ]";
    }
}
