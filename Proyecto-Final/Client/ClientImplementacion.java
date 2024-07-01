//Importaciones de paquetes necesarios
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

// Definición de la clase ClientImpl que implementa la interfaz ClientI y extiende UnicastRemoteObject
public class ClientImplementacion extends UnicastRemoteObject implements ClientInterface  {
// Constante que define la longitud de cada fragmento del archivo
        final int PIECE_LENGTH=524288-16;
        // Clave de encriptación
        static final String key= "Mary has one cat";
        // Referencia al servidor de índices
        ServerInterface IndexServer; 
        // Nombre de la instancia del cliente
        String instanceName; 
        // Nombre del directorio compartido
        String dirname; 
        // Objeto que representa el directorio compartido
        File dir; 
        // Contadores de conexiones entrantes y salientes
        int inConnections;//numero de conexiones de entrada
        int outConnections;//numero de conexiones de salida
        // Datos del peer (ID, dirección IP, puerto)
        PeerData p;
        // Instancia de la clase Fragmentacion para manejar fragmentación y unión de archivos
        final Fragmentacion frg;
        // Estructura de datos concurrente para el progreso de la descarga de archivos
        ConcurrentMap<String,Progreso> progreso; 
    /**
     * @param IndexServer Es la conexion con el servidor
     * @param p Son los datos del peer
     * @throws RemoteException
     */
    // Constructor de la clase ClientImpl
    public ClientImplementacion(ServerInterface IndexServer,PeerData p) throws RemoteException {

                super();
                progreso = new ConcurrentHashMap<String,Progreso>();
                this.IndexServer = IndexServer;
                this.p = p;
                instanceName = "Nodo" + p.Id;//es el nombre de la instancia, el cual corresponde a la carpeta compartida
                dirname = instanceName; //el nombre del directorio. Se manejo con otra variable por si en un futuro se cambiaba la direccion de este
                dir = new File(dirname);
		if (!dir.exists()) {//si el directorio no existe, se crea
			System.out.println("Creating new shared directory");
			dir.mkdir();
		}
                frg=new Fragmentacion();
	}
        
    /**
     *
     * @return
     */
    // Método principal para ejecutar el cliente
    public boolean run(){//el metodo principal
            // main UI loop
		int choice=0;//aqui se guarda la opcion escogida
		String s;//sirve para leer la opcion
		Scanner scan = new Scanner(System.in);//sirve para leer la opcion
		InputStreamReader stream = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(stream);//sirve para leer el nombre del archivo
		boolean loop = true;
		String filename;
		// Registro del directorio compartido en el servidor de índices
                register(dir);
                try {
                    // Menú de opciones para el usuario
                    System.out.println("\n\n" + p);
                    System.out.println("Opciones:");
                    System.out.println("1:Buscar archivo por nombre");
                    System.out.println("2:Descargar un archivo");
                    System.out.println("3:Salir");
                    
                    System.out.print("\n\n>");

                    s = scan.nextLine();
                    try { choice = Integer.parseInt(s.trim()); }
                    catch(NumberFormatException e) {
                        // Manejo de excepciones
                        //System.out.println("\nPlease enter an integer\n");
                    }
// Switch para manejar las opciones del menú
                    switch (choice) {


                        case 1:
                            System.out.print("Introduce el nombre del archivo que deseas buscar: ");
                            filename = in.readLine();
                            System.out.print("\n");
                            search(filename);
                            break;

                        case 2:
                            System.out.print("Introduce el nombre del archivo que deseas buscar: ");
                            filename = in.readLine();
                            getFile(filename);
                            
                            break;

                        case 3:
                            
                            loop = false;
                            break;

                        
                        default:
                            System.out.println("\nOpcion invalida\n");
                            break;
                    }

                }
                catch(IOException ex) {
                    Logger.getLogger(ClientImplementacion.class.getName()).log(Level.SEVERE, null, ex);
                    //System.out.println("\nPlease enter an integer\n");
                }

                return loop;
        }
// Implementación del método de la interfaz ClientI para obtener un fragmento de un archivo
        @Override
	public byte[] obtain(String file, int piece) {//es una funcion remota, devuelve un arreglo de bytes
// Control de conexiones entrantes
		if(inConnections<15)inConnections++;//comprueba el limite de conexiones, puesto que 15 es el maximo
                else return null;
                
                byte[] bytes = null;
                // Ruta del archivo fragmentado 
		String pathfile = instanceName + "/" + file+"."+piece+".bin";

		// Objeto que representa el archivo fragmentado
		File readfile = new File(pathfile);
        // Verificación de existencia del archivo fragmentado
		if (!readfile.exists()) { 
                    String pathfile2frag=instanceName + "/" + file; 
                    File file2frag = new File(pathfile2frag); 
                    if (!file2frag.exists()) {
                        inConnections--;
                        return null;
                    }
                    if(piece==0){
                        int size = (int) file2frag.length();
                        if(size<=PIECE_LENGTH){
                            bytes = frg.read(file2frag);
                            inConnections--;
                            try{
                                bytes = CryptoUtils.encrypt(key, bytes);
                            }catch (CryptoException ex) {
                                System.out.println(ex.getMessage());
                                ex.printStackTrace();
                            }
                            return bytes;
                        }
                    }
                    frg.fragmentar(pathfile2frag,instanceName+"/",PIECE_LENGTH);
		}
                bytes = frg.read(readfile); 
		inConnections--;
		return bytes; 

	}
    // Método para listar los archivos compartidos por el peer
	
