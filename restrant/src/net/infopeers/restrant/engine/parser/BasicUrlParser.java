package net.infopeers.restrant.engine.parser;

import java.util.ArrayList;
import java.util.List;

import net.infopeers.restrant.engine.InvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.params.EditableParams;

public class BasicUrlParser implements UrlParser {

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

	}

	public class OnRestful implements UrlParser {

		@Override
		public boolean parse(EditableParams params, String path) {
			params.addExtension(InvokerBuilder.ACTION_PLACEHOLDER_LABEL, params
					.getMethod().toLowerCase());
			return true;
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
		return action(InvokerBuilder.GET);
	}

	public BasicUrlParser onPost() {
		return action(InvokerBuilder.POST);
	}

	public BasicUrlParser onHead() {
		return action(InvokerBuilder.HEAD);
	}

	public BasicUrlParser onOptions() {
		return action(InvokerBuilder.OPTIONS);
	}

	public BasicUrlParser onDelete() {
		return action(InvokerBuilder.DELETE);
	}

	public BasicUrlParser onPut() {
		return action(InvokerBuilder.PUT);
	}

	public BasicUrlParser onRestful() {
		this.parts.add(new OnRestful());
		return this;
	}

	public BasicUrlParser controller(String controller) {
		this.parts.add(new WithParam(
				InvokerBuilder.CONTROLLER_PLACEHOLDER_LABEL, controller));
		return this;
	}

	public BasicUrlParser action(String action) {
		this.parts.add(new WithParam(InvokerBuilder.ACTION_PLACEHOLDER_LABEL,
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

}
