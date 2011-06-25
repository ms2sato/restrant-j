package net.infopeers.restrant.commons.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpUtils {

	public static void setJsonContentType(HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
	}
	
	public static void setCrossDomainable(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
	}

	public static String getServerName(HttpServletRequest req) {
		return req.getScheme() + "://"
				+ req.getHeader("Host");
	}

}
