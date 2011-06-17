package net.infopeers.commons.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Replacer {

	Pattern p;

	public Replacer(Pattern p) {
		this.p = p;
	}

	public String replace(String text) {
		Matcher m = p.matcher(text);
		StringBuilder sb = new StringBuilder();
		int start = 0;
		while (m.find()) {

			int count = m.groupCount();
			for (int i = 1; i <= count; ++i) {
				String name = m.group(i);
				int s = m.start(i);
				int e = m.end(i);
				sb.append(replace(text.substring(start, s - 1))).append(
						replace(i, name));
				start = e;
			}
		}

		sb.append(text.substring(start));
		return sb.toString();
	}

	protected abstract String replace(int groupIndex, String value);

}