package net.infopeers.restrant.commons.http.response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import net.infopeers.restrant.commons.http.HttpUtils;

public abstract class JsonResponseHelper {

	private static final Logger log = Logger.getLogger(JsonResponseHelper.class
			.getName());

	public static final String STATUS = "status"; //Status
	public static final String STATUSCODE = "statusCode"; // HTTPステータスコード
	
	public static final String MESSAGE = "message";
	public static final String DETAIL = "detail"; // エラーの詳細
	public static final String VALUE = "value"; // 正常時の戻り値
	public static final String CAUSE = "cause"; // エラーの原因 FailerCauses
	
	/**
	 * 利用しているJSONライブラリで文字列変換して返すようにすること
	 * @param res オブジェクト
	 * @return JSON文字列
	 */
	protected abstract String toJson(Object res);
	
	public void responseJson(final HttpServletResponse response,
			Object res, boolean crossDomain) throws IOException {
		log.info("response: " + res.toString());
		HttpUtils.setJsonContentType(response);
		if(crossDomain){
			HttpUtils.setCrossDomainable(response);
		}

		outputJson(response, res);
	}

	protected void outputJson(final HttpServletResponse response, Object res)
			throws IOException {
		// こっちの方式で無いとOSDEでレスポンスが二重に返ってしまう。…理由不明。
		response.getWriter().append(toJson(res));
		// JSON.encode(res, response.getOutputStream());
	}


	public void responseFailer(HttpServletResponse response, String errorMessage, String detail,
			FailerCauses cause, int statusCode) throws IOException {
		responseFailer(response, errorMessage, detail, cause, statusCode, false);
	}
	
	
	public void responseFailer(HttpServletResponse response, String errorMessage, String detail,
			FailerCauses cause, int statusCode, boolean crossDomain) throws IOException {
	
		Map<String, Object> res = new HashMap<String, Object>();
		res.put(STATUS, Status.NG.toString());
		res.put(MESSAGE, errorMessage);
		res.put(DETAIL, detail);
		res.put(CAUSE, cause.toString());
		res.put(STATUSCODE, statusCode);
	
		response.setStatus(statusCode);
		responseJson(response, res, crossDomain);
	}

	public <T> void responseSuccess(HttpServletResponse response,
			T resObj) throws IOException {
		responseSuccess(response, resObj, false);
	}
	
	public <T> void responseSuccess(HttpServletResponse response,
			T resObj, boolean crossDomain) throws IOException {
		responseJson(response, resObj, crossDomain);
	}


}
