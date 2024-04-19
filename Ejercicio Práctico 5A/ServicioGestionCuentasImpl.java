import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ServicioGestionCuentasImpl extends UnicastRemoteObject implements ServicioGestionCuentas {
    // Mapa para almacenar las cuentas de usuario (nombre de usuario y contraseña)
    private Map<String, String> cuentas;

    public ServicioGestionCuentasImpl() throws RemoteException {
        super();
        // Inicializar el mapa de cuentas
        this.cuentas = new HashMap<>();
    }

    @Override
    public boolean registrarUsuario(String nombreUsuario, String contrasena) throws RemoteException {
        // Verificar si el nombre de usuario ya está registrado
        if (cuentas.containsKey(nombreUsuario)) {
            return false; // El nombre de usuario ya existe
        } else {
            // Registrar el nuevo usuario
            cuentas.put(nombreUsuario, contrasena);
            return true; // Registro exitoso
        }
    }

    @Override
    public boolean iniciarSesion(String nombreUsuario, String contrasena) throws RemoteException {
        // Verificar si el nombre de usuario existe y la contraseña coincide
        return cuentas.containsKey(nombreUsuario) && cuentas.get(nombreUsuario).equals(contrasena);
    }

    @Override
    public boolean cerrarSesion(String nombreUsuario) throws RemoteException {
        // La implementación del cierre de sesión depende de la lógica específica de la aplicación
        // En este ejemplo, simplemente devolvemos true como indicación de que la sesión ha sido cerrada
        return true;
    }

    @Override
    public boolean actualizarInformacion(String nombreUsuario, String nuevaInformacion) throws RemoteException {
        // La implementación de actualización de información depende de la lógica específica de la aplicación
        // En este ejemplo, simplemente devolvemos true como indicación de que la información ha sido actualizada
        return true;
    }
}

