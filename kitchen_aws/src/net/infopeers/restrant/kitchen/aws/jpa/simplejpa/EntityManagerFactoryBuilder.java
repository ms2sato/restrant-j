package net.infopeers.restrant.kitchen.aws.jpa.simplejpa;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;

import com.spaceprogram.simplejpa.EntityManagerFactoryImpl;

public class EntityManagerFactoryBuilder {

	private static final Logger logger = Logger
			.getLogger(EntityManagerFactoryBuilder.class.getName());

	String accessKey;
	String secretKey;

	String lobBucketName; // Bucket for blob data defined by SimpleJPA
	String unitName; // DB's prefix

	boolean readMyPropertiesOnly;

	Map<String, String> properties = new HashMap<String, String>();

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getLobBucketName() {
		return lobBucketName;
	}

	public void setLobBucketName(String lobBucketName) {
		this.lobBucketName = lobBucketName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public boolean isReadMyPropertiesOnly() {
		return readMyPropertiesOnly;
	}

	public void setReadMyPropertiesOnly(boolean readMyPropertiesOnly) {
		this.readMyPropertiesOnly = readMyPropertiesOnly;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public EntityManagerFactory build() {

		InputStream credentialsFile = getClass().getClassLoader()
				.getResourceAsStream("AwsCredentials.properties");
		if (credentialsFile != null) {
			// if not null, SimpleJPA ignore variant properties.
			logger.info("AwsCredentials.properties on class path. SimpleJPA read this one only.");

			if (readMyPropertiesOnly) {
				throw new RuntimeException(
						"AwsCredentials.properties found on class path. if your process continue, readMyPropertiesOnly option off. otherwise delete AwsCredentials.properties");
			}
		} else {
			logger.info("AwsCredentials.properties not found on class path. read properties.");

			if (accessKey == null) {
				throw new RuntimeException("accessKey not found.");
			}
			
			if (secretKey == null){
				throw new RuntimeException("secretKey not found.");
			}

			properties.put("accessKey", accessKey);
			properties.put("secretKey", secretKey);

		}

		if (lobBucketName != null) {
			properties.put("lobBucketName", lobBucketName);
		}
		
		if(unitName == null){
			throw new IllegalStateException("unitName is null");
		}

		EntityManagerFactoryImpl factory = new EntityManagerFactoryImpl(
				unitName, properties);

		return factory;
	}

}
