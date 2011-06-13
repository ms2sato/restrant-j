package net.infopeers.restrant.engine.parser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.infopeers.restrant.engine.PatternInvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.params.EditableParams;

/**
 * テキスト表現のUrlParser。
 * <p>
 * 下記のようにRoR表記にならって判断される。 「@restful」はactionの選別をHTTPメソッドで行う。
 * </p>
 * <ol>
 * <li>/:controller/args/:id
 * <li>/test/:action/:id :controller=samples
 * <li>/:controller/:action/:id
 * <li>/:controller/:id @restful
 * </ol>
 * 
 * @author ms2
 * 
 */
public class TextUrlParser implements UrlParserWithPathFormat {

	private static final String RESTFUL_ATTRIBUTE = "@restful";

	private static final String GET_METHOD_ATTRIBUTE = "@get";
	private static final String POST_METHOD_ATTRIBUTE = "@post";
	private static final String PUT_METHOD_ATTRIBUTE = "@put";
	private static final String DELETE_METHOD_ATTRIBUTE = "@delete";
	private static final String HEAD_METHOD_ATTRIBUTE = "@head";
	private static final String OPTIONS_METHOD_ATTRIBUTE = "@options";

	private static HashMap<String, String> httpMethods = new HashMap<String, String>();

	static {
		httpMethods.put(GET_METHOD_ATTRIBUTE, PatternInvokerBuilder.GET);
		httpMethods.put(POST_METHOD_ATTRIBUTE, PatternInvokerBuilder.POST);
		httpMethods.put(PUT_METHOD_ATTRIBUTE, PatternInvokerBuilder.PUT);
		httpMethods.put(DELETE_METHOD_ATTRIBUTE, PatternInvokerBuilder.DELETE);
		httpMethods.put(HEAD_METHOD_ATTRIBUTE, PatternInvokerBuilder.HEAD);
		httpMethods.put(OPTIONS_METHOD_ATTRIBUTE, PatternInvokerBuilder.OPTIONS);
	}

	private PlaceholderFormatter phFormatter;

	private String fullFormat;

	private String[] section; // {/:controller/:action/:id?test1=1&test2=2,
								// @restful, :action=qqqq}

	private UrlPathParser urlPathParser;

	/**
	 * コンストラクタ
	 * 
	 * @param fullFormat
	 *            フォーマット文字列
	 * @param phFormatter
	 *            PlaceholderFormatter
	 */
	public TextUrlParser(String fullFormat, PlaceholderFormatter phFormatter) {
		this.fullFormat = fullFormat;
		this.phFormatter = phFormatter;

		section = fullFormat.split("[\\s]+");

		String pathFormat = section[0];
		this.urlPathParser = new UrlPathParser(phFormatter, pathFormat);
	}

	/**
	 * フォーマット文字列を取得する
	 * 
	 * @return フォーマット文字列
	 */
	public String getFormat() {
		return fullFormat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.infopeers.restrant.engine.Parser#parse(net.infopeers.restrant.engine
	 * .EditableParams, java.lang.String)
	 */
	@Override
	public boolean parse(EditableParams params, String path) {

		if (!parsePath(params, path))
			return false;

		if (section.length > 1) {
			return parseAttributes(params, section);
		}

		return true;
	}

	private boolean parsePath(EditableParams params, String path) {
		return this.urlPathParser.parse(params, path);
	}

	/**
	 * attributesはインデクス1以上が対象
	 * 
	 * @param params
	 * @param attributes
	 * @return
	 */
	private boolean parseAttributes(EditableParams params, String[] attributes) {

		for (int i = 1; i < attributes.length; ++i) {

			String attribute = attributes[i];
			if (attribute.equals(RESTFUL_ATTRIBUTE)) {
				// @restful
				params.addExtension(
						PatternInvokerBuilder.ACTION_PLACEHOLDER_LABEL, params
								.getMethod().toLowerCase());

				continue;
			}

			String httpMethod = httpMethods.get(attribute);
			if (httpMethod != null) {
				// @get, @post...

				if (!httpMethod.equals(params.getMethod().toLowerCase())) {
					return false;
				}

				params.addExtension(
						PatternInvokerBuilder.ACTION_PLACEHOLDER_LABEL, httpMethod);
				continue;
			}

			String[] argPair = attribute.split("=");
			String key = argPair[0];
			String value = argPair[1];
			if (phFormatter.isPlaceholder(key)) {
				addExtension(params, key, value);
			}
		}

		return true;
	}

	private void addExtension(EditableParams params, String key, String value) {
		params.addExtension(phFormatter.dePlaceholder(key), value);
	}

	public String toString() {
		return getClass().getName() + ":" + fullFormat;
	}

	@Override
	public String findSpecifiedPlaceHolder(String placeHolder) {

		String phLabel = phFormatter.enPlaceholder(placeHolder);

		Pattern p = Pattern.compile("[\\s]+" + phLabel
				+ "[\\s]*=([\\w]+)[\\s]+");
		Matcher m = p.matcher(fullFormat);
		if (!m.matches())
			return null;

		return m.group(1);
	}

	@Override
	public UrlPathParser getUrlPathParser() {
		return this.urlPathParser;
	}
}
