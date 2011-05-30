package net.infopeers.restrant.kitchen.aws.util;

import java.util.Properties;
import java.util.logging.Logger;

import net.infopeers.restrant.kitchen.aws.files.S3FileStorageBuilder;
import net.infopeers.restrant.kitchen.aws.jpa.simplejpa.EntityManagerFactoryBuilder;

/**
 * Initialize utility. get accessKey and secretKey from WebConsole.
 * @author ms2
 *
 */
public class AwsInitializer {

	private static final Logger logger = Logger.getLogger(AwsInitializer.class
			.getName());

	String accessKey;
	String secretKey;

	public boolean hasConsoleConfig(){
		return accessKey != null && secretKey != null;
	}
	
	public void initialize() {
		// AWSWebConsole can define System.Properties
		Properties props = System.getProperties();
		accessKey = props.getProperty("AWS_ACCESS_KEY_ID");
		secretKey = props.getProperty("AWS_SECRET_KEY");
		
		if(accessKey != null && accessKey.length() == 0){
			accessKey = null;
		}
		
		if(secretKey != null && secretKey.length() == 0){
			secretKey = null;
		}
		
		logger.info("accessKey:" + accessKey);
		logger.info("secretKey:" + secretKey);
	}
	
	public S3FileStorageBuilder createFileStorageBuilder(){
		S3FileStorageBuilder b = new S3FileStorageBuilder();
		b.setAccessKey(accessKey);
		b.setSecretKey(secretKey);
		return b;
	}
	
	public EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(){
		EntityManagerFactoryBuilder b = new EntityManagerFactoryBuilder();
		b.setAccessKey(accessKey);
		b.setSecretKey(secretKey);
		return b;
	}
}
