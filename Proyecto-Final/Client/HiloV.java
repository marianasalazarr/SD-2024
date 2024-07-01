//Clases
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;




public class HiloV {
    
        private Timer timer;
        private TimeOutTask task;
        JProgressBar pbFile;
        Progreso p;
        ClientImplementacion c;
        
        long startTime;
        long endTime;
        // Constructor del hilo de visualización
        HiloV(ClientImplementacion c,JProgressBar pbFile ,Progreso p){
            this.c=c;
            this.pbFile=pbFile;
            this.p=p;
        }
        // Inicia el hilo de visualización
        public void start() {
             timer = new Timer();
             task = new TimeOutTask();
             startTime = System.nanoTime();//con esto se calcula el tiempo de descarga
             timer.scheduleAtFixedRate(task, 0, 100);//se manda a llamar la tarea cada 100ms (se actualiza la ventana)
        }
        // Detiene el hilo de visualización
        public void stop() {
        // Calcula la duración de la descarga
            endTime=System.nanoTime();
            double duration = (endTime - startTime)/1000000000.0;
            DecimalFormat df = new DecimalFormat("#.##");
            System.out.println("Download Response time: "+ df.format(duration) + "s");
            // Muestra un mensaje de finalización de descarga
            JOptionPane.showMessageDialog(null, "Descarga finalizada");
            // Detiene el temporizador
            timer.cancel();
            timer.purge();
        }
        // Clase interna para manejar la tarea de temporizador
        class TimeOutTask extends TimerTask {
            @Override
            public void run() {
                // Actualiza la barra de progreso con el progreso actual
                    pbFile.setValue(p.getValue());
                    // Si la descarga está completa, realiza la unión de las partes del archivo
                    if(p.getValue()==100){//se completo el progreso
                        c.unir(p.getFilename(),p.getPiecesLength());
                        // Detiene el hilo de visualización
                        stop();
                    }
            }
        }
    }
