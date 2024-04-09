/* Programa random_local.c que incluye únicamente el programa main */
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include "random_local.h"
#include "rand.h" // Incluir el archivo de cabecera para las funciones RPC

int main(int argc, char *argv[])
{
    int misemilla, itera, i;
    if (argc != 3) {
        printf("Uso: %s semilla iteraciones\n", argv[0]);
        exit(1);
    }
    misemilla = (long)atoi(argv[1]);
    itera = atoi(argv[2]);

    // Llamar a la función RPC inicializa_random_1
    CLIENT *clnt = clnt_create("localhost", RAND_PROG, RAND_VERS, "udp");
    if (clnt == NULL) {
        clnt_pcreateerror("localhost");
        exit(1);
    }
    void *result_1;
    result_1 = inicializa_random_1(&misemilla, clnt);
    if (result_1 == (void *)NULL) {
        clnt_perror(clnt, "call failed");
        exit(1);
    }
    clnt_destroy(clnt);

    // Llamar a la función RPC obtiene_siguiente_random_1 iter veces
    for (i = 0; i < itera; i++) {
        double *result_2;
        result_2 = obtiene_siguiente_random_1(NULL, clnt);
        if (result_2 == (double *)NULL) {
            clnt_perror(clnt, "call failed");
            exit(1);
        }
        printf("%d : %.2f\n", i, *result_2);
    }

    exit(0);
}

