package net.infopeers.restrant.engine;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.infopeers.restrant.Params;
import net.infopeers.restrant.engine.gae.GaeExtensionPolicy;

public class ParserManager {

	private List<Parser> parsers;
	private HttpServletRequest req;
	
	private EditableParams params;
	private Parser selectedParser;
	
	private final ExtensionPolicy exPolicy;
	
	public ParserManager(ExtensionPolicy exPolicy){
		this.exPolicy = exPolicy;
	}
	
	public void setRequest(HttpServletRequest req){
		this.req = req;
	}
	
	public void setParsers(List<Parser> parsers){
		this.parsers = parsers;
	}
	
	public Parser getSelectedParser(){
		return selectedParser;
	}
	
	public Params getSelectedParam(){
		return params;
	}
	
	public void execute(String path){
		for (Parser parser : parsers) {
			 EditableParams p = createParams();
			if (parser.parse(p, path)) {
				params = p;
				selectedParser = parser;
				break;
			}
		}
	}
	
	EditableParams createParams(){
		return new ParamsImpl(exPolicy, req);
	}
	
	
	
}
