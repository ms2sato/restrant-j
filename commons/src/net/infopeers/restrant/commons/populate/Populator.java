package net.infopeers.restrant.commons.populate;

public interface Populator {

	public static final Object[] INVALID_OBJECT = new Object[] {};

	/**
	 * type型のインスタンスを生成し、fromの値を移植して取得する。
	 */
	<T> T clone(Class<T> type, Object from);
	
	/**
	 * 二つのインスタンスで値を移植する。
	 * @param from 
	 * @param to
	 */
	void populate(Object from, Object to);
}