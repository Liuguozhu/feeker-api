package org.sidao.cm.users;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class EncryptKeyUtil {

	private static final Log LOGGER = LogFactory.getLog(EncryptKeyUtil.class);

	@SuppressWarnings("unused")
	private static EncryptKey urlEncryptKey;

	private static EncryptKey pwEncryptKey;
	
	private static Configuration conf=null;

	public static EncryptKey getPWEncryptKey() throws Exception {
		
		
		if(conf==null){
			conf=new PropertiesConfiguration("password.properties");
		}
		if (pwEncryptKey == null) {
			pwEncryptKey = new EncryptKey("password.properties");
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Init urlEncryptKey by file in classpath["
						+"password.properties"
						+ "]");
			}
		}
		return pwEncryptKey;
	}
}
