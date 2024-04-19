import java.rmi.Naming;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    public static void main(String[] args) {
        try {
            // Conectar con el servidor RMI
            Registry registry = LocateRegistry.getRegistry("localhost");

            // Buscar el servicio de gestión de cuentas en el registro RMI
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

                // Menú principal después de iniciar sesión
                int opcion;
                do {
                    System.out.println("\n*** Menú Principal ***");
                    System.out.println("1. Realizar operaciones con proveedores");
                    System.out.println("2. Actualizar datos personales");
                    System.out.println("3. Cerrar sesión");

                    opcion = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer del scanner

                    switch (opcion) {
                        case 1:
                            // Realizar operaciones con los proveedores
                            realizarOperacionesProveedores(registry);
                            break;
                        case 2:
                             // Actualizar información personal
                            System.out.println("Por favor, ingrese su nueva información personal:");
                            String nuevaInformacion = scanner.nextLine();
                            boolean actualizacionExitosa = servicioGestionCuentas.actualizarInformacion(nombreUsuario, nuevaInformacion);
                            if (actualizacionExitosa) {
                                System.out.println("Información actualizada correctamente.");
                            } else {
                                System.out.println("Error al actualizar la información.");
                            }
                            break;
                        case 3:
                            System.out.println("Saliendo...");
                            break;
                        default:
                            System.out.println("Opción inválida. Por favor, ingrese una opción válida.");
                    }
                } while (opcion != 3);

            } else {
                System.out.println("Inicio de sesión fallido. Verifique sus credenciales.");
            }

            // Cerrar el scanner
            scanner.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void realizarOperacionesProveedores(Registry registry) {
        try {
            // Buscar los proveedores en el registro RMI
            TiendaOnline proveedorCompra = (TiendaOnline) registry.lookup("Compra");
            TiendaOnline proveedorCarrito = (TiendaOnline) registry.lookup("Carrito");
            TiendaOnline proveedorVerCarrito = (TiendaOnline) registry.lookup("VerCarrito");
            TiendaOnline proveedorPagar = (TiendaOnline) registry.lookup("Pagar");

            // Realizar operaciones con los proveedores
            System.out.println("Resultado del proveedor de compra: " + proveedorCompra.comprar("Producto1"));
            System.out.println("Resultado del proveedor de carrito: " + proveedorCarrito.agregarAlCarrito("Producto2"));
            System.out.println("Resultado del proveedor de ver carrito: " + proveedorVerCarrito.verCarrito());
            System.out.println("Resultado del proveedor de pagar: " + proveedorPagar.pagar());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

