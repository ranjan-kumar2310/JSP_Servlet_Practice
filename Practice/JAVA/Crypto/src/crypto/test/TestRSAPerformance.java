package crypto.test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import crypto.test.helper.PerformanceTestHelper;
import crypto.utils.RSAUtils;

public class TestRSAPerformance {
	private static char[] _publicKeyChars;
	private static char[] _privateKeyChars;
	private static String _src = "test";
	private static int _runs = 100;
	
	private static List<String> _encryptedPub = new ArrayList<String>();
	private static List<String> _encryptedPri = new ArrayList<String>();
	
	private static int[] _keySizes = new int[] {1024, 2048, 4096, 8192};
	
	public static void main (String[] args) throws Exception {
		// test over each key size
		// generate 8192 bits key will take quite a while
		for (int keySize : _keySizes) {
			// prepare key and encrypted data
			KeyPair keyPair = RSAUtils.generateKey(keySize);
			_publicKeyChars = RSAUtils.getBase64PublicKeyChars(keyPair);
			_privateKeyChars = RSAUtils.getBase64PrivateKeyChars(keyPair);
			prepareEncryptedPub();
			prepareEncryptedPri();
			
			testCreateCipherEachTimeEncPub();
			testReuseCipherEncPub();
			testCreateCipherEachTimeEncPri();
			testReuseCipherEncPri();
			
			testCreateCipherEachTimeDecPub();
			testReuseCipherDecPub();
			testCreateCipherEachTimeDecPri();
			testReuseCipherDecPri();
		}
	}

	/**
	 * Encrypt by Public Key, create Cipher each time
	 * 
	 * @throws Exception
	 */
	private static void testCreateCipherEachTimeEncPub() throws Exception {
		new PerformanceTestHelper("testCreateCipherEachTimeEncPub") {
			int i = 1;
			PublicKey publicKey = RSAUtils.getPublicKeyFromBase64Chars(_publicKeyChars);
			@Override
			public void run() throws Exception {
				Cipher encCipher = RSAUtils.getCipher(Cipher.ENCRYPT_MODE, publicKey);
				String encrypted = RSAUtils.encrypt(encCipher, _src+i);
				// print 5th char
				System.out.print(encrypted.charAt(5));
				i++;
			}
			
		}.setRuns(_runs)
		.setShouldWarmUp(true)
		.doTest()
		.showResult();
	}
	
	/**
	 * Encrypt by Public Key, reuse Cipher
	 * 
	 * @throws Exception
	 */
	private static void testReuseCipherEncPub() throws Exception {
		new PerformanceTestHelper("testReuseCipherEncPub") {
			int i = 1;
			PublicKey publicKey = RSAUtils.getPublicKeyFromBase64Chars(_publicKeyChars);
			Cipher encCipher = RSAUtils.getCipher(Cipher.ENCRYPT_MODE, publicKey);
			@Override
			public void run() throws Exception {
				String encrypted = RSAUtils.encrypt(encCipher, _src+i);
				// print 5th char
				System.out.print(encrypted.charAt(5));
				i++;
			}
			
		}.setRuns(_runs)
		.setShouldWarmUp(true)
		.doTest()
		.showResult();
	}
	
	/**
	 * Encrypt by Private Key, create Cipher each time
	 * 
	 * @throws Exception
	 */
	private static void testCreateCipherEachTimeEncPri() throws Exception {
		new PerformanceTestHelper("testCreateCipherEachTimeEncPri") {
			int i = 1;
			PrivateKey privateKey = RSAUtils
					.getPrivateKeyFromBase64Chars(_privateKeyChars);
			@Override
			public void run() throws Exception {
				Cipher encCipher = RSAUtils.getCipher(Cipher.ENCRYPT_MODE, privateKey);
				String encrypted = RSAUtils.encrypt(encCipher, _src+i);
				// print 5th char
				System.out.print(encrypted.charAt(5));
				i++;
			}
			
		}.setRuns(_runs)
		.setShouldWarmUp(true)
		.doTest()
		.showResult();
	}
	
	/**
	 * Encrypt by Private Key, reuse Cipher
	 * 
	 * @throws Exception
	 */
	private static void testReuseCipherEncPri() throws Exception {
		new PerformanceTestHelper("testReuseCipherEncPri") {
			int i = 1;
			PrivateKey privateKey = RSAUtils
					.getPrivateKeyFromBase64Chars(_privateKeyChars);
			Cipher encCipher = RSAUtils.getCipher(Cipher.ENCRYPT_MODE, privateKey);
			@Override
			public void run() throws Exception {
				String encrypted = RSAUtils.encrypt(encCipher, _src+i);
				// print 5th char
				System.out.print(encrypted.charAt(5));
				i++;
			}
			
		}.setRuns(_runs)
		.setShouldWarmUp(true)
		.doTest()
		.showResult();
	}
	
