//Clases
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

//Ventana Swing
public class VentanaD extends JFrame{
    //Objeto con atributo clase
    JProgressBar pbFile;
    //Constructor
    VentanaD(ClientImplementacion c,String title, Progreso p){
        super("Downloading");//Titulo
        //Configuracion de JprogressBar
        pbFile = new JProgressBar(); //Nuevo objeto
        pbFile.setMaximum(100);
        pbFile.setStringPainted(true);
        pbFile.setBorder(BorderFactory.createTitledBorder("Downloading: "+title));
        //Panel de contenido de la ventana
        Container contentPane = this.getContentPane();
        contentPane.add(pbFile, BorderLayout.SOUTH);
        //Hilo de actualizacion de progreso
        HiloV hv= new HiloV(c,pbFile,p);
        hv.start();//Inicio del hilo
        //Ventana con tamaños
        setSize(300, 100);
        //Se coloca la ventana en el centro de la pantalla
        setLocationRelativeTo(null);
    }    
}
