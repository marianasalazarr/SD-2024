//Clasess
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;



public class HiloP {
    
        private Timer timer;
        private TimeOutTask task;
        Progreso p,pB;
        PeerData rp;
        String instanceName;
        String filename;
        Fragmentacion frg;
        ClientInterface servingPeer=null;
        
        static final String key= "Mary has one cat";
        // Constructor para un hilo que se encarga de descargar un archivo desde un peer
        HiloP(PeerData rp,Progreso p,String instanceName, String filename){
            this.rp=rp;
            this.p=p;
            this.instanceName=instanceName;
            this.filename=filename;
            frg = new Fragmentacion();
        }
        // Constructor para un hilo que se encarga de probar la conexión con un peer
        HiloP(PeerData rp,String instanceName, String filename){
            this.rp=rp;
            this.p=null;
            this.instanceName=instanceName;
            this.filename=filename;
            frg = new Fragmentacion();
        }
        // Inicia el hilo
        public void start() {
             // Si el progreso es nulo, realiza la descarga directa del archivo
             if(p==null){
                 getSingleFile();
                 return;
             }
             // Configura un temporizador y una tarea para gestionar el tiempo de espera
             timer = new Timer();
             task = new TimeOutTask();
             timer.schedule(task, 0);
        }
        // Método para probar la conexión con el peer
        public boolean probe(){
            boolean res=false;
            try {
                Registry registry = LocateRegistry.getRegistry(rp.host,rp.port);
                servingPeer =  (ClientInterface) registry.lookup("rmi://"+rp.host+":"+rp.port+"/"
                                + "Peer" + rp.Id);
                res=servingPeer.probe();
            } catch (NotBoundException e) {
                System.out.println("\nError - Invalid peer \""+rp+"\" entered.  Please enter valid peer");
            }catch (RemoteException e) {
                System.out.println("\nError - Invalid peer \""+rp+"\" entered.  Please enter valid peer");
            }
            
            return res;
        }
// Método para descargar un solo archivo desde el peer
        public void getSingleFile(){
            try{
                byte[] temp;
                Registry registry = LocateRegistry.getRegistry(rp.host,rp.port);
                servingPeer =  (ClientInterface) registry.lookup("rmi://"+rp.host+":"+rp.port+"/"
                            + "Peer" + rp.Id);
                temp=servingPeer.obtain(filename,0);
                if(temp==null){
                    System.out.println("Error, temp=null");
                }
                frg.write(CryptoUtils.decrypt(key, temp),instanceName+"/"+filename);
                System.out.println("Download complete");
            } catch (NotBoundException e) {
                    System.out.println("\nError - Invalid peer \""+rp+"\" entered.  Please enter valid peer");
            }catch (RemoteException e) {
                    System.out.println("\nError - RemoteException <class HiloP:start>");
            }catch (CryptoException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
            }
        }
        // Detiene el temporizador
        public void stop() {
            timer.cancel();
            timer.purge();
        }
        // Clase interna para manejar la tarea de temporizador (tiempo de espera)
        class TimeOutTask extends TimerTask {        
            @Override
            public void run() {
                byte[] temp = null;
                int i;
                int tries=0;
                int clears=0;
                try{
                    System.out.println("Conectando con: "+rp);
                    Registry registry = LocateRegistry.getRegistry(rp.host,rp.port);
                    servingPeer =  (ClientInterface) registry.lookup("rmi://"+rp.host+":"+rp.port+"/" + "Peer" + rp.Id);
                    pB=servingPeer.getProgress(filename);
                    while(p.getValue()<100){
                        if((i = p.getIndexPieceNull(pB)) == -1){
                            // Espera y verifica si hay nuevas piezas para descargar
                            Thread.sleep(500);
                            tries++;
                            if(tries>20){
                                p.clear();
                                clears++;
                                if(clears>20){
                                    break;
                                }
                            }
                            continue;
                        }// Obtiene una pieza del archivo
                        temp=servingPeer.obtain(filename,i); 
                        if(temp==null){
                            System.out.println("Error: temp=null; i="+i);
                            Thread.sleep(500);
                            tries++;
                            if(tries>20){
                                p.clear();
                                clears++;
                                if(clears>20){
                                    break;
                                }
                            }
                            continue;
                        }
                        // Escribe la pieza en el archivo local
                        frg.write(temp,instanceName+"/"+filename,i);
                        // Marca la pieza como descargada
                        p.setPieceValue(i,(short)2);
                    }
                    // Detiene el hilo después de completar la descarga
                    stop();
                } catch (NotBoundException e) {
                        System.out.println("\nError - Invalid peer \""+rp+"\" entered.  Please enter valid peer");
                }catch (RemoteException e) {
                        System.out.println("\nError - RemoteException <class HiloP:run>");
                } catch (InterruptedException ex) {
                    System.out.println("\nError - InterruptedException <class HiloP:run:sleep>");
                }
                    
            }
        }
    }
