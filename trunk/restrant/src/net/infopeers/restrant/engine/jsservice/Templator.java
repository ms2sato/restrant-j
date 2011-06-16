package net.infopeers.restrant.engine.jsservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import net.infopeers.commons.io.StreamUtils;
import net.infopeers.restrant.engine.parser.PatternParserWithPathFormat;
import net.infopeers.restrant.util.AnnotationUtils;

class Templator {

	String namespace = "RESTRANTJS";
	MessageFormat functionTemplate;

	public Templator() {
		try {
			functionTemplate = new MessageFormat(StreamUtils.toString(this
					.getClass().getResourceAsStream("function.txt")));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void appendHeader(PrintWriter writer) throws IOException {
		String file = "header.txt";
		MessageFormat headerTemplate = new MessageFormat(
				StreamUtils.toString(this.getClass().getResourceAsStream(
						file)));

		writer.print(headerTemplate.format(new Object[] { namespace }));
	}

	public void appendFooter(PrintWriter writer) throws IOException {
		String file = "footer.txt";
		MessageFormat headerTemplate = new MessageFormat(
				StreamUtils.toString(this.getClass().getResourceAsStream(
						file)));

		writer.print(headerTemplate.format(new Object[] {}));
	}

	public void appendClass(PrintWriter writer, Class<?> cls)
			throws IOException {
		String file = "class.txt";
		MessageFormat headerTemplate = new MessageFormat(
				StreamUtils.toString(this.getClass().getResourceAsStream(
						file)));

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
			jsonParams.append("'");
			String paramName = argLabels[i];
			jsonParams.append(paramName);
			jsonParams.append("' : ").append(paramName);

			funcParams.append(", ").append(paramName);
		}

		writer.println(functionTemplate.format(new Object[] { namespace,
				method.getDeclaringClass().getSimpleName(), name,
				funcParams, patternParser.getUrlPathParser().getPath(), jsonParams }));
	}
}