package net.infopeers.restrant.engine;

/**
 * URLのパス表現を読み込み、パラメータを取りだすサブパーサー
 * 
 * @author ms2
 * 
 */
public class UrlPathParser implements Parser {

	private PlaceholderFormatter phFormatter;
	private String[] pathAndQuery; // {/:controller/:action/:id,
									// test1=1&test2=2}
	private String[] formatPathParts; // {:controller, :action, :id}

	/**
	 * 
	 * @param format
	 *            /:controller/test/:action/:id のようなパス表現フォーマット
	 */
	UrlPathParser(PlaceholderFormatter phFormatter, String pathFormat) {
		this.phFormatter = phFormatter;

		pathAndQuery = pathFormat.split("\\?");
		if (pathAndQuery.length > 2) {
			throw new IllegalArgumentException("「?」が複数存在してはいけません");
		}

		this.formatPathParts = pathAndQuery[0].split("[/\\.]");
	}

	@Override
	public boolean parse(EditableParams params, String path) {
		String[] pathParts = path.split("[/\\.]");

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
			return parseReqParams(params, pathAndQuery[1].split("&"));
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

}