//Creacion de Hilos y tareas programadas

//Clases
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class HiloS { //Creacion de clase que sirve para que se puedan eliminar de peers que no estan conectados de su lista previamente generada
    
        private Timer timer; //Objeto de la clase timer
        private TimeOutTask task; //Objeto de la clase interna TimerTask
        ServerImplementacion s; //Objeto de la clase ServerImpl
        long startTime; //Inicio de tiempo (medidor de tiempo)
        long endTime; //Final de tiempo (medidor de tiempo)
        
        HiloS(ServerImplementacion s){ //Constructor de la clase toma un objeto ServerImpl como parametro
            this.s=s; //El parametro se asigna a s
        }
        public void start() { //Inicializa temporizador, la tarea y establece tiempo de inicio
             timer = new Timer();
             task = new TimeOutTask();
             startTime = System.nanoTime();
             timer.scheduleAtFixedRate(task, 0, 5000); //Se ejecuta repetida mente 5s (5000 milisegundos) asi se puede comprobar los peers conectados
        }
        


        public void stop() { //Calcula el tiempo total de la sesion, hace una diferencia entre el tiempo final y el tiempo de inicio
        
            endTime=System.nanoTime();
            double duration = (endTime - startTime)/1000000000.0;
            DecimalFormat df = new DecimalFormat("#.##"); //Se le da el formato
            System.out.println("Sesion total time: "+ df.format(duration) + "s"); //Se imprime 
            timer.cancel(); //Cancela la tarea, se detiene
            timer.purge(); //Purga la informacion
        }
        class TimeOutTask extends TimerTask { //Clase interna TimeOutTask que extiende de TimerTask 
        
            @Override
            public void run() { //Implementacion de este metodo
                    s.checkPeers(); //Verifica los peers conectados, se ejecutara en el tiempo pre establecido de arriba que son 5 segundos
            }
        }
    }
