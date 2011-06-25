package net.infopeers.restrant.commons.service;

/**
 * 不適切な外部からのアクセス。
 * ありえない入力を外から作られている可能性を示す。
 * @author ms2
 */
public class IllegalOperationException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2752436818659274788L;

	public IllegalOperationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IllegalOperationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IllegalOperationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IllegalOperationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
