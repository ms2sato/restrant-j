package net.infopeers.restrant.engine;

import java.util.ArrayList;
import java.util.List;

public class CompositeParserArranger implements ParserArranger {

	private List<ParserArranger> arrangers = new ArrayList<ParserArranger>();
	
	public void add(ParserArranger arranger){
		arrangers.add(arranger);
	}
	
	@Override
	public void arrange(InvokerBuilder invokerBuilder) {
		
		for(ParserArranger arranger: arrangers){
			arranger.arrange(invokerBuilder);
		}
		
	}

}
