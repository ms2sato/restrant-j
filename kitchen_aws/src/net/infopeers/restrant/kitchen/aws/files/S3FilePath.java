package net.infopeers.restrant.kitchen.aws.files;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.infopeers.restrant.kitchen.files.FilePath;
import net.infopeers.restrant.kitchen.files.FileStorageMetadata;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

public class S3FilePath implements FilePath {

	String path;

	String backet;

	S3FileStorage storage;

	S3FilePath(S3FileStorage storage) {
		this.storage = storage;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getBacket() {
		return backet;
	}

	@Override
	public URL toURL() throws MalformedURLException {
		return new URL(storage.getUrl(backet, path));
	}

	@Override
	public void write(byte[] data, FileStorageMetadata metadata) {
		this.storage.store(this, data, metadata);
	}

	@Override
	public boolean setReadable(boolean readable) {
		this.storage.changePermission(this,
				readable ? CannedAccessControlList.PublicRead
						: CannedAccessControlList.Private);
		return true;
	}
	
	@Override
	public InputStream readAsStream(){
		return getInternalObject().getObjectContent();
	}
	
	@Override
	public FileStorageMetadata getMetadata(){
		S3Object o = getInternalObject();
		ObjectMetadata om = o.getObjectMetadata();
		
		FileStorageMetadata meta = new FileStorageMetadata();
		meta.setContentLength(om.getContentLength());
		meta.setContentType(om.getContentType());
		return meta;
	}
	
	public S3Object getInternalObject(){
		return this.storage.getInternalObject(this);
	}

	void setPath(String path) {
		this.path = path;
	}

	void setBacket(String backet) {
		this.backet = backet;
	}

}
