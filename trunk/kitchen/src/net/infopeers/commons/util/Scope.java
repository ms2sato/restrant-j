package net.infopeers.commons.util;

public interface Scope<T> {
	T start() throws Exception;
}
