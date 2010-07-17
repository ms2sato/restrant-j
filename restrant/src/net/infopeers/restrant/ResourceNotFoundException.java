package net.infopeers.restrant;

/**
 * リソースの不在を示す例外。フレームワークでステータス404として出力される。
 * @author ms2
 */
public class ResourceNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3116223552848314762L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ResourceNotFoundException(String arg0) {
		super(arg0);
	}

	public ResourceNotFoundException(Throwable arg0) {
		super(arg0);
	}

}
