import java.io.*;
import java.net.*;

public class ServidorDeEco {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Uso desde consola: java ServidorDeEco <numero puerto>");
            System.exit(1);
        }

        int numeroPuerto = Integer.parseInt(args[0]);
        
        try (ServerSocket serverSocket = new ServerSocket(numeroPuerto)) {
            System.out.println("Servidor escuchando en el puerto " + numeroPuerto + "...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    
                    System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Cliente dice: " + inputLine);
                        // Modificar el mensaje de respuesta
                        out.println("Hola que tal");
                    }
                } catch (IOException e) {
                    System.err.println("Error al manejar la conexión del cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            System.exit(1);
        }
    }
}
