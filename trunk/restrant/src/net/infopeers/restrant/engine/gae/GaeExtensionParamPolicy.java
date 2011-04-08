package net.infopeers.restrant.engine.gae;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.infopeers.restrant.engine.params.ExtensionParamPolicy;

import com.google.appengine.repackaged.com.google.common.collect.ArrayListMultimap;

/**
 * GAEのコレクションを利用した拡張パラメータポリシー
 * @author ms2
 *
 */
public class GaeExtensionParamPolicy implements ExtensionParamPolicy {

	private ArrayListMultimap<String, String> extensions = ArrayListMultimap
			.create();

	@Override
	public List<String> getExtensionListOf(String key) {
		return extensions.get(key);
	}

	@Override
	public Set<String> getExtensionNames() {
		return new HashSet<String>(extensions.keySet());
	}

	@Override
	public void addExtension(String key, String value) {
		extensions.put(key, value);
	}

}
