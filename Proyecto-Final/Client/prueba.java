import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class prueba{


public static void main(String[] args){
//Gestionar la seguridad en el entorno de ejecución
                if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
    
		try {
			//Se utiliza para obtener un registro en la máquina local y en el puerto especificado.
                        Registry registry = LocateRegistry.getRegistry(4455);
						//Busqueda de objeto remoto
			ClientInterface servingPeer =  (ClientInterface) registry.lookup("rmi://localhost:4455/Client2");
			//servingPeer.list();
			//Manejo de excepciones, para objetos remotos no regisrados o si existe un error en comuicacion remota
		} catch (NotBoundException e) {
			
			System.out.println("\nError - Invalid peer entered.  Please enter valid peer");
                        e.printStackTrace();
                }catch (RemoteException e) {
                        System.out.println("\nError - RemoteException");
                        e.printStackTrace();
                }
		
	}
}
