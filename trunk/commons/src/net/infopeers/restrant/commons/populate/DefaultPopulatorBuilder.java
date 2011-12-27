package net.infopeers.restrant.commons.populate;

import net.infopeers.restrant.commons.populate.clone.Bean2BeanConvertor;
import net.infopeers.restrant.commons.populate.clone.Convertor;
import net.infopeers.restrant.commons.populate.clone.DefaultConvertor;
import net.infopeers.restrant.commons.populate.clone.TargetInstanceCreator;
import net.infopeers.restrant.commons.populate.clone.leaf.Array2ArrayConvertor;
import net.infopeers.restrant.commons.populate.clone.leaf.Array2CollectionConvertor;
import net.infopeers.restrant.commons.populate.clone.leaf.AssignableConvertor;
import net.infopeers.restrant.commons.populate.clone.leaf.Collection2ArrayConvertor;
import net.infopeers.restrant.commons.populate.clone.leaf.Collection2CollectionConvertor;
import net.infopeers.restrant.commons.populate.clone.leaf.StringConvertor;

public class DefaultPopulatorBuilder {

	final DefaultConvertor convertor;

	public DefaultPopulatorBuilder(){
		this(true);
	}
	
	public DefaultPopulatorBuilder(boolean onDefault) {
		convertor = new DefaultConvertor();

		if(onDefault){
			convertor.add(new AssignableConvertor());
	
			convertor.add(new StringConvertor());
	
			convertor.add(new Collection2ArrayConvertor(convertor));
	
			convertor.add(new Collection2CollectionConvertor(convertor));
	
			convertor.add(new Array2CollectionConvertor(convertor));
	
			convertor.add(new Array2ArrayConvertor(convertor));
		}
	}
	
	public DefaultConvertor getRoot(){
		return convertor;
	}

	public void add(Convertor convertor){
		this.convertor.add(convertor);
	}
	
	public void addBean2Bean(Class from, Class to) {
		convertor.add(new Bean2BeanConvertor(convertor, from, to));
	}

	public void addBean2Bean(Class from, Class to,
			TargetInstanceCreator tiCreator) {
		convertor.add(new Bean2BeanConvertor(convertor, from, to, tiCreator));
	}

	public void setMaxLevel(int level) {
		convertor.setMaxLevel(level);
	}

	public Populator create() {
		return new PopulatorImpl(convertor);
	}
}
