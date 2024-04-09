#include <stdio.h>
#include <stdlib.h>
#include "operaciones.h"

int main(int argc, char **argv) {
    CLIENT *cl;
    int *resultado;
    operandos argumentos;

    if (argc != 4) {
        printf("Uso: %s servidor arg1 arg2\n", argv[0]);
        return 1;
    }

    cl = clnt_create(argv[1], OPERACIONES, OPERACIONES_V1, "tcp");
    if (cl == NULL) {
        clnt_pcreateerror(argv[1]);
        return 1;
    }

    argumentos.arg1 = atoi(argv[2]);
    argumentos.arg2 = atoi(argv[3]);

    resultado = suma_1(&argumentos, cl);
    if (resultado == NULL) {
        clnt_perror(cl, "llamada a suma_1");
        return 1;
    }
    printf("Suma: %d\n", *resultado);

    resultado = resta_1(&argumentos, cl);
    if (resultado == NULL) {
        clnt_perror(cl, "llamada a resta_1");
        return 1;
    }
    printf("Resta: %d\n", *resultado);

    resultado = multiplicacion_1(&argumentos, cl);
    if (resultado == NULL) {
        clnt_perror(cl, "llamada a multiplicacion_1");
        return 1;
    }
    printf("Multiplicación: %d\n", *resultado);

    resultado = division_1(&argumentos, cl);
    if (resultado == NULL) {
        clnt_perror(cl, "llamada a division_1");
        return 1;
    }
    printf("División: %d\n", *resultado);

    clnt_destroy(cl);
    return 0;
}

