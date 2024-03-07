import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        final String SERVIDOR_IP = "127.0.0.1";
        final int PUERTO = 12345;

        try (Socket socket = new Socket(SERVIDOR_IP, PUERTO);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor. Introduce un número entero (0 para salir):");

            String inputLine;
            while ((inputLine = stdin.readLine()) != null) {
                // Enviar número al servidor
                out.println(inputLine);

                // Terminar si el cliente envía "0"
                if (inputLine.equals("0")) {
                    break;
                }

                // Recibir respuesta del servidor
                String respuesta = in.readLine();
                System.out.println("Respuesta del servidor: " + respuesta);

                System.out.println("Introduce otro número entero (0 para salir):");
            }
                    } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
    }
}
