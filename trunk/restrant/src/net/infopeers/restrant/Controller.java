package net.infopeers.restrant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * コントローラー
 * 
 * @author ms2
 *
 */
public interface Controller {

	/**
	 * @param request
	 */
	void setRequest(HttpServletRequest request);

	/**
	 * @param response
	 */
	void setResponse(HttpServletResponse response);

	/**
	 * @param params
	 */
	void setParams(Params params);
	
	/**
	 * 処理が呼ばれる前にコールされる
	 * @param method メソッド名
	 * @return 通常のメソッドコールを続けるなら真。さもなくば偽。
	 */
	boolean beforeInvoke(String method) throws Exception;
	
	/**
	 * 処理が呼ばれた後にコールされる
	 * @param method メソッド名
	 */
	void afterInvoke(String method) throws Exception;

	/**
	 * サーブレットを設定する
	 * @param servlet コントローラサーブレット
	 */
	void setServlet(ControllerServlet servlet);
}
