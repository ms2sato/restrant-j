package net.infopeers.restrant.engine.params;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 一般的なParamsの実装
 * 
 * @author ms2
 * 
 */
public class ParamsImpl extends AbstractParams {

	private HttpServletRequest req;

	/**
	 * コンストラクタ
	 * 
	 * @param exPolicy
	 *            ExtensionPolicy
	 * @param req
	 *            HttpServletRequest
	 */
	public ParamsImpl(ExtensionMultimap exPolicy, HttpServletRequest req) {
		super(exPolicy);
		this.req = req;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#getParameter(java.lang.String)
	 */
	public String getParameter(String key) {
		return req.getParameter(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#getParameters(java.lang.String)
	 */
	public String[] getParameters(String key) {
		return req.getParameterValues(key);
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getParameterNames() {
		return req.getParameterNames();
	}

	@SuppressWarnings("unchecked")
	public Set<String> nameSet() {

		// TODO: より効率の良い実装があるだろう。頻度が低いはずなので今はこの実装
		Set<String> names = getExtensionNames();

		for (Enumeration<String> e = req.getParameterNames(); e
				.hasMoreElements();) {
			names.add(e.nextElement());
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.infopeers.restrant.Params#getMethod()
	 */
	public String getMethod() {
		return req.getMethod();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return req.getInputStream();
	}

	@Override
	public boolean isStreamContent() {
		String type = req.getHeader("Content-Type");
		if (type == null)
			return false;
		if (type.equals("www-form-urlencoded"))
			return false;
		return true;
	}

}