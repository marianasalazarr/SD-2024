#include <stdio.h>
#include <stdlib.h>
#include <rpc/pmap_clnt.h>
#include "operaciones.h"
#include "operaciones_svc.h"

#ifndef SIG_PF
#define SIG_PF void(*)(int)
#endif

int main(int argc, char **argv) {
    register SVCXPRT *transp;

    pmap_unset(OPERACIONES, OPERACIONES_V1);

    transp = svcudp_create(RPC_ANYSOCK);
    if (transp == NULL) {
        fprintf(stderr, "Error al crear el servicio UDP\n");
        exit(1);
    }
    if (!svc_register(transp, OPERACIONES, OPERACIONES_V1, operaciones_1, IPPROTO_UDP)) {
        fprintf(stderr, "Error al registrar el servicio UDP\n");
        exit(1);
    }

    transp = svctcp_create(RPC_ANYSOCK, 0, 0);
    if (transp == NULL) {
        fprintf(stderr, "Error al crear el servicio TCP\n");
        exit(1);
    }
    if (!svc_register(transp, OPERACIONES, OPERACIONES_V1, operaciones_1, IPPROTO_TCP)) {
        fprintf(stderr, "Error al registrar el servicio TCP\n");
        exit(1);
    }

    svc_run();
    fprintf(stderr, "Error: svc_run ha retornado\n");
    exit(1);
}

