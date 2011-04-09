package net.infopeers.restrant.engine.params;

import java.util.List;
import java.util.Set;

/**
 * パラメータ拡張のポリシー
 * GAEやGoogleCollectionで別のパッケージのMultiMapを
 * 扱うため、切り分けた。
 * @author ms2
 *
 */
public interface ExtensionMultimap {

	List<String> getExtensionListOf(String key);

	Set<String> getExtensionNames();

	void addExtension(String key, String value);
}