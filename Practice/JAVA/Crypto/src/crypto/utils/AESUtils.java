package crypto.utils;

import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import crypto.CryptoConstants;

/**
 * AES Generate keys, get Cipher, Encrypt/Decrypt
 * 
 * @author benbai123
 *
 */
public class AESUtils {
	/**
	 * Generate 256-bits key
	 * @return
	 * @throws Exception
	 */
	public static char[] generateKey () throws Exception {
		return generateKey(256);
	}
	/**
	 * Get AES Key
	 * 
	 * @param size int, Key length, should be 128, 192, 256 if
	 * you do not use extra key derivation function or algorithms that
	 * allow different key length
	 * 
	 * @return
	 * @throws Exception
	 */
	public static char[] generateKey (int size) throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance(CryptoConstants.AES.v);
		keyGen.init(size);
		SecretKey secretKey = keyGen.generateKey();
		byte[] encodedKey = secretKey.getEncoded();
		char[] base64Chars = CryptoUtils.bytesToBase64Chars(encodedKey);
		// clear local array
		Arrays.fill(encodedKey, (byte) 0);
		return base64Chars;
	}
	
	/**
	 * Encrypt with provided Cipher
	 * 
	 * @param encCipher
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encrypt (Cipher encCipher, String data) throws Exception {
		return CryptoUtils.encrypt(encCipher, data);
	}
	/**
	 * Decrypt with provided Cipher
	 * 
	 * @param decCipher
	 * @param encryptedData
	 * @return
	 * @throws Exception
	 */
	public static String decrypt (Cipher decCipher, String encryptedData) throws Exception {
		return CryptoUtils.decrypt(decCipher, encryptedData);
	}
	/**
	 * Get Cipher with specified Key
	 * 
	 * @param mode Cipher.ENCRYPT_MODE (1) or Cipher.DECRYPT_MODE (2)
	 * @param key AES Key, should be generated by AESUtils.generateKey
	 * with size 128/192/256
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Cipher getCipher (int mode, char[] key) throws Exception {
		byte[] keyBytes = CryptoUtils.base64CharsToBytes(key);
		byte[] ivBytes = CryptoUtils.md5(keyBytes);
		SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, CryptoConstants.AES.v);
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		
		Cipher cipher = Cipher.getInstance(CryptoConstants.AES_CBC_PKCS5PADDING.v);
		cipher.init(mode, skeySpec, iv);
		// clear local array
		Arrays.fill(keyBytes, (byte) 0);
		Arrays.fill(ivBytes, (byte) 0);
		return cipher;
	}
	/**
	 * Get Cipher for Encrypt/Decrypt with Key, IV and Salt specified
	 * 
	 * Put PBE Key generation and Cipher creation together since
	 * the concept here is "use key and salt to get Cipher"
	 * so...
	 * 
	 * The iteration count (65536) affect the processing time,
	 * adjust it to smaller value (e.g., 128) if needed.
	 * 
	 * @param mode Cipher.ENCRYPT_MODE (1) or Cipher.DECRYPT_MODE (2)
	 * @param key AES Key, can have any length here
	 * @param ivBytes bytes for IV (initial vector), should be 16 bytes long
	 * @param saltBytes bytes for salt, cannot be empty
	 * @return
	 * @throws Exception
	 */
	public static Cipher getCipher(int mode, char[] key, byte[] ivBytes,
			byte[] saltBytes) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(CryptoConstants.PBKDF2WithHmacSHA256.v);
		KeySpec spec = new PBEKeySpec(key, saltBytes, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKeySpec skeySpec = new SecretKeySpec(tmp.getEncoded(), CryptoConstants.AES.v);

		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

		Cipher cipher = Cipher.getInstance(CryptoConstants.AES_CBC_PKCS5PADDING.v);
		cipher.init(mode, skeySpec, ivSpec);
		return cipher;
	}
	/**
	 * Convenient method for get Cipher with Key, IV and Salt
	 * 
	 * @param mode Cipher.ENCRYPT_MODE (1) or Cipher.DECRYPT_MODE (2)
	 * @param key any length of chars
	 * @param base64EncodedIvChars chars of Base64 Encoded 16 bytes
	 * @param base64EncodedSaltChars any length of chars
	 * @return
	 * @throws Exception
	 */
	public static Cipher getCipher(int mode, char[] key, char[] base64EncodedIvChars,
			char[] base64EncodedSaltChars) throws Exception {
		byte[] ivBytes = CryptoUtils.base64CharsToBytes(base64EncodedIvChars);
		byte[] saltBytes = CryptoUtils.base64CharsToBytes(base64EncodedSaltChars);
		Cipher cipher = getCipher(mode, key, ivBytes, saltBytes);
		// clear local array
		Arrays.fill(ivBytes, (byte) 0);
		Arrays.fill(saltBytes, (byte) 0);
		return cipher;
	}
	/**
	 * Get Random IV chars
	 * 
	 * @return char[], the base64 encoded random 16 bytes
	 * @throws Exception
	 */
	public static char[] getRandomIv () throws Exception {
		return CryptoUtils.getRandomBase64Chars(16);
	}
}
