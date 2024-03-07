#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PUERTO 12345
#define TAM_BUFFER 1024

int main() {
    int servidor_fd, cliente_fd;
    struct sockaddr_in servidor_addr, cliente_addr;
    int addr_len = sizeof(cliente_addr);
    int num;

    // Crear socket
    if ((servidor_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("Error al crear el socket");
        exit(EXIT_FAILURE);
    }

    // Configurar dirección del servidor
    memset(&servidor_addr, 0, sizeof(servidor_addr));
    servidor_addr.sin_family = AF_INET;
    servidor_addr.sin_addr.s_addr = INADDR_ANY;
    servidor_addr.sin_port = htons(PUERTO);

    // Vincular el socket al puerto
    if (bind(servidor_fd, (struct sockaddr *)&servidor_addr, sizeof(servidor_addr)) < 0) {
        perror("Error al vincular el socket");
        exit(EXIT_FAILURE);
         // Escuchar en el puerto
    if (listen(servidor_fd, 1) < 0) {
        perror("Error al escuchar en el puerto");
        exit(EXIT_FAILURE);
    }

    printf("Servidor esperando conexiones...\n");

    // Aceptar conexión entrante
    if ((cliente_fd = accept(servidor_fd, (struct sockaddr *)&cliente_addr, (socklen_t *)&addr_len)) < 0) {
        perror("Error al aceptar la conexión");
        exit(EXIT_FAILURE);
    }

    printf("Cliente conectado.\n");

    // Recibir números del cliente y responder
    while (1) {
        // Recibir número del cliente
        if (recv(cliente_fd, &num, sizeof(int), 0) < 0) {
            perror("Error al recibir el número del cliente");
            break;
        }

        // Incrementar el número recibido
        num++;

        // Enviar número incrementado al cliente
        if (send(cliente_fd, &num, sizeof(int), 0) < 0) {
            perror("Error al enviar el número al cliente");
            break;
               }

        // Terminar si el cliente envía 0
        if (num == 0) {
            printf("El cliente ha enviado 0. Terminando la conexión.\n");
            break;
        }
    }

    // Cerrar los sockets
    close(cliente_fd);
    close(servidor_fd);

    return 0;
}
