package net.infopeers.restrant.engine.parser;

import java.util.ArrayList;
import java.util.List;

import net.infopeers.restrant.engine.PatternInvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.params.EditableParams;

public class BasicPatternParser implements PatternParserWithPathFormat {

	List<PatternParser> parts = new ArrayList<PatternParser>();
	UrlPathParser urlPathParser;
	List<String> methods = new ArrayList<String>(); //target methods
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
			String method = PatternParserUtils.getMethod(params);
			
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
			String method = PatternParserUtils.getMethod(params);
			return methods.contains(method.toLowerCase());
		}

		@Override
		public String findSpecifiedPlaceHolder(String placeHolder) {
			return null;
		}
	}

	public BasicPatternParser(String pathFormat, PlaceholderFormatter phFormatter) {
		this.urlPathParser = new UrlPathParser(phFormatter, pathFormat);

		this.parts.add(urlPathParser);
	}

	public BasicPatternParser add(PatternParser parser) {
		this.parts.add(parser);
		return this;
	}

	public BasicPatternParser withParam(String key, String value) {
		this.parts.add(new WithParam(key, value));
		return this;
	}

	public BasicPatternParser onGet() {
		return method(PatternInvokerBuilder.GET);
	}

	public BasicPatternParser onPost() {
		return method(PatternInvokerBuilder.POST);
	}

	public BasicPatternParser onHead() {
		return method(PatternInvokerBuilder.HEAD);
	}

	public BasicPatternParser onOptions() {
		return method(PatternInvokerBuilder.OPTIONS);
	}

	public BasicPatternParser onDelete() {
		return method(PatternInvokerBuilder.DELETE);
	}

	public BasicPatternParser onPut() {
		return method(PatternInvokerBuilder.PUT);
	}

	public BasicPatternParser onRestful() {
		isRestful = true;
		this.parts.add(new OnRestful());
		return this;
	}

	public BasicPatternParser controller(String controller) {
		this.parts.add(new WithParam(
				PatternInvokerBuilder.CONTROLLER_PLACEHOLDER_LABEL, controller));
		return this;
	}

	public BasicPatternParser action(String action) {
		this.parts.add(new WithParam(PatternInvokerBuilder.ACTION_PLACEHOLDER_LABEL,
				action));
		return this;
	}

	public BasicPatternParser method(String method) {
		methods.add(method);
		return this;
	}
	
	
	@Override
	public boolean parse(EditableParams params, String path) {

		if(!isRestful){
			if(methods.isEmpty()){
				//any methods process
			}
			else{
				String method = PatternParserUtils.getMethod(params);
				if(!methods.contains(method)){
					return false;
				}
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

	@Override
	public List<String> getHttpMethods() {
		return methods;
	}
	
}
