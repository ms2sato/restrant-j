/**
 * 
 */
package jp.co.scio.syare.util.pool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SimpleObjectPool<Key, Value> {
	private Creator<Key, Value> creator;

	private HashMap<Key, Value> map = new HashMap<Key, Value>();

	public SimpleObjectPool(Creator<Key, Value> creator) {
		this.creator = creator;
	}

	public Collection<Value> values() {
		return map.values();
	}

	public Set<Key> keySet() {
		return map.keySet();
	}

	/**
	 * key�ɑΉ�����l���擾����B
	 * ���݂��Ȃ��ꍇ�͐V���ɍ쐬�����B
	 * @param key �L�[
	 * @return key�ɑΉ�����l
	 */
	public Value get(Key key) {
		Value value = map.get(key);
		if (value == null) {
			value = creator.create(key);
			map.put(key, value);
		}
		return value;
	}

	/**
	 * �s�v�ȃL���b�V�����폜����B
	 * @param keys �L���ȃL�[�̃R���N�V����
	 */
	public void normalize(Collection<Key> keys) {
		List<Key> unused = new ArrayList<Key>();
		for (Key key : map.keySet()) {
			if (!keys.contains(key)) {
				unused.add(key);
			}
		}

		for (Key key : unused) {
			map.remove(key);
		}
	}

	/**
	 * �s�v�ȃL���b�V�����폜����B
	 * @param keys �L���ȃL�[�̔z��
	 */
	public void normalize(Key[] keys) {
		normalize(Arrays.asList(keys));
	}
}