import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class ServidorTienda {
    public static void main(String args[]) {
        try {
            // Crear los proveedores
            ProveedorCompra proveedorCompra = new ProveedorCompra();
            ProveedorCarrito proveedorCarrito = new ProveedorCarrito();
            ProveedorVerCarrito proveedorVerCarrito = new ProveedorVerCarrito();
            ProveedorPagar proveedorPagar = new ProveedorPagar();

            // Registrar los proveedores en el registro RMI
            String direccionIP = "localhost"; // Cambiar a la dirección IP del servidor si es necesario
            int puertoRMI = 1099; // Puerto RMI predeterminado
            String rutaCompra = "//" + direccionIP + ":" + puertoRMI + "/Compra";
            String rutaCarrito = "//" + direccionIP + ":" + puertoRMI + "/Carrito";
            String rutaVerCarrito = "//" + direccionIP + ":" + puertoRMI + "/VerCarrito";
            String rutaPagar = "//" + direccionIP + ":" + puertoRMI + "/Pagar";

            Registry registry = LocateRegistry.createRegistry(1099);

            Naming.rebind(rutaCompra, proveedorCompra);
            Naming.rebind(rutaCarrito, proveedorCarrito);
            Naming.rebind(rutaVerCarrito, proveedorVerCarrito);
            Naming.rebind(rutaPagar, proveedorPagar);

            System.out.println("Servidores de tienda listos");

            // Crear el servicio de gestión de cuentas
            ServicioGestionCuentasImpl servicioGestionCuentas = new ServicioGestionCuentasImpl();

            // Registrar algunos usuarios de ejemplo
            servicioGestionCuentas.registrarUsuario("marchi", "netito");
            servicioGestionCuentas.registrarUsuario("netito", "marchi");
            servicioGestionCuentas.registrarUsuario("usuario3", "contrasena3");

            // Registrar el servicio de gestión de cuentas en el registro RMI
            registry.rebind("GestionCuentas", servicioGestionCuentas);
            System.out.println("Servidor de gestión de cuentas listo");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

