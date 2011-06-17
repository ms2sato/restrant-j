package net.infopeers.restrant.engine.jsservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;

import net.infopeers.commons.io.StreamUtils;
import net.infopeers.commons.regex.Replacer;
import net.infopeers.restrant.engine.PatternInvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.parser.PatternParserWithPathFormat;
import net.infopeers.restrant.engine.parser.UrlPathParser;
import net.infopeers.restrant.util.AnnotationUtils;

class Templator {

	private static String DEFAULT_NS = "RESTRANTJS";

	String namespace;
	MessageFormat functionTemplate;
	PlaceholderFormatter phFormatter;
	Pattern phPattern;

	public Templator(PlaceholderFormatter phFormatter) {
		this(DEFAULT_NS, phFormatter);
	}

	public Templator(String namespace, PlaceholderFormatter phFormatter) {
		this.namespace = namespace;
		this.phFormatter = phFormatter;

		try {
			functionTemplate = new MessageFormat(StreamUtils.toString(this
					.getClass().getResourceAsStream("function.txt")));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.phPattern = Pattern.compile(phFormatter
				.getRegexToReplacePlaceHolders());
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void appendHeader(PrintWriter writer) throws IOException {
		String file = "header.txt";
		MessageFormat headerTemplate = new MessageFormat(
				StreamUtils.toString(this.getClass().getResourceAsStream(file)));

		writer.print(headerTemplate.format(new Object[] { namespace }));
	}

	public void appendFooter(PrintWriter writer) throws IOException {
		String file = "footer.txt";
		MessageFormat headerTemplate = new MessageFormat(
				StreamUtils.toString(this.getClass().getResourceAsStream(file)));

		writer.print(headerTemplate.format(new Object[] {}));
	}

	public void appendClass(PrintWriter writer, Class<?> cls)
			throws IOException {
		String file = "class.txt";
		MessageFormat headerTemplate = new MessageFormat(
				StreamUtils.toString(this.getClass().getResourceAsStream(file)));

		writer.print(headerTemplate.format(new Object[] { namespace,
				cls.getSimpleName() }));
	}

	public void appendFunction(PrintWriter writer, Method method,
			PatternParserWithPathFormat patternParser) {

		net.infopeers.restrant.Method ma = method
				.getAnnotation(net.infopeers.restrant.Method.class);
		String[] argLabels;
		if (ma == null) {
			argLabels = new String[] {};
		} else {
			argLabels = AnnotationUtils.getArgs(ma);
		}

		String name = method.getName();
		StringBuilder jsonParams = new StringBuilder();
		StringBuilder funcParams = new StringBuilder();
		int argLength = AnnotationUtils
				.checkParameterLengthToAnnotationArgLabelLength(method,
						argLabels);

		for (int i = 0; i < argLength; ++i) {
			String paramName = argLabels[i];
			if (i != 0) {
				jsonParams.append(", ");	
			}
			jsonParams.append("'").append(paramName).append("' : ")
					.append(paramName);

			funcParams.append(", ").append(paramName);
		}

		List<String> httpMethods = patternParser.getHttpMethods();
		String httpMethod;
		if (httpMethods.isEmpty()) {
			httpMethod = PatternInvokerBuilder.GET;
		} else {
			httpMethod = httpMethods.get(0);
		}

		writer.println(functionTemplate.format(new Object[] { namespace,
				method.getDeclaringClass().getSimpleName(), name, funcParams,
				toPathInScript(patternParser), jsonParams,
				httpMethod.toUpperCase() }));
	}

	/**
	 * change to path for script. path/to/:placeholder/format to path/to/' +
	 * placeholder + '/format
	 * 
	 * @param patternParser
	 * @return
	 */
	private String toPathInScript(PatternParserWithPathFormat patternParser) {
		UrlPathParser pp = patternParser.getUrlPathParser();
		String path = pp.getPath();

		Replacer rep = new Replacer(phPattern) {
			@Override
			protected String replace(int groupIndex, String value) {
				return "' + " + value + " + '";
			}
		};
		return rep.replace(path);
	}
}