package net.infopeers.restrant.engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infopeers.commons.io.StreamUtils;
import net.infopeers.restrant.ControllerServlet;
import net.infopeers.restrant.ResourceNotFoundException;
import net.infopeers.restrant.engine.parser.PatternParser;
import net.infopeers.restrant.engine.parser.PatternParserArranger;
import net.infopeers.restrant.engine.parser.PatternParserWithPathFormat;
import net.infopeers.restrant.engine.parser.UrlPathParser;

public class JsServiceInvokerBuilderFactory extends
		AbstractInvokerBuilderFactory {

	class InvokerBuilderImpl implements InvokerBuilder {

		@Override
		public Invoker build(ControllerServlet servlet, HttpServletRequest req)
				throws ResourceNotFoundException {

			return new Invoker() {

				@Override
				public void invoke(HttpServletRequest req,
						HttpServletResponse resp) throws Exception {
					// TODO Auto-generated method stub

				}

			};
		}

	}

	static class Templator {
		String namespace = "TEST";
		MessageFormat functionTemplate;

		public Templator() {
			try {
				functionTemplate = new MessageFormat(StreamUtils.toString(this
						.getClass().getResourceAsStream("function.txt")));

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void appendFunction(PrintWriter writer, Method m,
				UrlPathParser pathParser) {

			String name = m.getName();

			StringBuilder params = new StringBuilder();
			Class<?>[] types = m.getParameterTypes();
			for (int i = 0; i < types.length; ++i) {
				params.append(", ");
				Class<?> type = types[i];
				params.append(type.getSimpleName().toLowerCase() + (i + 1));
			}

			writer.println(functionTemplate.format(new Object[] { namespace,
					m.getDeclaringClass().getSimpleName(), name, params,
					pathParser.getPatternFormat() }));
		}
	}

	private String rootPackage;
	private PatternParserArranger parserArranger;

	public JsServiceInvokerBuilderFactory(String rootPackage,
			PatternParserArranger parserArranger) {
		this.rootPackage = rootPackage;
		this.parserArranger = parserArranger;
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

		Templator templator = new Templator();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			for (PatternParser p : parsers) {

				UrlPathParser pathParser;
				if (p instanceof PatternParserWithPathFormat) {
					pathParser = ((PatternParserWithPathFormat) p)
							.getUrlPathParser();
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
					for (Method m : cls.getMethods()) {
						if (!m.getName().equals(actionName))
							continue;

						if (pathParser.getPlaceholderFormatter()
								.hasPlaceholder(pathParser.getPath())) {
							// path with placeholder is not target
							continue;
						}

						templator.appendFunction(pw, m, pathParser);
					}

					System.out.println(sw.toString());
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}

			}
		} finally {
			pw.close();
		}

		return new InvokerBuilderImpl();
	}

}
