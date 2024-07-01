import java.rmi.*; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class p2pServer extends UnicastRemoteObject implements MyRemoteInterface {

    protected p2pServer() throws RemoteException {
        super();
    }

    @Override
    public void someRemoteMethod() throws RemoteException {
        System.out.println("Remote method invoked!");
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: p2pServer <host> <port>");
            System.exit(0);
        }
        
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // Establecimiento del administrador de seguridad
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // Creación e instalación del registro RMI
        try {
            LocateRegistry.createRegistry(port);
            p2pServer server = new p2pServer();
            Naming.rebind("rmi://" + host + ":" + port + "/server", server);
            System.out.println("Server \"rmi://" + host + ":" + port + "/server\" running...");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Bucle principal del programa
        int num = 1;
        HiloS h = new HiloS(server); // Se crea un objeto de hilos
        h.start(); // Se inicia

        System.out.println("Enter 0 to exit: ");
        Scanner scan = new Scanner(System.in);
        while (num != 0) {
            num = scan.nextInt();
        }

        h.stop(); // Detiene la tarea del hilo
        // Desenlace y salida
        try {
            Naming.unbind("rmi://" + host + ":" + port + "/server");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
