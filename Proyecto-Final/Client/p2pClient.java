import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class p2pClient {

    public static void main(String args[]) throws Exception {
        PeerData p;
        String serverHost;
        int serverPort;
        Registry registry = null;
        String instanceName;
        ServerInterface IndexServer = null;

        if (args.length != 5) {
            System.out.println("usage: p2pClient <serverHost> <serverPort> <ClientHost> <clientPort> <idPeer>\n\n");
            return;
        }
        try {
            serverHost = args[0];
            serverPort = Integer.parseInt(args[1]);
            p = new PeerData(Integer.parseInt(args[4]), args[2], Integer.parseInt(args[3]));

        } catch (NumberFormatException e) {
            System.err.println("Argument must be and Integer");
            return;
        }

        if (serverPort == p.port) {
            System.out.println("Server's port can't be equal to Client's port \n\n");
            return;
        }

        instanceName = "Peer" + p.Id;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            registry = LocateRegistry.getRegistry(serverHost, serverPort);
            IndexServer = (ServerInterface) registry.lookup("/server");

            if (IndexServer.getPeer(p.Id) != null) {
                System.out.println("peerId already exists. Please select another peerId");
                System.exit(0);
            }
        } catch (RemoteException e) {
            System.out.println("\nError - Server \"" + serverHost + ":" + serverPort + "/server\" not found.");
            System.exit(0);
        }

        ClientImplementacion ClientImplementacion = new ClientImplementacion(IndexServer, p);
        registry = getRegistry(p.port);

        try {
            registry.rebind(instanceName, ClientImplementacion);
        } catch (RemoteException e) {
            System.out.println("\nError binding Peer " + p.Id + " Please select another peerId");
            System.exit(0);
        }

        System.out.println("ClientServer running... \"" + p.host + ":" + p.port + "/" + instanceName + "\"");

        while (ClientImplementacion.run());

        System.out.println("Adios");

        try {
            registry = getRegistry(p.port);
            registry.unbind(instanceName);
        } catch (NotBoundException e) {
            System.out.println("Error - NotBoundException " + p);
        } catch (AccessException e) {
            System.out.println("Error - AccessException " + p);
        } catch (NullPointerException e) {
            System.out.println("Error - NullPointerException " + p);
        } catch (RemoteException e) {
            System.out.println("Error - RemoteException " + p);
        }

        try {
            IndexServer.unregistryPeer(p);
        } catch (RemoteException em) {
            System.out.println("\nError - No se pudo conectar con el servidor.\n");
        } finally {
            System.exit(0);
        }
    }

    private static Registry getRegistry(int port) {
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (ExportException e) {
            try {
                registry = LocateRegistry.getRegistry(port);
            } catch (RemoteException em) {
                System.out.println("\nError - No se pudo obtener el registro.\n");
                System.exit(0);
            }
        } catch (RemoteException ex) {
            System.out.println("\nError - No se pudo crear el registro.\n");
            System.exit(0);
        }
        return registry;
    }

    private static String getHost() {
        String host_ = "";
        String prefijo = "192";
        boolean buscando = true;
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements() && buscando) {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements() && buscando) {
                    InetAddress i = ee.nextElement();
                    host_ = i.getHostAddress();
                    if (host_.startsWith(prefijo))
                        buscando = false;
                }
            }

        } catch (SocketException ex) {
            Logger.getLogger(p2pClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return host_;
    }
}
