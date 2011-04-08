package net.infopeers.restrant.engine;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.TreeSet;

import javax.servlet.ServletConfig;

public class ServletConfigParserArranger implements ParserArranger {

	private static final String ROUTE_FORMAT_LABEL = "RouteFormat"; // Web.xmlのURLテンプレート指定ラベル
	
	ServletConfig config;
	PlaceholderFormatter phFormatter;

	@SuppressWarnings("unchecked")
	protected Enumeration<String> getEnumration() {
		return config.getInitParameterNames();
	}
	
	protected String getFormatByName(String name) {
		return config.getInitParameter(name);
	}

	public ServletConfigParserArranger(PlaceholderFormatter phFormatter,
			ServletConfig config) {
		this.phFormatter = phFormatter;
		this.config = config;
	}

	@Override
	public void arrange(InvokerBuilder invokerBuilder) {
		
		TreeSet<RouteInfo> routes = new TreeSet<RouteInfo>(
				new Comparator<RouteInfo>() {
					@Override
					public int compare(RouteInfo lhv, RouteInfo rhv) {
						return lhv.index - rhv.index;
					}
				});

		for (Enumeration<String> e = getEnumration(); e.hasMoreElements();) {
			String name = e.nextElement();
			if (name.startsWith(ROUTE_FORMAT_LABEL)) {

				String suffix = name.substring(ROUTE_FORMAT_LABEL.length());
				try {
					RouteInfo info = new RouteInfo();
					info.index = Integer.parseInt(suffix);
					info.format = getFormatByName(name);
					routes.add(info);
				} catch (NumberFormatException ex) {
					// 例外なら無視する
				}
			}
		}

		for (RouteInfo route : routes) {
			invokerBuilder.addParser(new TextParser(route.format, phFormatter));
		}

	}

}
