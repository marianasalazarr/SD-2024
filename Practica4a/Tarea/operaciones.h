/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _OPERACIONES_H_RPCGEN
#define _OPERACIONES_H_RPCGEN

#include <rpc/rpc.h>


#ifdef __cplusplus
extern "C" {
#endif


struct operandos {
	int arg1;
	int arg2;
};
typedef struct operandos operandos;

#define OPERACIONES 1
#define OPERACIONES_V1 1

#if defined(__STDC__) || defined(__cplusplus)
#define suma 1
extern  int * suma_1(struct operandos *, CLIENT *);
extern  int * suma_1_svc(struct operandos *, struct svc_req *);
#define resta 2
extern  int * resta_1(struct operandos *, CLIENT *);
extern  int * resta_1_svc(struct operandos *, struct svc_req *);
#define multiplicacion 3
extern  int * multiplicacion_1(struct operandos *, CLIENT *);
extern  int * multiplicacion_1_svc(struct operandos *, struct svc_req *);
#define division 4
extern  int * division_1(struct operandos *, CLIENT *);
extern  int * division_1_svc(struct operandos *, struct svc_req *);
extern int operaciones_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define suma 1
extern  int * suma_1();
extern  int * suma_1_svc();
#define resta 2
extern  int * resta_1();
extern  int * resta_1_svc();
#define multiplicacion 3
extern  int * multiplicacion_1();
extern  int * multiplicacion_1_svc();
#define division 4
extern  int * division_1();
extern  int * division_1_svc();
extern int operaciones_1_freeresult ();
#endif /* K&R C */

/* the xdr functions */

#if defined(__STDC__) || defined(__cplusplus)
extern  bool_t xdr_operandos (XDR *, operandos*);

#else /* K&R C */
extern bool_t xdr_operandos ();

#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_OPERACIONES_H_RPCGEN */
