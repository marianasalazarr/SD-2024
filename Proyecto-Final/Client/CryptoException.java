// Definición de la clase CryptoException que extiende la clase Exception
public class CryptoException extends Exception {
// Constructor predeterminado sin argumentos
	public CryptoException() {
	}
// Constructor que toma un mensaje y una instancia de Throwable (para encadenar excepciones)
	public CryptoException(String message, Throwable throwable) {
		// Llama al constructor de la clase base (Exception) con el mensaje y la excepción original
		super(message, throwable);
	}
}
