import java.rmi.Naming;
import java.util.Scanner;

public class Cliente2 {
    public static void main(String[] args) {
        try {
            // Conectar con el servidor RMI
            ServicioGestionCuentas servicioGestionCuentas = (ServicioGestionCuentas) Naming.lookup("rmi://localhost/GestionCuentas");

            // Solicitar al usuario que ingrese los datos de inicio de sesión
            Scanner scanner = new Scanner(System.in);
            System.out.println("Por favor, ingrese su nombre de usuario:");
            String nombreUsuario = scanner.nextLine();
            System.out.println("Por favor, ingrese su contraseña:");
            String contrasena = scanner.nextLine();

            // Iniciar sesión con los datos proporcionados
            boolean inicioSesionExitoso = servicioGestionCuentas.iniciarSesion(nombreUsuario, contrasena);
            if (inicioSesionExitoso) {
                System.out.println("Inicio de sesión exitoso.");

                // Ofrecer opciones adicionales al usuario
                // En este caso, como es Cliente2, solo se imprimirá un mensaje de inicio de sesión exitoso.
                // Este cliente no maneja más funcionalidades.
            } else {
                System.out.println("Inicio de sesión fallido. Verifique sus credenciales.");
            }

            // Cerrar el scanner
            scanner.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

