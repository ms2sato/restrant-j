package net.infopeers.restrant.engine.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.infopeers.restrant.engine.PatternInvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.params.EditableParams;

public class BasicUrlParser implements PatternParserWithPathFormat {

	List<PatternParser> parts = new ArrayList<PatternParser>();
	UrlPathParser urlPathParser;
	Set<String> methods = new HashSet<String>(); //target methods
	boolean isRestful = false;

	public class WithParam implements PatternParser {

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

	public class OnRestful implements PatternParser {

		@Override
		public boolean parse(EditableParams params, String path) {
			String method = getMethod(params);
			
			params.addExtension(PatternInvokerBuilder.ACTION_PLACEHOLDER_LABEL, method.toLowerCase());
			return true;
		}

		@Override
		public String findSpecifiedPlaceHolder(String placeHolder) {
			return null;
		}
	}
	
	public class Method implements PatternParser{

		@Override
		public boolean parse(EditableParams params, String path) {
			String method = getMethod(params);
			return methods.contains(method.toLowerCase());
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

	public BasicUrlParser add(PatternParser parser) {
		this.parts.add(parser);
		return this;
	}

	public BasicUrlParser withParam(String key, String value) {
		this.parts.add(new WithParam(key, value));
		return this;
	}

	public BasicUrlParser onGet() {
		return method(PatternInvokerBuilder.GET);
	}

	public BasicUrlParser onPost() {
		return method(PatternInvokerBuilder.POST);
	}

	public BasicUrlParser onHead() {
		return method(PatternInvokerBuilder.HEAD);
	}

	public BasicUrlParser onOptions() {
		return method(PatternInvokerBuilder.OPTIONS);
	}

	public BasicUrlParser onDelete() {
		return method(PatternInvokerBuilder.DELETE);
	}

	public BasicUrlParser onPut() {
		return method(PatternInvokerBuilder.PUT);
	}

	public BasicUrlParser onRestful() {
		isRestful = true;
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

	public BasicUrlParser method(String method) {
		methods.add(method);
		return this;
	}
	
	
	@Override
	public boolean parse(EditableParams params, String path) {

		if(!isRestful){
			if(methods.isEmpty()){
				methods.add(PatternInvokerBuilder.GET);
			}
			
			String method = getMethod(params);
			if(!methods.contains(method)){
				return false;
			}
		}
		
		for (PatternParser part : parts) {
			if (!part.parse(params, path))
				return false;
		}

		return true;
	}

	@Override
	public String findSpecifiedPlaceHolder(String placeHolder) {
		for (PatternParser part : parts) {
			String ph = part.findSpecifiedPlaceHolder(placeHolder);
			if(ph != null) return ph;
		}
		return null;
	}

	@Override
	public UrlPathParser getUrlPathParser() {
		return this.urlPathParser;
	}

	private String getMethod(EditableParams params) {
		String method = params.getMethod();
		if(method == null) method = PatternInvokerBuilder.GET;
		return method.toLowerCase();
	}
	
}
