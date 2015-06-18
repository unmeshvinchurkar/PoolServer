package com.pool;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class AdvancedEncryptionStandard {
	private String encryptionKey;

	public AdvancedEncryptionStandard(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public String encrypt(String plainText) throws Exception {
		Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

		return new String(Base64.encodeBase64(encryptedBytes));
	}

	public String decrypt(String encrypted) throws Exception {
		Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
		byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(encrypted
				.getBytes()));

		return new String(plainBytes);
	}

	private Cipher getCipher(int cipherMode) throws Exception {
		String encryptionAlgorithm = "AES";
		SecretKeySpec keySpecification = new SecretKeySpec(
				encryptionKey.getBytes("UTF-8"), encryptionAlgorithm);
		Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
		cipher.init(cipherMode, keySpecification);

		return cipher;
	}

	public static void main(String[] arguments) throws Exception {
		String encryptionKey = "MZygpewJsCpRrfOr";
		String plainText = "Hello world!";
		AdvancedEncryptionStandard advancedEncryptionStandard = new AdvancedEncryptionStandard(
				encryptionKey);
		String cipherText = advancedEncryptionStandard.encrypt(plainText);
		String decryptedCipherText = advancedEncryptionStandard
				.decrypt(cipherText);

		System.out.println(plainText);
		System.out.println(cipherText);
		System.out.println(decryptedCipherText);
	}
}