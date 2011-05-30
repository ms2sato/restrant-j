package net.infopeers.restrant.kitchen.files;

public class FileStorageMetadata {

	private long contentLength;
	private String contentType;
	
	public long getContentLength() {
		return contentLength;
	}
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
