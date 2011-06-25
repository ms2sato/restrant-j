package net.infopeers.restrant.commons.http;

import java.io.IOException;

public class HttpException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6239653701572866585L;

	private int statusCode;
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public int getStatusCode() {
		return statusCode;
	}	
	
	public HttpException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public HttpException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public HttpException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public HttpException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