        public void list() {//muesta los archivos completos que se estan compartiendo
		File[] sharedfiles = dir.listFiles();
		System.out.println("\n\nArchivos compartidos en directorio: ");
                for (int i = 0; i < sharedfiles.length; i++) {
                    if(! sharedfiles[i].getName().endsWith(".bin")){
                        System.out.println(sharedfiles[i].getName());
                    }
                    
		}
		System.out.print("\n\n");

	}
    // Método para buscar un archivo en el servidor de índices
	private void search(String filename)
			throws RemoteException {

		double startTime=System.nanoTime();//sirve para contar el tiempo de respuesta
		double endTime;
                
		try {
                        // Obtener la lista de peers que tienen el archivo
			List<Integer> peers =  IndexServer.searchFile(filename); //obtiene una lista con los ID de los peer

			
			if (peers == null) {
				System.out.println("\n\nNo se han encontrado nodos con el archivo "+ filename + "\n\n");
				return;
			}

			System.out.print("Los siguientes nodos tienen el archivo " + filename+ " :\n");
			// Mostrar los IDs de los peers que tienen el archivo
                        for(Integer pId : peers){
                            System.out.println(pId);
                        }
                        
			System.out.print("\n\n");

		} finally {
			endTime=System.nanoTime();
		}
		
                double duration = (endTime - startTime)/1000000000.0;
                DecimalFormat df = new DecimalFormat("#.##");
                System.out.println("Respuesta de tiempo en descarga: "+ df.format(duration) + "s");
		
	}

