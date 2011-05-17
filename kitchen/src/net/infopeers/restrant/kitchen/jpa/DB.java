package net.infopeers.restrant.kitchen.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.infopeers.restrant.kitchen.StorageException;

public class DB {

	private static EntityManagerFactory factory;
	
	public static void setEntityManagerFactory(EntityManagerFactory value){
		factory = value;
	}
	
	public static EntityManager getEntityManager(){
		return factory.createEntityManager();
	}
	
	public static <T> StringBuffer select(Class<T> cls) {
		StringBuffer sb = new StringBuffer("select t from "
				+ cls.getName() + " t");
		return sb;
	}

	public static <T> T getSimple(final Class<T> cls, final Object id)
			throws StorageException {

		return new DBScope<T>() {

			@Override
			protected T run(EntityManager em) {
				return em.find(cls, id);
			}

		}.start();

	}
	
	public static <T> T putSimple(final T obj) throws StorageException {

		new DBScope<Void>() {

			@Override
			protected Void run(EntityManager em) {
				em.persist(obj);
				return null;
			}

		}.start();

		return obj;
	}
}
