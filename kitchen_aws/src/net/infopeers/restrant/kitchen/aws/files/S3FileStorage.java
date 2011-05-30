package net.infopeers.restrant.kitchen.aws.files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.infopeers.restrant.kitchen.files.FilePath;
import net.infopeers.restrant.kitchen.files.FileStorage;
import net.infopeers.restrant.kitchen.files.FileStorageMetadata;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3FileStorage implements FileStorage {

	AmazonS3 s3;

	public S3FileStorage(AmazonS3 s3) {
		this.s3 = s3;
	}
	
	/* (non-Javadoc)
	 * @see net.infopeers.restrant.kitchen.files.s3.FileStorage#getPath(java.lang.String, java.lang.String)
	 */
	@Override
	public FilePath getPath(String backet, String path){
		S3FilePath file =  new S3FilePath(this);
		file.setBacket(backet);
		file.setPath(path);
		return file;
	}

	@Override
	public FilePath getPath(String path){
		throw new UnsupportedOperationException("getPath(String path)");
	}
	
	public String getUrl(String backetName, String storagePath) {
		return "http://s3.amazonaws.com/" + backetName + "/" + storagePath;
	}

	public String store(FilePath path, byte[] data, FileStorageMetadata metadata) {

		String backetName = path.getBacket();
		String storagePath = path.getPath();
		
		return store(backetName, storagePath, data, metadata);
	}

	public String store(String backetName, String storagePath, byte[] data,
			FileStorageMetadata metadata) {
		if (backetName == null) {
			throw new IllegalArgumentException("path.backet is null");
		}
		if (storagePath == null) {
			throw new IllegalArgumentException("path.path is null");
		}

		ObjectMetadata omd = new ObjectMetadata();
		omd.setContentType(metadata.getContentType());
		omd.setContentLength(data.length);
		ByteArrayInputStream is = new ByteArrayInputStream(data);

		PutObjectRequest request = new PutObjectRequest(backetName,
				storagePath, is, omd);

		s3.putObject(request);

		return getUrl(backetName, storagePath);

		// If we have an ACL set access permissions for the the data on S3
		// if (acl!=null) {
		// s3client.setObjectAcl(obj.getBucketName(), obj.getStoragePath(),
		// acl);
		// }
	}

	public void changePermission(FilePath path, CannedAccessControlList access) {
		s3.setObjectAcl(path.getBacket(), path.getPath(),
				access);
	}

	public InputStream getAsStream(FilePath path) {
		return s3.getObject(path.getBacket(), path.getPath())
				.getObjectContent();
	}

	public S3Object getInternalObject(FilePath path) {
		return s3.getObject(path.getBacket(), path.getPath());
	}

}
