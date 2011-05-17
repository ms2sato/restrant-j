package net.infopeers.restrant.kitchen.jpa;

import javax.persistence.EntityManager;

import net.infopeers.commons.util.Scope;
import net.infopeers.restrant.kitchen.StorageException;


public abstract class DBScope<T> implements Scope<T> {

	protected abstract T run(EntityManager em) throws StorageException;

	@Override
	public T start() throws StorageException {

		EntityManager em = null;
		try {
			em = DB.getEntityManager();
			return run(em);
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
