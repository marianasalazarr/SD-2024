#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Uso desde consola: %s <nombre de host (computadora)> <numero puerto>\n", argv[0]);
        exit(1);
    }

    char *nombreHost = argv[1];
    int numeroPuerto = atoi(argv[2]);

    struct sockaddr_in servidorAddr;
    memset(&servidorAddr, 0, sizeof(servidorAddr));
    servidorAddr.sin_family = AF_INET;
    servidorAddr.sin_addr.s_addr = inet_addr(nombreHost);
    servidorAddr.sin_port = htons(numeroPuerto);

    int socketCliente = socket(AF_INET, SOCK_STREAM, 0);
    if (socketCliente < 0) {
        perror("Error al crear socket");
        exit(1);
    }

    if (connect(socketCliente, (struct sockaddr *)&servidorAddr, sizeof(servidorAddr)) < 0) {
        perror("Error al conectar");
        exit(1);
    }

    char buffer[BUFFER_SIZE];
    char entradaUsuario[BUFFER_SIZE];

    while (1) {
        printf("Escriba un mensaje: ");
        fgets(entradaUsuario, BUFFER_SIZE, stdin);

        if (send(socketCliente, entradaUsuario, strlen(entradaUsuario), 0) < 0) {
            perror("Error al enviar mensaje");
            exit(1);
        }

        ssize_t bytesRecibidos = recv(socketCliente, buffer, BUFFER_SIZE, 0);
        if (bytesRecibidos < 0) {
            perror("Error al recibir mensaje");
            exit(1);
        } else if (bytesRecibidos == 0) {
            printf("Servidor desconectado.\n");
            break;
        }

        buffer[bytesRecibidos] = '\0';
        printf("El eco del servidor dice: %s", buffer);
    }

    close(socketCliente);

    return 0;
}