	/**
	 * Decrypt by Public Key, create Cipher each time
	 * 
	 * @throws Exception
	 */
	private static void testCreateCipherEachTimeDecPub() throws Exception {
		new PerformanceTestHelper("testCreateCipherEachTimeDecPub") {
			PublicKey publicKey = RSAUtils.getPublicKeyFromBase64Chars(_publicKeyChars);

			int cnt = 0;
			@Override
			public void run() throws Exception {
				String encrypted = _encryptedPri.get(cnt);
				Cipher decCipher = RSAUtils.getCipher(Cipher.DECRYPT_MODE, publicKey);
				String decrypted = RSAUtils.decrypt(decCipher, encrypted);
				
				System.out.print(decrypted.substring(4)+", ");
				cnt++;
			}
		}.setRuns(_runs)
		.setShouldWarmUp(true)
		.doTest()
		.showResult();
	}
	
	/**
	 * Decrypt by Public Key, reuse Cipher
	 * 
	 * @throws Exception
	 */
	private static void testReuseCipherDecPub() throws Exception {
		new PerformanceTestHelper("testReuseCipherDecPub") {
			PublicKey publicKey = RSAUtils.getPublicKeyFromBase64Chars(_publicKeyChars);

			int cnt = 0;
			Cipher decCipher = RSAUtils.getCipher(Cipher.DECRYPT_MODE, publicKey);
			@Override
			public void run() throws Exception {
				String encrypted = _encryptedPri.get(cnt);
				String decrypted = RSAUtils.decrypt(decCipher, encrypted);
				
				System.out.print(decrypted.substring(4)+", ");
				cnt++;
			}
		}.setRuns(_runs)
		.setShouldWarmUp(true)
		.doTest()
		.showResult();
	}
	
	/**
	 * Decrypt by Private Key, create Cipher each time
	 * 
	 * @throws Exception
	 */
	private static void testCreateCipherEachTimeDecPri() throws Exception {
		new PerformanceTestHelper("testCreateCipherEachTimeDecPri") {
			PrivateKey privateKey = RSAUtils
					.getPrivateKeyFromBase64Chars(_privateKeyChars);

			int cnt = 0;
			@Override
			public void run() throws Exception {
				String encrypted = _encryptedPub.get(cnt);
				Cipher decCipher = RSAUtils.getCipher(Cipher.DECRYPT_MODE, privateKey);
				String decrypted = RSAUtils.decrypt(decCipher, encrypted);
				
				System.out.print(decrypted.substring(4)+", ");
				cnt++;
			}
		}.setRuns(_runs)
		.setShouldWarmUp(true)
		.doTest()
		.showResult();
	}
	
	/**
	 * Decrypt by Private Key, reuse Cipher
	 * 
	 * @throws Exception
	 */
	private static void testReuseCipherDecPri() throws Exception {
		new PerformanceTestHelper("testReuseCipherDecPri") {
			PrivateKey privateKey = RSAUtils
					.getPrivateKeyFromBase64Chars(_privateKeyChars);

			int cnt = 0;
			Cipher decCipher = RSAUtils.getCipher(Cipher.DECRYPT_MODE, privateKey);
			@Override
			public void run() throws Exception {
				String encrypted = _encryptedPub.get(cnt);
				String decrypted = RSAUtils.decrypt(decCipher, encrypted);
				
				System.out.print(decrypted.substring(4)+", ");
				cnt++;
			}
		}.setRuns(_runs)
		.setShouldWarmUp(true)
		.doTest()
		.showResult();
	}
	
	private static void prepareEncryptedPub () throws Exception {
		PublicKey publicKey = RSAUtils.getPublicKeyFromBase64Chars(_publicKeyChars);
		Cipher encCipher = RSAUtils.getCipher(Cipher.ENCRYPT_MODE, publicKey);
		_encryptedPub.clear();
		// add more for warmup
		for (int i = 0; i < _runs+3; i++) {
			String encrypted = RSAUtils.encrypt(encCipher, _src+i);
			_encryptedPub.add(encrypted);
		}
	}
	private static void prepareEncryptedPri () throws Exception {
		PrivateKey privateKey = RSAUtils
				.getPrivateKeyFromBase64Chars(_privateKeyChars);
		Cipher encCipher = RSAUtils.getCipher(Cipher.ENCRYPT_MODE, privateKey);
		_encryptedPri.clear();
		// add more for warmup
		for (int i = 0; i < _runs+3; i++) {
			String encrypted = RSAUtils.encrypt(encCipher, _src+i);
			_encryptedPri.add(encrypted);
		}
	}
}
