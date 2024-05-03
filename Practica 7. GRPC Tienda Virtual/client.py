import grpc
import shop_pb2
import shop_pb2_grpc

def run():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = shop_pb2_grpc.ShopServiceStub(channel)

	# Ejemplo de llamada al método PurchaseItem
        purchase_request = shop_pb2.PurchaseRequest(product_id="123", quantity=2)
        purchase_response = stub.PurchaseItem(purchase_request)
        print("Respuesta de Compra de Artículo:", purchase_response.message)
        print("Total Price:", purchase_response.total_price)
        
        # Ejemplo de llamada al método MakePayment
        payment_request = shop_pb2.PaymentRequest(payment_method="credit_card", amount=20.0)
        payment_response = stub.MakePayment(payment_request)
        print("Respuesta de Pago:", payment_response.message)
        print("Éxito del Pago:", payment_response.success)

        # Ejemplo de llamada al método PlaceOrder
        order_request = shop_pb2.OrderRequest()
        order_request.items.extend([
            shop_pb2.PurchaseRequest(product_id="456", quantity=3),
            shop_pb2.PurchaseRequest(product_id="789", quantity=1)
        ])
        order_response = stub.PlaceOrder(order_request)
        print("Respuesta de Realización de Pedido:", order_response.order_id)
        for purchase in order_response.purchases:
            print("Artículo:", purchase.message)
            print("Precio Total:", purchase.total_price)


        # Ejemplo de llamada al método GetProductInfo
        product_id = "456"
        product_info_request = shop_pb2.ProductInfoRequest(product_id=product_id)
        product_info_response = stub.GetProductInfo(product_info_request)
        print("Información del Producto:")
        print("Nombre:", product_info_response.name)
        print("Descripción:", product_info_response.description)
        print("Precio:", product_info_response.price)

if __name__ == '__main__':
    run()
