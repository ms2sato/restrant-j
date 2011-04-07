package net.infopeers.restrant;

import java.io.IOException;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infopeers.restrant.engine.DefaultParser;
import net.infopeers.restrant.engine.DefaultPlaceholderFormatter;
import net.infopeers.restrant.engine.ExtensionPolicy;
import net.infopeers.restrant.engine.Invoker;
import net.infopeers.restrant.engine.InvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.RouteInfo;

/**
 * このシステムのサーブレット
 * 
 * @author ms2
 * 
 */
@SuppressWarnings("serial")
public class ControllerServlet extends HttpServlet {

	private static final Logger logger = Logger
			.getLogger(ControllerServlet.class.getName());

	private static final String ROOT_PACKAGE_LABEL = "RootPackage"; // Web.xmlのルートパッケージ指定ラベル

	private static final String ROUTE_FORMAT_LABEL = "RouteFormat"; // Web.xmlのURLテンプレート指定ラベル

	private static final String ENCODING = "Encoding"; // Web.xmlのエンコーディング指定ラベル

	/**
	 * デフォルトのルートフォーマット
	 */
	private static final String defaultRouteFormat = "/:controller/:action/:id";

	private InvokerBuilder invokerBuilder;

	private PlaceholderFormatter phFormatter = new DefaultPlaceholderFormatter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		initInvokerBuilder(config);
	}

	@SuppressWarnings("unchecked")
	private void initInvokerBuilder(ServletConfig config) {

		String rootPackage = config.getInitParameter(ROOT_PACKAGE_LABEL);
		if (rootPackage == null) {
			throw new RuntimeException(
					"document(web.xml)/web-app/servlet/init-paramに"
							+ ROOT_PACKAGE_LABEL
							+ "の指定でアクションの格納されたパッケージを設定してください。");
		}

		TreeSet<RouteInfo> routes = new TreeSet<RouteInfo>(
				new Comparator<RouteInfo>() {
					@Override
					public int compare(RouteInfo lhv, RouteInfo rhv) {
						return lhv.index - rhv.index;
					}
				});

		for (Enumeration<String> e = config.getInitParameterNames(); e
				.hasMoreElements();) {
			String name = e.nextElement();
			if (name.startsWith(ROUTE_FORMAT_LABEL)) {

				String suffix = name.substring(ROUTE_FORMAT_LABEL.length());
				try {
					RouteInfo info = new RouteInfo();
					info.index = Integer.parseInt(suffix);
					info.format = config.getInitParameter(name);
					routes.add(info);
				} catch (NumberFormatException ex) {
					// 例外なら無視する
				}
			}
		}

		Map<String, String> multimap2exPolicy = new HashMap<String, String>();
		multimap2exPolicy
				.put("com.google.appengine.repackaged.com.google.common.collect.ArrayListMultimap",
						"net.infopeers.restrant.engine.gae.GaeExtensionPolicy");
		multimap2exPolicy
				.put("com.google.common.collect.ArrayListMultimap",
						"net.infopeers.restrant.engine.GoogleCollectionExtensionPolicy");

		ExtensionPolicy exPolicy = null;
		for (String key : multimap2exPolicy.keySet()) {
			try {
				Class.forName(key);
				try {
					exPolicy = (ExtensionPolicy) Class.forName(
							multimap2exPolicy.get(key)).newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} catch (ClassNotFoundException e) {
				// continue;
			}
		}

		if (exPolicy == null) {
			throw new IllegalStateException(
					"GoogleCollection's ArrayListMultimap was not found.");
		}

		invokerBuilder = new InvokerBuilder(exPolicy, phFormatter);

		if (routes.isEmpty()) {
			invokerBuilder.addParser(new DefaultParser(defaultRouteFormat,
					phFormatter));
		} else {
			for (RouteInfo route : routes) {
				invokerBuilder.addParser(new DefaultParser(route.format,
						phFormatter));
			}
		}

		invokerBuilder.setRootPackage(rootPackage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		if (logger.isLoggable(Level.INFO)) {
			outputRequest(req);
		}

		String enc = getServletConfig().getInitParameter(ENCODING);
		if (enc != null) {
			req.setCharacterEncoding(enc);
		}

		beforeExecute(req, resp);

		try {
			Invoker invoker = invokerBuilder.build(this, req);
			invoker.invoke(req, resp);
		} catch (ResourceNotFoundException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			afterExecute(req, resp);
		}
	}

	@SuppressWarnings("unchecked")
	private void outputRequest(HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());
		Enumeration<String> nameEnum = req.getParameterNames();
		while (nameEnum.hasMoreElements()) {
			String name = nameEnum.nextElement();
			String[] values = req.getParameterValues(name);
			for (int i = 0; i < values.length; ++i) {
				logger.info("param[" + name + "]=" + values[i]);
			}
		}
	}

	/**
	 * 処理を開始する際に必ず呼ばれる
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	protected void beforeExecute(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

	}

	/**
	 * 処理が終了した際に必ず呼ばれる
	 * 
	 * @param req
	 * @param resp
	 */
	protected void afterExecute(HttpServletRequest req, HttpServletResponse resp) {
	}

}
