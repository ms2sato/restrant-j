package net.infopeers.restrant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infopeers.restrant.engine.PatternInvokerBuilderFactory;
import net.infopeers.restrant.engine.PrefixedPlaceholderFormatter;
import net.infopeers.restrant.engine.Invoker;
import net.infopeers.restrant.engine.InvokerBuilderFactory;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.jsservice.JsServiceInvokerBuilderFactory;
import net.infopeers.restrant.engine.params.ExtensionMultimapFactory;
import net.infopeers.restrant.engine.parser.CompositeUrlParserArranger;
import net.infopeers.restrant.engine.parser.PatternParserArranger;
import net.infopeers.restrant.route.RouteClassUrlParserArranger;
import net.infopeers.restrant.util.ServletConfigUrlParserArranger;

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
	private static final String ROUTE_CLASS_LABEL = "RouteClass"; // Web.xmlのRouteクラス指定ラベル
	private static final String SERVICE_JS_LABEL = "ServiceJs"; // Web.xmlのJavaScriptサービス指定ラベル
	private static final String SERVICE_JS_NAMESPACE_LABEL = "ServiceJsNamespace"; // Web.xmlのJavaScriptでJSのネームスペース指定ラベル

	private static final String ENCODING = "Encoding"; // Web.xmlのエンコーディング指定ラベル

	private PlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();

	private static final String ibfDefaultName = ":default"; // 将来的にはパス→実装クラス対応か？
	private Map<String, InvokerBuilderFactory> path2ibf = new HashMap<String, InvokerBuilderFactory>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			initInvokerBuilder(config);
		} catch (MalformedURLException e) {
			throw new ServletException(e);
		}
	}

	private void initInvokerBuilder(ServletConfig config)
			throws MalformedURLException {

		String rootPackage = config.getInitParameter(ROOT_PACKAGE_LABEL);
		if (rootPackage == null) {
			throw new RuntimeException(
					"document(web.xml)/web-app/servlet/init-paramに"
							+ ROOT_PACKAGE_LABEL
							+ "の指定でコントローラーの格納されたパッケージを設定してください。");
		}

		PatternParserArranger parserArranger = createParserArranger(config);
		ExtensionMultimapFactory exPolicy = createExtensionPolicy();
		path2ibf.put(ibfDefaultName, new PatternInvokerBuilderFactory(
				rootPackage, parserArranger, exPolicy));

		String serviceJsPath = config.getInitParameter(SERVICE_JS_LABEL);
		if (serviceJsPath != null) {

			String serviceJsNamespace = config
					.getInitParameter(SERVICE_JS_NAMESPACE_LABEL);
			path2ibf.put(serviceJsPath, new JsServiceInvokerBuilderFactory(
					rootPackage, parserArranger, serviceJsNamespace,
					phFormatter));
		}

	}

	private CompositeUrlParserArranger createParserArranger(ServletConfig config) {
		CompositeUrlParserArranger arranger = new CompositeUrlParserArranger();

		String routeClass = config.getInitParameter(ROUTE_CLASS_LABEL);
		if (routeClass != null) {
			try {
				Class<?> cls = Class.forName(routeClass);
				RouteClassUrlParserArranger cdarranger = new RouteClassUrlParserArranger(
						phFormatter, cls);
				arranger.add(cdarranger);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"document(web.xml)/web-app/servlet/init-paramに"
								+ ROUTE_CLASS_LABEL + "で指定されたクラスが見つかりません。", e);
			}
		}

		arranger.add(new ServletConfigUrlParserArranger(phFormatter, config));
		return arranger;
	}

	private ExtensionMultimapFactory createExtensionPolicy() {
		Map<String, String> multimap2exPolicy = new HashMap<String, String>();
		multimap2exPolicy
				.put("com.google.appengine.repackaged.com.google.common.collect.ArrayListMultimap",
						"net.infopeers.restrant.util.gae.GaeExtensionMultimapFactory");
		multimap2exPolicy
				.put("com.google.common.collect.ArrayListMultimap",
						"net.infopeers.restrant.util.GoogleCollectionExtensionMultimapFactory");

		ExtensionMultimapFactory exPolicy = null;
		for (String key : multimap2exPolicy.keySet()) {
			try {
				Class.forName(key);
				try {
					exPolicy = (ExtensionMultimapFactory) Class.forName(
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
		return exPolicy;
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

		String enc = getServletConfig().getInitParameter(ENCODING);
		if (enc != null) {
			req.setCharacterEncoding(enc);
		}

		beforeExecute(req, resp);

		if (logger.isLoggable(Level.INFO)) {
			outputRequest(req);
		}

		try {
			Invoker invoker = getInvoker(req);
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

	private Invoker getInvoker(HttpServletRequest req)
			throws ResourceNotFoundException, MalformedURLException {

		String path = req.getRequestURI().substring(
				req.getContextPath().length());
		InvokerBuilderFactory factory = path2ibf.get(path);
		if (factory == null) {
			factory = path2ibf.get(ibfDefaultName);
		}

		// TODO: 柔軟にするには切り出す。本来は初期化時に行えれば良い
		URL url = new URL(req.getRequestURL().toString());
		String host = url.getHost();
		factory.setEvery(host.equals("localhost"));

		Invoker invoker = factory.getInvokerBuilder().build(this, req);
		return invoker;
	}

	@SuppressWarnings("unchecked")
	private void outputRequest(HttpServletRequest req) {
		logger.info(req.getRequestURL().toString());
//		Enumeration<String> nameEnum = req.getParameterNames();
//		while (nameEnum.hasMoreElements()) {
//			String name = nameEnum.nextElement();
//			String[] values = req.getParameterValues(name);
//			for (int i = 0; i < values.length; ++i) {
//				logger.info("param[" + name + "]=" + values[i]);
//			}
//		}
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
