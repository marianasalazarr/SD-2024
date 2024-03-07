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

    // Configurar direcci�n del servidor
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

    // Aceptar conexi�n entrante
    if ((cliente_fd = accept(servidor_fd, (struct sockaddr *)&cliente_addr, (socklen_t *)&addr_len)) < 0) {
        perror("Error al aceptar la conexi�n");
        exit(EXIT_FAILURE);
    }

    printf("Cliente conectado.\n");

    // Recibir n�meros del cliente y responder
    while (1) {
        // Recibir n�mero del cliente
        if (recv(cliente_fd, &num, sizeof(int), 0) < 0) {
            perror("Error al recibir el n�mero del cliente");
            break;
        }

        // Incrementar el n�mero recibido
        num++;

        // Enviar n�mero incrementado al cliente
        if (send(cliente_fd, &num, sizeof(int), 0) < 0) {
            perror("Error al enviar el n�mero al cliente");
            break;
               }

        // Terminar si el cliente env�a 0
        if (num == 0) {
            printf("El cliente ha enviado 0. Terminando la conexi�n.\n");
            break;
        }
    }

    // Cerrar los sockets
    close(cliente_fd);
    close(servidor_fd);

    return 0;
}
