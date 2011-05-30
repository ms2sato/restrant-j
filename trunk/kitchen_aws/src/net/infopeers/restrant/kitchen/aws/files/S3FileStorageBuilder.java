package net.infopeers.restrant.kitchen.aws.files;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3FileStorageBuilder {

	private static final Logger logger = Logger
			.getLogger(S3FileStorageBuilder.class.getName());

	String accessKey;
	String secretKey;

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
	
	public S3FileStorage create() throws IOException {

		if (accessKey == null || secretKey == null) {
			
			// if not AWSWebConsole propertyes found, read from class path.
			logger.info("AWS_ACCESS_KEY_ID or AWS_SECRET_KEY is null. not found AWS key info. read default key info from classpath.");
			Properties awsProps = new Properties();
			awsProps.load(this.getClass().getResourceAsStream(
					"/AwsCredentials.properties"));
			accessKey = awsProps.getProperty("accessKey");
			secretKey = awsProps.getProperty("secretKey");
		}

		logger.info("AccessKey:" + accessKey);
		logger.info("SecretKey:" + secretKey);

		AWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
		return new S3FileStorage(new AmazonS3Client(creds));
	}
}
