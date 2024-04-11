import java.rmi.Naming;

public class ServidorCalculadoraRMI {
    public static void main(String[] args) {
        try {
            CalculadoraServidor calculadora = new CalculadoraServidor();
            Naming.rebind("Calculadora", calculadora);
            System.out.println("Servidor RMI de la calculadora iniciado.");
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor RMI:");
            e.printStackTrace();
        }
    }
}

