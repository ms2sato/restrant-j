package net.infopeers.restrant.engine.parser;

import java.util.ArrayList;
import java.util.List;

import net.infopeers.restrant.engine.ParserHolder;

public class CompositeUrlParserArranger implements PatternParserArranger {

	private List<PatternParserArranger> arrangers = new ArrayList<PatternParserArranger>();
	
	public void add(PatternParserArranger arranger){
		arrangers.add(arranger);
	}
	
	@Override
	public void arrange(ParserHolder parserHolder) {
		
		for(PatternParserArranger arranger: arrangers){
			arranger.arrange(parserHolder);
		}
		
	}

}
