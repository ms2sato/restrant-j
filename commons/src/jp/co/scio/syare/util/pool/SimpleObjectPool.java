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
	 * keyに対応する値を取得する。
	 * 存在しない場合は新たに作成される。
	 * @param key キー
	 * @return keyに対応する値
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
	 * 不要なキャッシュを削除する。
	 * @param keys 有効なキーのコレクション
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
	 * 不要なキャッシュを削除する。
	 * @param keys 有効なキーの配列
	 */
	public void normalize(Key[] keys) {
		normalize(Arrays.asList(keys));
	}
}