package net.infopeers.restrant.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.infopeers.restrant.engine.params.ExtensionMultimap;

import com.google.common.collect.ArrayListMultimap;

/**
 * GoogleCollectionを使用したExtensionParamPolicy
 * ControllerServletでClass.forNameで呼ばれるのでリファクタは注意
 * @author ms2
 *
 */
public class GoogleCollectionExtensionParamPolicy implements ExtensionMultimap {

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
