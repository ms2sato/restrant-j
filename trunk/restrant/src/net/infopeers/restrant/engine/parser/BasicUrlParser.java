package net.infopeers.restrant.engine.parser;

import java.util.ArrayList;
import java.util.List;

import net.infopeers.restrant.engine.PatternInvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.params.EditableParams;

public class BasicUrlParser implements UrlParserWithPathFormat {

	List<UrlParser> parts = new ArrayList<UrlParser>();
	UrlPathParser urlPathParser;

	public class WithParam implements UrlParser {

		String key;
		String value;

		WithParam(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public boolean parse(EditableParams params, String path) {
			params.addExtension(key, value);
			return true;
		}

		@Override
		public String findSpecifiedPlaceHolder(String placeHolder) {
			if(key.equals(placeHolder)) return value;
			return null;
		}

	}

	public class OnRestful implements UrlParser {

		@Override
		public boolean parse(EditableParams params, String path) {
			params.addExtension(PatternInvokerBuilder.ACTION_PLACEHOLDER_LABEL, params
					.getMethod().toLowerCase());
			return true;
		}

		@Override
		public String findSpecifiedPlaceHolder(String placeHolder) {
			return null;
		}

	}

	public BasicUrlParser(String pathFormat, PlaceholderFormatter phFormatter) {
		this.urlPathParser = new UrlPathParser(phFormatter, pathFormat);

		this.parts.add(urlPathParser);
	}

	public BasicUrlParser add(UrlParser parser) {
		this.parts.add(parser);
		return this;
	}

	public BasicUrlParser withParam(String key, String value) {
		this.parts.add(new WithParam(key, value));
		return this;
	}

	public BasicUrlParser onGet() {
		return action(PatternInvokerBuilder.GET);
	}

	public BasicUrlParser onPost() {
		return action(PatternInvokerBuilder.POST);
	}

	public BasicUrlParser onHead() {
		return action(PatternInvokerBuilder.HEAD);
	}

	public BasicUrlParser onOptions() {
		return action(PatternInvokerBuilder.OPTIONS);
	}

	public BasicUrlParser onDelete() {
		return action(PatternInvokerBuilder.DELETE);
	}

	public BasicUrlParser onPut() {
		return action(PatternInvokerBuilder.PUT);
	}

	public BasicUrlParser onRestful() {
		this.parts.add(new OnRestful());
		return this;
	}

	public BasicUrlParser controller(String controller) {
		this.parts.add(new WithParam(
				PatternInvokerBuilder.CONTROLLER_PLACEHOLDER_LABEL, controller));
		return this;
	}

	public BasicUrlParser action(String action) {
		this.parts.add(new WithParam(PatternInvokerBuilder.ACTION_PLACEHOLDER_LABEL,
				action));
		return this;
	}

	@Override
	public boolean parse(EditableParams params, String path) {

		for (UrlParser part : parts) {
			if (!part.parse(params, path))
				return false;
		}

		return true;
	}

	@Override
	public String findSpecifiedPlaceHolder(String placeHolder) {
		for (UrlParser part : parts) {
			String ph = part.findSpecifiedPlaceHolder(placeHolder);
			if(ph != null) return ph;
		}
		return null;
	}

	@Override
	public UrlPathParser getUrlPathParser() {
		return this.urlPathParser;
	}
	
}
