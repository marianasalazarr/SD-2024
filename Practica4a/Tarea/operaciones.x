/* operaciones.x */
struct operandos {
    int arg1;
    int arg2;
};

program OPERACIONES {
    version OPERACIONES_V1 {
        int suma(struct operandos) = 1;
        int resta(struct operandos) = 2;
        int multiplicacion(struct operandos) = 3;
        int division(struct operandos) = 4;
    } = 1;
} = 1;
