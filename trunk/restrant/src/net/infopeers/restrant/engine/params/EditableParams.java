package net.infopeers.restrant.engine.params;

import net.infopeers.restrant.Params;

/**
 * 編集可能なParams
 * @author ms2
 *
 */
public interface EditableParams extends Params {
	
	/**
	 * URLから取得した拡張パラメータを追加する
	 * @param key キー文字列
	 * @param value 値文字列
	 */
	void addExtension(String key, String value);


}
