package net.infopeers.restrant.engine;

import java.util.ArrayList;
import java.util.List;

public class CodableParser implements Parser {

	List<Parser> parts = new ArrayList<Parser>();
	UrlPathParser urlPathParser;

	public class WithParam implements Parser {

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

	}

	public class OnRestful implements Parser {

		@Override
		public boolean parse(EditableParams params, String path) {
			params.addExtension(InvokerBuilder.ACTION_PLACEHOLDER_LABEL, params
					.getMethod().toLowerCase());
			return true;
		}

	}

	public CodableParser(String pathFormat, PlaceholderFormatter phFormatter) {
		this.urlPathParser = new UrlPathParser(phFormatter, pathFormat);

		this.parts.add(urlPathParser);
	}

	public CodableParser add(Parser parser) {
		this.parts.add(parser);
		return this;
	}

	public CodableParser withParam(String key, String value) {
		this.parts.add(new WithParam(key, value));
		return this;
	}

	public CodableParser onGet() {
		return action(InvokerBuilder.GET);
	}

	public CodableParser onPost() {
		return action(InvokerBuilder.POST);
	}

	public CodableParser onHead() {
		return action(InvokerBuilder.HEAD);
	}

	public CodableParser onOptions() {
		return action(InvokerBuilder.OPTIONS);
	}

	public CodableParser onDelete() {
		return action(InvokerBuilder.DELETE);
	}

	public CodableParser onPut() {
		return action(InvokerBuilder.PUT);
	}

	public CodableParser onRestful() {
		this.parts.add(new OnRestful());
		return this;
	}

	public CodableParser controller(String controller) {
		this.parts.add(new WithParam(
				InvokerBuilder.CONTROLLER_PLACEHOLDER_LABEL, controller));
		return this;
	}

	public CodableParser action(String action) {
		this.parts.add(new WithParam(InvokerBuilder.ACTION_PLACEHOLDER_LABEL,
				action));
		return this;
	}

	@Override
	public boolean parse(EditableParams params, String path) {

		for (Parser part : parts) {
			if (!part.parse(params, path))
				return false;
		}

		return true;
	}

}