    // Método para registrar los archivos compartidos en el servidor de índices
	private void register(File dir){
		File[] sharedfiles = dir.listFiles();
                int totalSF=0;
                Torrent tmp;
		System.out.println(dir.getPath());
			// Verificación de existencia de archivos compartidos
		if (sharedfiles.length == 0) { 
                    System.out.println("No existen archivos");
                    return;
		}
                // Intento de registro en el servidor de índices
                try{
                    IndexServer.registryPeer(p); //se registra el peer por si por alguna razon se perdio su registro en el lado del servidor
                }catch(RemoteException em){
                    System.out.println("\nError - No se pudo conectar con el servidor.\nSe intentara denuevo en 5s;");
                    try {
                        Thread.sleep(5000);
                        register(dir);//basicamente hace un bucle que termina cuando obtiene respuesta del servidor
                        return;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientImplementacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                try{
                    // Registro de cada archivo compartido en el servidor de índices
                    for (int i = 0; i < sharedfiles.length; i++) {

                        if(! sharedfiles[i].getName().endsWith(".bin")){ 
                            tmp = new Torrent(sharedfiles[i].getName(),sharedfiles[i].length()); 
                            tmp.addSeeder(p.Id); 
                            IndexServer.registrySeeder(p.Id,tmp); 
                            Progreso prg = new Progreso(tmp.getName(),tmp.getPieces());
                            prg.setFull();
                            progreso.put(sharedfiles[i].getName(),prg);
                            totalSF++; 
                        }
                    }
                }catch(RemoteException e){
                    System.out.println("Error al conectar con el servidor");
                    return;
                }
                //System.out.println("#Numero de Archivos registrados: " + totalSF);

	}
        // Método para obtener el progreso de descarga de un archivo
        public Progreso getProgress(String filename){
            return progreso.get(filename); //revuelve el progreso relacionado al archivo indicado
        }
        
        // Método para descargar un archivo desde otros peers
	private void getFile(String filename)
			throws FileNotFoundException, IOException {

                List<PeerData> seeders=null;
                List<PeerData> leechers=null;
                int[] connection = new int[4];
                int k=0; 
                Torrent to= IndexServer.getTorrent(filename); 
                if(to==null){ 
                    System.out.println("No se encontró el archivo");
                    return;
                }
		
                
                
                
                seeders = IndexServer.getSeeders(filename);
                if(seeders==null){
                    System.out.println("No hay suficientes seeders");
                    return;
                }
                seeders.remove(p);
                if(seeders.size()<1){
                    System.out.println("No hay sufiecientes seeders");
                    return;
                }
                
                Progreso prg = new Progreso(filename,to.getPieces(),connection);
                if(prg.getPiecesLength()==1){
                    HiloP hilo;
                    for(PeerData sd: seeders){
                        hilo = new HiloP(sd,instanceName,filename);
                        if(hilo.probe()){
                            hilo.start();
                            return;
                        }
                    }
                    System.out.println("No hay sufiecientes seeders");
                    return;
                }
                
                
                progreso.put(filename,prg);
                leechers = IndexServer.getLeechers(filename);
                Torrent tmp = new Torrent(filename,to.getLength());
                tmp.addLeecher(p.Id);
                IndexServer.registryLeecher(p.Id,tmp); 
                
                
                HiloP[] hilos = new HiloP[4];
                
                VentanaD vent = new VentanaD(this,filename,prg); 
                vent.setVisible(true);
                
                Iterator<PeerData> it = seeders.iterator();
                
                while(it.hasNext()){
                    PeerData sd = (PeerData)it.next();
                    hilos[0] = new HiloP(sd,prg,instanceName,filename);
                    if(hilos[0].probe()){
                        System.out.println("Se conectara con SD:"+sd);
                        hilos[0].start();
                        connection[k]=sd.Id;
                        k++;
                        break;
                    }
                }
                
                if(hilos[0]==null){
                    System.out.println("No fue posible conectarse con ningun seeder\nSe ha cancelado la descarga");
                    return;
                }
                leechers.remove(p);
                if(leechers.size()>0){
                    for(PeerData lch: leechers){
                        hilos[k] = new HiloP(lch,prg,instanceName,filename);
                        if(hilos[k].probe()){
                            System.out.println("Se conectara con LCH:"+lch);
                            hilos[k].start();
                            connection[k]=lch.Id;
                            k++;
                            if(k==4){
                                break;
                            }
                        }
                    }
                }
                
                if(k<4){
                    while(it.hasNext()){
                    PeerData sd = (PeerData)it.next();
                        hilos[k] = new HiloP(sd,prg,instanceName,filename);
                        if(hilos[k].probe()){
                            System.out.println("Se conectara con sd:"+sd);
                            hilos[k].start();
                            connection[k]=sd.Id;
                            k++;
                            if(k==4){
                                break;
                            }
                        }
                    }
                }

                System.out.println("Numero de conexiones: "+k);
                
               
	}
        // Método para unir las partes de un archivo descargado
        public void unir(String filename,int piezas){
            
                System.out.println("Completando archivo...");
                String strFilePath = instanceName + "/" + filename;
                File tmpFile = new File(filename);
                if(frg.unirPartes(strFilePath,piezas)){
                    System.out.println("Archivo completado exitosamente.!\n>");
                    try{
                        Torrent tmp = new Torrent(filename,tmpFile.length());
                        tmp.addSeeder(p.Id);
                        IndexServer.registrySeeder(p.Id,tmp);
                    }catch(RemoteException e){
                        System.out.println("Error al conectar con el servidor");
                        return;
                    }
                }
                
            
            
        }
        // Método para obtener el ID del peer
        public int getId() throws RemoteException{
            return p.Id;
        }
        // Implementación del método de la interfaz ClientI para verificar la conexión
        @Override
        public boolean probe() throws RemoteException{
            return true;
        }
}
