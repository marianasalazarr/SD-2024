import java.io.Serializable;
import java.rmi.Remote;
import java.util.Arrays;
import java.util.Objects;

// Clase que representa el progreso de descarga de un archivo
public class Progreso implements Remote, Serializable {
    
    private static final long serialVersionUID = 1L;
    private final String filename;// Nombre del archivo
    private short pieces[];// Array de piezas que representa el progreso de las descargas de cada parte del archivo
    private int[] connection;  // Información sobre la conexión
    private boolean full=false; // Indica si se ha descargado completamente el archivo
    // Constructor utilizado cuando se conocen las piezas descargadas y la información de conexión
    Progreso(String filename,short[] pieces,int[] connection){
        this.filename=filename;
        this.pieces = pieces;
        this.connection=connection;
    }
    // Constructor utilizado cuando se conoce el nombre del archivo y el número total de piezas
    Progreso(String filename,int totalPieces,int[] connection){
        this.filename=filename;
        pieces = new short[totalPieces];
        this.connection=connection;
    }
    // Constructor utilizado cuando solo se conoce el nombre del archivo y se crea un array de piezas vacío
    Progreso(String filename,int totalPieces){
        this.filename=filename;
    }
    // Establece que la descarga está completa
    public void setFull(){
        full=true;
    }
// Obtiene el nombre del archivo
    public String getFilename(){
        return filename;
    }
    // Obtiene el porcentaje de descarga completada
    public int getValue() {//calcula el porcentaje del progreso
        int v=0;
        for(short piece: pieces ){
            if(piece==2)v++;
        }
        v=100*v/pieces.length;
        if(v==100) full=true;
        return v;
    }
// Obtiene el array de piezas que representa el progreso de descarga
    public short[] getPieces() {
        return pieces;
    }
    // Obtiene la longitud del array de piezas
    public int getPiecesLength() {
        return pieces.length;
    }
// Establece el array de piezas que representa el progreso de descarga
    public synchronized void setPieces(short[] pieces) {
        this.pieces = pieces;
    }
     // Obtiene el valor de una pieza en una posición específica
    public short getPieceValue(int i) throws StackOverflowError{
        return pieces[i];
    }
// Establece el valor de una pieza en una posición específica
    public synchronized void setPieceValue(int i,short value) throws StackOverflowError{
        pieces[i] = value;
    }
     // Obtiene el estado de descarga completa
    public boolean getState(){
        return full;
    }
    // Limpia el progreso de descarga
    public void clear(){
        for(int i=0;i<pieces.length;i++ ){
                if(pieces[i]==1){
                    pieces[i]=0;
                }
            }
    }
     // Obtiene el índice de la primera pieza que está marcada como no descargada
    public synchronized int getIndexPieceNull(Progreso p){//obtiene el indice de la primera pieza sin descargar, con la condicion de que la tenga el cliente remoto
        
        if(p.getState()){
            for(int i=0;i<pieces.length;i++ ){
                if(pieces[i]==0){
                    pieces[i]=1;
                    return i;
                }
            }
        }else{
            for(int i=0;i<pieces.length;i++ ){
                if(pieces[i]==0 && getPieceValue(i)==2){
                    pieces[i]=1;
                    return i;
                }
            }
        }
        
        return -1;
    }
// Método generado automáticamente para calcular el código hash del objeto
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.filename);
        hash = 89 * hash + Arrays.hashCode(this.pieces);
        return hash;
    }
    // Método generado automáticamente para comparar objetos por igualdad
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Progreso other = (Progreso) obj;
        if (!Objects.equals(this.filename, other.filename)) {
            return false;
        }
        if (!Arrays.equals(this.pieces, other.pieces)) {
            return false;
        }
        return true;
    }
    
    
    
}
