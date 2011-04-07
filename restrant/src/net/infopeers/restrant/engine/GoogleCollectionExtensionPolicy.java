package net.infopeers.restrant.engine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;


public class GoogleCollectionExtensionPolicy implements ExtensionPolicy {

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
