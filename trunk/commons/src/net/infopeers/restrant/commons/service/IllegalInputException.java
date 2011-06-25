package net.infopeers.restrant.commons.service;

/**
 * 不正な入力の例外。
 * データ入力項目での入力ミス。
 * @author ms2
 */
public class IllegalInputException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5022682365930913225L;

	public IllegalInputException() {
		// TODO Auto-generated constructor stub
	}

	public IllegalInputException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IllegalInputException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IllegalInputException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
