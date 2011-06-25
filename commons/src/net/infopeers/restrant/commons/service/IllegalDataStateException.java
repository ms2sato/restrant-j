package net.infopeers.restrant.commons.service;

/**
 * クライアントから送信されてきたデータの状態が古い等
 * サーバのデータとクライアントのデータの不一致の場合。
 * クライアントが更新すれば解消が見込まれる。
 * @author ms2
 *
 */
public class IllegalDataStateException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6531054739757073266L;

	public IllegalDataStateException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IllegalDataStateException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IllegalDataStateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IllegalDataStateException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
