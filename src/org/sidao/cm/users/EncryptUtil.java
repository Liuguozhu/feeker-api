package org.sidao.cm.users;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptUtil {

	public static final String DEFAULT_AES_ARITHMETIC = "AES";
	public static final String DEFAULT_AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
	public static final int DEFAULT_AES_KEYSIZE = 128;
	public static final boolean DEFAULT_AES_USE_IV = true;

	private static Cipher createCipher(String arithmetic,
			String transformation, byte[] key, byte[] ivParam, int keySize,
			boolean supportIV, int opmod) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, arithmetic);

		// 初始加密模式
		Cipher cipher = Cipher.getInstance(transformation);

		// 生成初始向量
		IvParameterSpec iv = new IvParameterSpec(ivParam);

		// 初始算法器
		if (supportIV) {
			cipher.init(opmod, secretKeySpec, iv);
		} else {
			cipher.init(opmod, secretKeySpec);
		}

		return cipher;
	}

	public static byte[] generatKey(byte[] srcKey, String arithmetic,
			int keySize) throws NoSuchAlgorithmException {

		// 初始安全随机对象
		SecureRandom sr = new SecureRandom(srcKey);

		// 实例化密钥生成器
		KeyGenerator kgen = KeyGenerator.getInstance(arithmetic);

		// 初始密钥生成器
		kgen.init(keySize, sr);

		// 生成密钥对象
		SecretKey sk = kgen.generateKey();

		// 获取密钥数据
		byte[] key = sk.getEncoded();

		return key;
	}

	public static String generatKeyBase64(byte[] srcKey, String arithmetic,
			int keySize) throws NoSuchAlgorithmException {
		return encodeBase64(generatKey(srcKey, arithmetic, keySize));
	}

	public static byte[] generatDefaultAESKey(byte[] srcKey)
			throws NoSuchAlgorithmException {
		return generatKey(srcKey, DEFAULT_AES_ARITHMETIC, DEFAULT_AES_KEYSIZE);
	}

	public static String generatDefaultAESKeyBase64(byte[] srcKey)
			throws NoSuchAlgorithmException {
		return generatKeyBase64(srcKey, DEFAULT_AES_ARITHMETIC,
				DEFAULT_AES_KEYSIZE);
	}

	public static Cipher createCipherForEncryptByDefault(byte[] keyData,
			byte[] ivParam) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException {

		return createCipher(DEFAULT_AES_ARITHMETIC, DEFAULT_AES_TRANSFORMATION,
				keyData, ivParam, DEFAULT_AES_KEYSIZE, DEFAULT_AES_USE_IV,
				Cipher.ENCRYPT_MODE);
	}

	public static Cipher generatCipherForAESDecryptByDefault(byte[] keyData,
			byte[] ivParam) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException {

		return createCipher(DEFAULT_AES_ARITHMETIC, DEFAULT_AES_TRANSFORMATION,
				keyData, ivParam, DEFAULT_AES_KEYSIZE, DEFAULT_AES_USE_IV,
				Cipher.DECRYPT_MODE);
	}

	public static byte[] encryptAESByDefault(byte[] data, byte[] keyData,
			byte[] ivParam) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {

		Cipher cipher = createCipherForEncryptByDefault(keyData, ivParam);
		return cipher.doFinal(data);
	}

	public static byte[] decryptAESByDefault(byte[] data, byte[] keyData,
			byte[] ivParam) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = generatCipherForAESDecryptByDefault(keyData, ivParam);
		return cipher.doFinal(data);
	}

	public static String encodeBase64(byte[] data) {
		return new BASE64Encoder().encode(data);
	}

	public static byte[] decodeBase64(String str) throws IOException {
		return new BASE64Decoder().decodeBuffer(str);
	}

	public static String encryptAESByDefaultAndBase64(String data,
			String charsetName, byte[] keyData, byte[] ivParam)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {

		return encodeBase64(encryptAESByDefault(data.getBytes(charsetName),
				keyData, ivParam));
	}

	public static String encryptAESByDefaultAndBase64(String data,
			String charsetName, EncryptKey key) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {

		return encodeBase64(encryptAESByDefault(data.getBytes(charsetName), key
				.getKeyData(), key.getIvParam()));
	}

	public static String encryptAESByDefaultAndBase64ForData(byte[] data,
			String charsetName, byte[] keyData, byte[] ivParam)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {

		return encodeBase64(encryptAESByDefault(data, keyData, ivParam));
	}

	public static String encryptAESByDefaultAndBase64ForData(byte[] data,
			String charsetName, EncryptKey key) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {

		return encodeBase64(encryptAESByDefault(data, key.getKeyData(), key
				.getIvParam()));
	}

	public static String decryptAESByDefaultAndBase64(String data,
			String charsetName, byte[] keyData, byte[] ivParam)
			throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, IOException {

		return new String(decryptAESByDefault(decodeBase64(data), keyData,
				ivParam), charsetName);
	}

	public static String decryptAESByDefaultAndBase64(String data,
			String charsetName, EncryptKey key) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {

		return new String(decryptAESByDefault(decodeBase64(data), key
				.getKeyData(), key.getIvParam()), charsetName);
	}

	public static byte[] decryptAESByDefaultAndBase64ForData(String data,
			String charsetName, byte[] keyData, byte[] ivParam)
			throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, IOException {

		return decryptAESByDefault(decodeBase64(data), keyData, ivParam);
	}

	public static byte[] decryptAESByDefaultAndBase64ForData(String data,
			String charsetName, EncryptKey key) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException {

		return decryptAESByDefault(decodeBase64(data), key.getKeyData(), key
				.getIvParam());
	}
}
