// Clase utilitaria para operaciones de cifrado y descifrado
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class CryptoUtils {
	// Algoritmo de cifrado utilizado (AES)
	private static final String ALGORITHM = "AES";
	// Transformación específica del algoritmo (AES)
	private static final String TRANSFORMATION = "AES";
	// Método para cifrar datos
	public synchronized static byte[] encrypt(String key, byte[] inputBytes)
			throws CryptoException {
		return doCrypto(Cipher.ENCRYPT_MODE, key, inputBytes);
	}
// Método para descifrar datos
	public synchronized static byte[] decrypt(String key, byte[] inputBytes)
			throws CryptoException {
		return doCrypto(Cipher.DECRYPT_MODE, key, inputBytes);
	}
// Método interno para realizar operaciones de cifrado y descifrado
	private synchronized static byte[] doCrypto(int cipherMode, String key, byte[] inputBytes) throws CryptoException {
		try {
			// Convierte la clave proporcionada a una clave secreta
			Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			// Obtiene una instancia del cifrador utilizando la transformación y la clave
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);
			// Realiza la operación de cifrado o descifrado
			byte[] outputBytes = cipher.doFinal(inputBytes);
			return outputBytes;
			
		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException  ex) {
					// Captura las posibles excepciones durante la operación de cifrado o descifrado y las envuelve en CryptoException
			throw new CryptoException("Error encrypting/decrypting file", ex);
		}
	}
}
