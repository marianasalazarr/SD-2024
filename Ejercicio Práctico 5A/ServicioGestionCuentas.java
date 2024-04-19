import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioGestionCuentas extends Remote {
    // Método para registrar un nuevo usuario
    boolean registrarUsuario(String nombreUsuario, String contrasena) throws RemoteException;

    // Método para iniciar sesión
    boolean iniciarSesion(String nombreUsuario, String contrasena) throws RemoteException;

    // Método para cerrar sesión
    boolean cerrarSesion(String nombreUsuario) throws RemoteException;

    // Método para actualizar la información de la cuenta
    boolean actualizarInformacion(String nombreUsuario, String nuevaInformacion) throws RemoteException;
}

