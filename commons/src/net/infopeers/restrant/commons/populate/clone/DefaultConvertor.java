package net.infopeers.restrant.commons.populate.clone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import net.infopeers.restrant.commons.populate.Populator;


public class DefaultConvertor implements Convertor {

	private List convertors = new LinkedList();

	private int maxLevel = 12;

	public void add(Convertor convertor) {
		convertors.add(0, convertor);
	}

	public void setMaxLevel(int level) {
		this.maxLevel = level;
	}

	public Object convert(Invoker invoker, int level) {

		if (!canContinue(invoker, level))
			return Populator.INVALID_OBJECT;

		for (Iterator itr = convertors.iterator(); itr.hasNext();) {
			Convertor c = (Convertor) itr.next();
			Object o = c.convert(invoker, level + 1);
			if (o != null)
				return o;
		}

		// 無効なオブジェクトを返す
		return Populator.INVALID_OBJECT;
	}

	protected boolean canContinue(Invoker invoker, int level) {
		return level <= maxLevel;
	}

	public Collection createCollection(Class targClass) {
		Collection col;
		if (targClass.isInterface()) {

			if (List.class.isAssignableFrom(targClass)) {
				col = new ArrayList();
			} else if (Set.class.isAssignableFrom(targClass)) {
				col = new HashSet();
			} else if (Queue.class.isAssignableFrom(targClass)) {
				col = new LinkedList();
			} else {
				col = new ArrayList();
			}
		} else {
			try {
				col = (Collection) targClass.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return col;
	}
}
