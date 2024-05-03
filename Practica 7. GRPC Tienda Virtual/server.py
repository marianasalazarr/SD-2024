import grpc
from concurrent import futures
import shop_pb2
import shop_pb2_grpc

class ShopServicer(shop_pb2_grpc.ShopServiceServicer):
    def PurchaseItem(self, request, context):
        total_price = 10 * request.quantity
        return shop_pb2.PurchaseResponse(message="Artículo comprado exitosamente", total_price=total_price)

    def MakePayment(self, request, context):
        success = True
        return shop_pb2.PaymentResponse(message="Pago exitoso", success=success)

    def PlaceOrder(self, request, context):
        order_id = "123456"
        purchases = []
        total_price = 0
        for item in request.items:
            total_price += 10 * item.quantity
            purchases.append(shop_pb2.PurchaseResponse(message="Artículo comprado exitosamente", total_price=total_price))
        return shop_pb2.OrderResponse(order_id=order_id, purchases=purchases)

    def GetProductInfo(self, request, context):
        product_id = request.product_id
        product_info = obtain_product_info_from_database(product_id)
        return shop_pb2.ProductInfoResponse(name=product_info['name'], description=product_info['description'], price=product_info['price'])

def obtain_product_info_from_database(product_id):
    product_info = {
        "123": {"name": "Producto 1", "description": "Descripción del Producto 1", "price": 10.99},
        "456": {"name": "Producto 2", "description": "Descripción del Producto 2", "price": 20.49},
        "789": {"name": "Producto 3", "description": "Descripción del Producto 3", "price": 5.99},
    }
    return product_info.get(product_id, {"name": "Producto no encontrado", "description": "", "price": 0.0})

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    shop_pb2_grpc.add_ShopServiceServicer_to_server(ShopServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    print("Servidor iniciado. Escuchando en el puerto 50051")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()
