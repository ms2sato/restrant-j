package net.infopeers.restrant.commons.populate.clone;

public interface TargetInstanceCreator {

	Object getTargetInstance(Object from, Invoker invoker);

}
