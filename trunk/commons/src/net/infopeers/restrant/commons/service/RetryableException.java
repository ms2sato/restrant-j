package net.infopeers.restrant.commons.service;

/**
 * �đ��M����Ώ������\�ɂȂ�ƍl�������O
 * @author ms2
 *
 */
public class RetryableException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 394558848756997769L;


	public RetryableException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RetryableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RetryableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RetryableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
