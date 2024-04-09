/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _OPERACIONES_SVC_H_RPCGEN
#define _OPERACIONES_SVC_H_RPCGEN

#include <rpc/rpc.h>
#include "operaciones.h"

#ifdef __cplusplus
extern "C" {
#endif

#define OPERACIONES 0x20000001
#define OPERACIONES_V1 1

#define suma 1
extern int * suma_1_svc(operandos *, struct svc_req *);

#define resta 2
extern int * resta_1_svc(operandos *, struct svc_req *);

#define multiplicacion 3
extern int * multiplicacion_1_svc(operandos *, struct svc_req *);

#define division 4
extern int * division_1_svc(operandos *, struct svc_req *);

extern int operaciones_1_freeresult(SVCXPRT *, xdrproc_t, caddr_t);

extern bool_t xdr_operandos(XDR *, operandos *);

#ifdef __cplusplus
}
#endif

#endif /* !_OPERACIONES_SVC_H_RPCGEN */

