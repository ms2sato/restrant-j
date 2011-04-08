package net.infopeers.restrant.engine.parser;

import java.util.ArrayList;
import java.util.List;

import net.infopeers.restrant.engine.InvokerBuilder;

public class CompositeUrlParserArranger implements UrlParserArranger {

	private List<UrlParserArranger> arrangers = new ArrayList<UrlParserArranger>();
	
	public void add(UrlParserArranger arranger){
		arrangers.add(arranger);
	}
	
	@Override
	public void arrange(InvokerBuilder invokerBuilder) {
		
		for(UrlParserArranger arranger: arrangers){
			arranger.arrange(invokerBuilder);
		}
		
	}

}
