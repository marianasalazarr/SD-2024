import java.rmi.Naming;
import java.util.Scanner;

public class ClienteCalculadoraRMI {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            
            // Pedir al usuario que ingrese los números flotantes
            System.out.println("Ingrese el primer número:");
            float a = scanner.nextFloat();
            System.out.println("Ingrese el segundo número:");
            float b = scanner.nextFloat();

            // Conectar con el servidor RMI
            CalculadoraRMI calculadora = (CalculadoraRMI) Naming.lookup("rmi://localhost/Calculadora");

            // Realizar las operaciones
            System.out.println("Suma: " + calculadora.sumar(a, b));
            System.out.println("Resta: " + calculadora.restar(a, b));
            System.out.println("Multiplicación: " + calculadora.multiplicar(a, b));
            System.out.println("División: " + calculadora.dividir(a, b));
            
            scanner.close();
        } catch (Exception e) {
            System.err.println("Error en el cliente RMI:");
            e.printStackTrace();
        }
    }
}

