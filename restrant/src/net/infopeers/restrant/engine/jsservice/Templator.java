package net.infopeers.restrant.engine.jsservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	abstract class FunctionAppender {

		MessageFormat format;

		FunctionAppender(MessageFormat format) {
			this.format = format;
		}

		public void append(PrintWriter writer, Method method,
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
			AnnotationUtils.checkParameterLengthToAnnotationArgLabelLength(
					method, argLabels);

			List<String> httpMethods = patternParser.getHttpMethods();
			String httpMethod;
			if (httpMethods.isEmpty()) {
				httpMethod = PatternInvokerBuilder.GET;
			} else {
				httpMethod = httpMethods.get(0);
			}

			Set<String> usedParam = new HashSet<String>();
			String path4Script = toPath4Script(usedParam, patternParser);

			StringBuilder funcParams = new StringBuilder();
			for (int i = 0; i < argLabels.length; ++i) {
				String paramName = argLabels[i];
				funcParams.append(", ").append(paramName);
			}

			StringBuilder jsonParams = new StringBuilder();
			createParams(argLabels, usedParam, jsonParams);
			writer.println(format.format(new Object[] { namespace,
					method.getDeclaringClass().getSimpleName(), name,
					funcParams, path4Script, jsonParams,
					httpMethod.toUpperCase() }));
		}

		protected abstract void createParams(String[] argLabels,
				Set<String> usedParam, StringBuilder jsonParams);

		/**
		 * change to path for script. path/to/:placeholder/format to path/to/' +
		 * placeholder + '/format
		 * 
		 * @param patternParser
		 * @return
		 */
		private String toPath4Script(final Set<String> usedParam,
				PatternParserWithPathFormat patternParser) {
			UrlPathParser pp = patternParser.getUrlPathParser();
			String path = pp.getPath();

			Replacer rep = new Replacer(phPattern) {
				@Override
				protected String replace(int groupIndex, String value) {
					usedParam.add(value);
					return "' + " + value + " + '";
				}
			};
			return rep.replace(path);
		}
	}

	class FunctionAppender4formtype extends FunctionAppender {

		FunctionAppender4formtype(MessageFormat format) {
			super(format);
		}

		protected void createParams(String[] argLabels, Set<String> usedParam,
				StringBuilder jsonParams) {
			int count = 0;
			for (int i = 0; i < argLabels.length; ++i) {
				String paramName = argLabels[i];
				if (usedParam.contains(paramName))
					continue;

				if (count != 0) {
					jsonParams.append(", ");
				}
				jsonParams.append("'").append(paramName).append("' : ")
						.append(paramName);

				count++;
			}
		}
	}

	class FunctionAppender4bodytype extends FunctionAppender {

		String bodyParamLabel;
		
		FunctionAppender4bodytype(MessageFormat format, String bodyParamLabel) {
			super(format);
			this.bodyParamLabel = bodyParamLabel;
		}

		protected void createParams(String[] argLabels, Set<String> usedParam,
				StringBuilder jsonParams) {
			jsonParams.append(bodyParamLabel);
		}
	}
	
	String namespace;

	PlaceholderFormatter phFormatter;
	Pattern phPattern;
	
	MessageFormat formtypeFunctionTemplate;
	MessageFormat bodytypeFunctionTemplate;
	
	public Templator(PlaceholderFormatter phFormatter) {
		this(DEFAULT_NS, phFormatter);
	}

	public Templator(String namespace, PlaceholderFormatter phFormatter) {
		this.namespace = namespace;
		this.phFormatter = phFormatter;

		try {
			formtypeFunctionTemplate = new MessageFormat(
					StreamUtils.toString(this.getClass().getResourceAsStream(
							"function_form.txt")));

			bodytypeFunctionTemplate = new MessageFormat(
					StreamUtils.toString(this.getClass().getResourceAsStream(
							"function_body.txt")));

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

	public void appendFunction4formtype(PrintWriter writer, Method method,
			PatternParserWithPathFormat patternParser) {
		FunctionAppender4formtype formtypeFunction = new FunctionAppender4formtype(
				formtypeFunctionTemplate);
		formtypeFunction.append(writer, method, patternParser);
	}

	public void appendFunction4bodytype(PrintWriter writer, Method method,
			PatternParserWithPathFormat patternParser, String bodyParamLabel) {
		FunctionAppender4bodytype bodytypeFunction = new FunctionAppender4bodytype(
				bodytypeFunctionTemplate, bodyParamLabel);
		bodytypeFunction.append(writer, method, patternParser);
	}

}