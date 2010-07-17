package net.infopeers.restrant.engine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 処理実行クラス
 * 
 * @author ms2
 *
 */
public interface Invoker {

	/**
	 * 実行する
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws Exception
	 */
	void invoke(HttpServletRequest req, HttpServletResponse resp) throws Exception;
	
}
