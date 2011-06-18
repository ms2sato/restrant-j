package net.infopeers.restrant.engine.parser;

import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.params.EditableParams;

/**
 * URLのパス表現を読み込み、パラメータを取りだすサブパーサー
 * 
 * @author ms2
 * 
 */
public class UrlPathParser implements PatternParser {

	private PlaceholderFormatter phFormatter;

	private String patternFormat;
	private String[] pathAndQuery; // {/:controller/:action/:id,
									// test1=1&test2=2}
	private String[] formatPathParts; // {:controller, :action, :id}

	/**
	 * 
	 * @param format
	 *            /:controller/test/:action/:id?param=:param のようなパス表現フォーマット
	 */
	public UrlPathParser(PlaceholderFormatter phFormatter, String patternFormat) {
		this.phFormatter = phFormatter;
		this.patternFormat = patternFormat;

		pathAndQuery = patternFormat.split("\\?");
		if (pathAndQuery.length > 2) {
			throw new IllegalArgumentException("「?」が複数存在してはいけません");
		}

		this.formatPathParts = phFormatter.splitSeparator(pathAndQuery[0]);
	}

	@Override
	public boolean parse(EditableParams params, String path) {
		String[] pathParts = phFormatter.splitSeparator(path);

		// 長さが違うなら一致しない
		if (formatPathParts.length != pathParts.length)
			return false;

		for (int i = 0; i < formatPathParts.length; ++i) {
			String formatPart = formatPathParts[i];
			String pathPart = pathParts[i];

			if (phFormatter.isPlaceholder(formatPart)) {
				addExtension(params, formatPart, pathPart);
				continue;
			}

			if (!formatPart.equals(pathPart)) {
				return false;
			}
		}

		if (pathAndQuery.length == 2) {
			String query = pathAndQuery[1];

			// ex: path/to/url?:body
			if (!isFormtype()) {
				String key = phFormatter.dePlaceholder(query);
				params.addContentParamKey(key);
				return true;
			}

			return parseReqParams(params, query.split("&"));
		}
		return true;
	}

	private boolean parseReqParams(EditableParams params, String[] reqParams) {

		for (int i = 0; i < reqParams.length; ++i) {

			String reqParam = reqParams[i];
			String[] kv = reqParam.split("=");
			String key = kv[0];
			String value = kv[1];

			String paramValue = params.getParameter(key);
			if (paramValue == null) {
				// フォーマットに含まれるキーに対応する値が無いなら一致していない
				return false;
			}

			if (phFormatter.isPlaceholder(value)) {

				String[] reqValues = params.gets(key);
				if (reqValues != null && reqValues.length != 0) {
					for (String reqValue : reqValues) {
						addExtension(params, value, reqValue);
					}
				} else {
					String reqValue = params.get(key);
					if (reqValue == null)
						return false;
					addExtension(params, value, reqValue);
				}
			} else {
				if (!value.equals(paramValue)) {
					// プレースホルダでないならばフォーマットとリクエスト値が一致しなければならない
					return false;
				}

			}
		}

		return true;
	}

	private void addExtension(EditableParams params, String key, String value) {
		params.addExtension(phFormatter.dePlaceholder(key), value);
	}

	@Override
	public String findSpecifiedPlaceHolder(String placeHolder) {
		return null;
	}

	public String getPatternFormat() {
		return this.patternFormat;
	}

	public String getPath() {
		return this.pathAndQuery[0];
	}

	public PlaceholderFormatter getPlaceholderFormatter() {
		return this.phFormatter;
	}

	public boolean isFormtype() {
		if (this.pathAndQuery.length == 2
				&& !this.pathAndQuery[1].contains("="))
			return false;

		return true;
	}

	public String getBodyParamLabel() {
		return this.phFormatter.dePlaceholder(pathAndQuery[1]);
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}

}