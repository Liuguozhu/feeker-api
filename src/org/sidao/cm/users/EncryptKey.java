package org.sidao.cm.users;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

public class EncryptKey {

	private static final Log LOGGER = LogFactory.getLog(EncryptKey.class);

	private byte[] keyData;

	private byte[] ivParam;

	public EncryptKey() {
		super();
	}

	/**
	 * 根据指定路径下的key文件初始化加解密key对象EncryptKey
	 * @param encryptKeyDataFileClassPath key文件路径及key文件名
	 */
	public EncryptKey(String encryptKeyDataFileClassPath) {
		SAXReader saxBaseReader = new SAXReader();
		Document document = null;
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(
					encryptKeyDataFileClassPath);
			document = saxBaseReader.read(is);
			this.keyData = EncryptUtil.decodeBase64(document.getRootElement()
					.element("KeyData").getText());
			this.ivParam = EncryptUtil.decodeBase64(document.getRootElement()
					.element("IvParam").getText());
		} catch (Exception e) {
			LOGGER.error("Read encrpt key and iv error from classpath:["
					+ encryptKeyDataFileClassPath + "]!", e);
			throw new RuntimeException(
					"Read encrpt key and iv error from classpath:["
							+ encryptKeyDataFileClassPath + "]!", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.warn(
							"Close InputStream fault on Read encrpt key and iv error from classpath:["
									+ encryptKeyDataFileClassPath + "]!", e);
				}
			}
		}
	}

	/**
	 * 设置加解密key对象密钥数据
	 * @param keyData 密钥数据
	 */
	public void setKeyData(byte[] keyData) {
		this.keyData = keyData;
	}

	/**
	 * 设置加解密key对象IV偏移参数数据
	 * @param ivParam IV偏移参数数据
	 */
	public void setIvParam(byte[] ivParam) {
		this.ivParam = ivParam;
	}

	/**
	 * 获取加解密key对象密钥数据
	 * @return 密钥数据byte数组
	 */
	public byte[] getKeyData() {
		return keyData;
	}

	/**
	 * 获取加解密key对象IV偏移参数数据
	 * @return IV偏移参数数据byte数组
	 */
	public byte[] getIvParam() {
		return ivParam;
	}

}
