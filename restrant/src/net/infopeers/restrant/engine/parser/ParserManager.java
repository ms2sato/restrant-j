package net.infopeers.restrant.engine.parser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.infopeers.restrant.Params;
import net.infopeers.restrant.engine.params.EditableParams;
import net.infopeers.restrant.engine.params.ExtensionMultimapFactory;
import net.infopeers.restrant.engine.params.ParamsImpl;

public class ParserManager {

	private List<PatternParser> parsers;
	private HttpServletRequest req;
	
	private EditableParams params;
	private PatternParser selectedParser;
	
	private final ExtensionMultimapFactory exPolicy;
	
	public ParserManager(ExtensionMultimapFactory exPolicy){
		this.exPolicy = exPolicy;
	}
	
	public void setRequest(HttpServletRequest req){
		this.req = req;
	}
	
	public void setParsers(List<PatternParser> parsers){
		this.parsers = parsers;
	}
	
	public PatternParser getSelectedParser(){
		return selectedParser;
	}
	
	public Params getSelectedParam(){
		return params;
	}
	
	public void select(String path){
		for (PatternParser parser : parsers) {
			 EditableParams p = createParams();
			if (parser.parse(p, path)) {
				params = p;
				selectedParser = parser;
				break;
			}
		}
	}
	
	EditableParams createParams(){
		return new ParamsImpl(exPolicy.create(), req);
	}
	
	
	
}
