//Definicion de la interfaz, este proporciona metodos remotos que pueden ser invocador por un cliente en un un entorno de RMI (Remote Method Invocation)

import java.rmi.*;

public interface ClientInterface extends java.rmi.Remote {
// Método para obtener una pieza específica de un archivo
    /**
     * @param file: Nombre del archivo
     * @param piece: El numero de la pieza a obtener
     * @return un arreglo de bytes, con la pieza
     * @throws RemoteException
     */
    public byte[] obtain(String file,int piece) 
		throws RemoteException;
        //Metodo para probar la conexion
    /**
     * @return
     * @throws RemoteException
     */
    public boolean probe()
                throws RemoteException;
       // Método para obtener el objeto de progreso para un archivo específico 
    /**
     * @param filename: el nombre del archivo del cual se quiere obtener su progreso
     * @return
     * @throws RemoteException
     */
    public Progreso getProgress(String filename) 
                throws RemoteException;
       // Método que ya no se utiliza, pero podría ser utilizado para crear nuevas conexiones y evitar conectarse con los ya conectados 
    /**
     * @return
     * @throws RemoteException
     */
    public int getId() 
                throws RemoteException;
	
}
