package net.infopeers.util.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class WebUtils {

	public static String urlFor(HttpServletRequest req, String path){
		if(path.startsWith("/")){
			return req.getContextPath() + path;
		}
		
		return path;
	}

	/**
	 * 
	 * @param req
	 * @param path
	 * @param full フルパスで取得
	 * @return
	 */
	public static String urlFor(HttpServletRequest req, String path, boolean full){

		if(full){
			//スキーマを含むのは既にフルパス扱い
			if(path.contains("://")){
				return path;
			}
			
			if(path.startsWith("/")){
				return getFullContextPath(req) + path;
			}
			
			return getFullContextPath(req) + '/' + path;
		}
		else{
			if(path.startsWith("/")){
				return req.getContextPath() + path;
			}
			
			return path;
		}
	}
	
	
	/**
	 * オリジンを取得する
	 */
	public static String getOrigin(HttpServletRequest req) {
		String origin = req.getScheme() + "://"
				+ req.getHeader("Host");
//
//		int port = req.getServerPort();
//		if(port != 80){
//			return origin + ":" + port;
//		}
		
		return origin;
	}
	
	/**
	 * コンテキストパスをフルURLで取得する
	 * @param req
	 * @return
	 */
	public static String getFullContextPath(HttpServletRequest req){
		
		return getOrigin(req) + req.getContextPath();
	}
	
	
	public static String getSafeContextParam(ServletContext context, String name) {
		String param = context.getInitParameter(name);
		if (param == null) {
			throw new RuntimeException(name
					+ " is not defined. on document(WEB.xml)/context-param");
		}
		return param;
	}

	public static String getSafeConfigParam(ServletConfig config, String name) {
		String param = config.getInitParameter(name);
		if (param == null) {
			String servlet = config.getServletName();
			throw new RuntimeException(name
					+ " is not defined. on document(WEB.xml)/" + servlet
					+ "context-param");
		}
		return param;
	}
	
	public static void removeCookies(HttpServletRequest req){
		for(Cookie c: req.getCookies()){
			c.setMaxAge(0);
		}
	}
}
