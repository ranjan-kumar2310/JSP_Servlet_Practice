package crypto.test;

import java.util.Arrays;

import crypto.impl.AESImpl;

public class TestAESImpl {
	private static String _src = "test 臺灣 No.1";
	public static void main (String[] args) throws Exception {
		testEncryptDecryptAndGetKey();
		testEncryptDecryptWithKey();
		testEncryptDecryptWithKeyIv();
		testEncryptDecryptWithKeyIvSalt();
	}

	private static void testEncryptDecryptAndGetKey() throws Exception {
		// get AESImpl instance
		AESImpl aesImpl = AESImpl.getInstance();
		
		// get key, you will want to store it somewhere for later use
		char[] key = aesImpl.getKey();
		// dupekey for another instance
		char[] dupekey = Arrays.copyOf(key, key.length);
		
		// test Encrypt Decrypt
		String encrypted = aesImpl.encrypt(_src);
		String decrypted = aesImpl.decrypt(encrypted);
		// can reuse it
		String encrypted2 = aesImpl.encrypt("another data");
		String decrypted2 = aesImpl.decrypt(encrypted2);
		
		// output result
		System.out.println("### testEncryptDecryptAndGetKey");
		System.out.println("\t### _src = "+_src);
		System.out.println("\t### key = "+new String(key));
		System.out.println("\t### encrypted = "+encrypted);
		System.out.println("\t### decrypted = "+decrypted);
		System.out.println("\t### encrypted2 = "+encrypted2);
		System.out.println("\t### decrypted2 = "+decrypted2);
		
		/* you can also get the auto generated IV and Salt
		 * but do not need to store them since they can be auto
		 * generated by key next time
		 * 
		 * of course, store them and set them directly next time
		 * will increase performance (slightly)
		 */
		System.out.println("\t### generated IV = "+new String(aesImpl.getIv()));
		System.out.println("\t### generated Salt = "+new String(aesImpl.getSalt()));
		// clear data in AESImpl
		aesImpl.clear();
		// the key is cleared too, told you? store it.
		System.out.println("\t### key = "+new String(key));
		
		// also try to get another instance with key to decrypt
		AESImpl anotherAESImpl = AESImpl.getInstance(dupekey);
		// do not need to get its key this time
		// so can clear it immediately
		anotherAESImpl.clear();
		// try decrypt
		System.out.println("\t### decrypted by another instance = "
				+ anotherAESImpl.decrypt(encrypted));
	}

	private static void testEncryptDecryptWithKey() throws Exception {
		// get AESImpl instance with key
		AESImpl aesImpl = AESImpl.getInstance("test key".toCharArray());
		// always remember to clear it
		aesImpl.clear();

		// test Encrypt Decrypt
		String encrypted = aesImpl.encrypt(_src);
		String decrypted = aesImpl.decrypt(encrypted);
		
		// output result
		System.out.println("### testEncryptDecryptWithKey");
		System.out.println("\t### _src = "+_src);
		System.out.println("\t### encrypted = "+encrypted);
		System.out.println("\t### decrypted = "+decrypted);
	}

	private static void testEncryptDecryptWithKeyIv() throws Exception {
		// get AESImpl instance with key and IV
		AESImpl aesImpl = AESImpl
				.getInstance("test key".toCharArray(), "test IV".toCharArray());
		// always remember to clear it
		aesImpl.clear();
				
		// test Encrypt Decrypt
		String encrypted = aesImpl.encrypt(_src);
		String decrypted = aesImpl.decrypt(encrypted);
		
		// output result
		System.out.println("### testEncryptDecryptWithKeyIv");
		System.out.println("\t### _src = "+_src);
		System.out.println("\t### encrypted = "+encrypted);
		System.out.println("\t### decrypted = "+decrypted);
	}

	private static void testEncryptDecryptWithKeyIvSalt() throws Exception {
		// get AESImpl instance with key, IV and Salt
		AESImpl aesImpl = AESImpl
				.getInstance("test key".toCharArray(),
						"test IV".toCharArray(), "test Salt".toCharArray());
		// always remember to clear it
		aesImpl.clear();
		
		// test Encrypt Decrypt
		String encrypted = aesImpl.encrypt(_src);
		String decrypted = aesImpl.decrypt(encrypted);
		
		// output result
		System.out.println("### testEncryptDecryptWithKeyIvSalt");
		System.out.println("\t### _src = "+_src);
		System.out.println("\t### encrypted = "+encrypted);
		System.out.println("\t### decrypted = "+decrypted);
	}
}
