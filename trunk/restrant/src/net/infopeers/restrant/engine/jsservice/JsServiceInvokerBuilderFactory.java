package net.infopeers.restrant.engine.jsservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import net.infopeers.restrant.engine.AbstractInvokerBuilderFactory;
import net.infopeers.restrant.engine.InvokerBuilder;
import net.infopeers.restrant.engine.ParserHolder;
import net.infopeers.restrant.engine.PatternInvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.parser.PatternParser;
import net.infopeers.restrant.engine.parser.PatternParserArranger;
import net.infopeers.restrant.engine.parser.PatternParserWithPathFormat;
import net.infopeers.restrant.engine.parser.UrlPathParser;

/**
 * 
 * 条件：
 * <ul>
 * <li>@Methodが指定されていること。</li>
 * <li>パターンのURL表現の途中に:controllerのようなプレースホルダが現れず、
 * Routeクラス等で直接値指定がされていること。</li>
 * <li>@Restfulでないこと</li>
 * </ul>
 * 
 * @author ms2
 * 
 */
public class JsServiceInvokerBuilderFactory extends
		AbstractInvokerBuilderFactory {

	private String rootPackage;
	private PatternParserArranger parserArranger;
	private String namespace;
	private PlaceholderFormatter phFormatter;

	public JsServiceInvokerBuilderFactory(String rootPackage,
			PatternParserArranger parserArranger, String serviceJsNamespace, PlaceholderFormatter phFormatter) {
		this.rootPackage = rootPackage;
		this.parserArranger = parserArranger;
		this.namespace = serviceJsNamespace;
		this.phFormatter = phFormatter;
	}

	@Override
	protected InvokerBuilder createInvokerBuilder() {

		final List<PatternParser> parsers = new ArrayList<PatternParser>();

		ParserHolder holder = new ParserHolder() {
			@Override
			public void addParser(PatternParser parser) {
				parsers.add(parser);
			}
		};

		parserArranger.arrange(holder);

		Templator templator = new Templator(namespace, phFormatter);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		Set<String> classNameSet = new HashSet<String>();

		try {
			templator.appendHeader(pw);

			for (PatternParser p : parsers) {

				PatternParserWithPathFormat pp;
				UrlPathParser pathParser;
				if (p instanceof PatternParserWithPathFormat) {
					pathParser = ((PatternParserWithPathFormat) p)
							.getUrlPathParser();
					pp = (PatternParserWithPathFormat)p;
				} else {
					continue;
				}

				String controllerSimpleName = p
						.findSpecifiedPlaceHolder(PatternInvokerBuilder.CONTROLLER_PLACEHOLDER_LABEL);
				if (controllerSimpleName == null)
					continue;

				String actionName = p
						.findSpecifiedPlaceHolder(PatternInvokerBuilder.ACTION_PLACEHOLDER_LABEL);
				if (actionName == null)
					continue;

				String controllerName = PatternInvokerBuilder
						.getFullControllerClassName(controllerSimpleName,
								rootPackage);
				try {
					Class<?> cls = Class.forName(controllerName);

					if (!classNameSet.contains(controllerName)) {
						templator.appendClass(pw, cls);
						classNameSet.add(controllerName);
					}

					for (Method m : cls.getMethods()) {

						net.infopeers.restrant.Method ma = m.getAnnotation(net.infopeers.restrant.Method.class); 
						if (ma == null)
							continue;

						if (!m.getName().equals(actionName)){
							if(!ma.name().equals(actionName)){
								continue;
							}
						}
						
						if(pathParser.isFormtype()){
							templator.appendFunction4formtype(pw, actionName, m, pp);
						}
						else{
							templator.appendFunction4bodytype(pw, actionName, m, pp, pathParser.getBodyParamLabel());
						}
					}

					//System.out.println(sw.toString());
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}

			}

			templator.appendFooter(pw);
			return new InvokerBuilderImpl(sw.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			pw.close();
		}
	}

}
