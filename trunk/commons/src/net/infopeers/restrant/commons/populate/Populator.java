package net.infopeers.restrant.commons.populate;

public interface Populator {

	public static final Object[] INVALID_OBJECT = new Object[] {};

	/**
	 * type�^�̃C���X�^���X�𐶐����Afrom�̒l���ڐA���Ď擾����B
	 */
	<T> T clone(Class<T> type, Object from);
	
	/**
	 * ��̃C���X�^���X�Œl���ڐA����B
	 * @param from 
	 * @param to
	 */
	void populate(Object from, Object to);
}