package net.infopeers.restrant.kitchen.jpa;

public interface PreloadHandler<T> {
	void handle(T t);
}
